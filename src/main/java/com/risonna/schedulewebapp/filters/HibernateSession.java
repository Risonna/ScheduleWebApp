package com.risonna.schedulewebapp.filters;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.risonna.schedulewebapp.hibernate.HibernateUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.hibernate.SessionFactory;

@WebFilter(filterName = "HibernateSession",
        urlPatterns = {"/pages/GroupsSchedule.xhtml", "/pages/TeacherSchedule.xhtml", "/pages/CabinetsSchedule.xhtml", "/pages/DepartmentSchedule.xhtml",
        "/pages/editing/parsing.xhtml", "/pages/editing/testing.xhtml", "/pages/editing/addTeacherAdmin.xhtml", "/pages/login.xhtml",
        "/pages/register.xhtml", "/pages/editing/dataAutomatic.xhtml"})
public class HibernateSession implements Filter {

    private SessionFactory sessionFactory;
    private static final boolean debug = false;
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;

    public HibernateSession() {
    }

    private void doBeforeProcessing(HibernateSession.RequestWrapper request, HibernateSession.ResponseWrapper response)
            throws IOException, ServletException {
        if (debug) {
            log("CheckSessionFilter:DoBeforeProcessing");
        }
    }

    private void doAfterProcessing(HibernateSession.RequestWrapper request, HibernateSession.ResponseWrapper response)
            throws IOException, ServletException {
        if (debug) {
            log("CheckSessionFilter:DoAfterProcessing");
        }
    }

    /**
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain    The filter chain we are processing
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HibernateSession.RequestWrapper wrappedRequest = new HibernateSession.RequestWrapper((HttpServletRequest) request);
        HibernateSession.ResponseWrapper wrappedResponse = new HibernateSession.ResponseWrapper((HttpServletResponse) response);

        try {
            System.out.println("Open session for " + wrappedRequest.getRequestURI());
            sessionFactory.getCurrentSession().beginTransaction();
            chain.doFilter(wrappedRequest, wrappedResponse);
            sessionFactory.getCurrentSession().getTransaction().commit();
            System.out.println("Close session for " + wrappedRequest.getRequestURI());
        } catch (Exception e) {
            e.printStackTrace();
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
        } finally {
            if (sessionFactory.getCurrentSession().isOpen()) {
                sessionFactory.getCurrentSession().close();
            }
        }
    }


    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        sessionFactory = HibernateUtil.getSessionFactory();
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("CheckSessionFilter: Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("CheckSessionFilter()");
        }
        StringBuilder sb = new StringBuilder("CheckSessionFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());

    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }


    class RequestWrapper extends HttpServletRequestWrapper {

        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }
        // You might, for example, wish to add a setParameter() method. To do this
        // you must also override the getParameter, getParameterValues, getParameterMap,
        // and getParameterNames methods.
        protected Hashtable localParams = null;

        public void setParameter(String name, String[] values) {
            if (debug) {
                System.out.println("CheckSessionFilter::setParameter(" + name + "=" + values + ")" + " localParams = " + localParams);
            }

            if (localParams == null) {
                localParams = new Hashtable();
                // Copy the parameters from the underlying request.
                Map wrappedParams = getRequest().getParameterMap();
                Set keySet = wrappedParams.keySet();
                for (Iterator it = keySet.iterator(); it.hasNext();) {
                    Object key = it.next();
                    Object value = wrappedParams.get(key);
                    localParams.put(key, value);
                }
            }
            localParams.put(name, values);
        }

        @Override
        public String getParameter(String name) {
            if (debug) {
                System.out.println("CheckSessionFilter::getParameter(" + name + ") localParams = " + localParams);
            }
            if (localParams == null) {
                return getRequest().getParameter(name);
            }
            Object val = localParams.get(name);
            if (val instanceof String) {
                return (String) val;
            }
            if (val instanceof String[]) {
                String[] values = (String[]) val;
                return values[0];
            }
            return (val == null ? null : val.toString());
        }

        @Override
        public String[] getParameterValues(String name) {
            if (debug) {
                System.out.println("CheckSessionFilter::getParameterValues(" + name + ") localParams = " + localParams);
            }
            if (localParams == null) {
                return getRequest().getParameterValues(name);
            }
            return (String[]) localParams.get(name);
        }

        @Override
        public Enumeration getParameterNames() {
            if (debug) {
                System.out.println("CheckSessionFilter::getParameterNames() localParams = " + localParams);
            }
            if (localParams == null) {
                return getRequest().getParameterNames();
            }
            return localParams.keys();
        }

        @Override
        public Map getParameterMap() {
            if (debug) {
                System.out.println("CheckSessionFilter::getParameterMap() localParams = " + localParams);
            }
            if (localParams == null) {
                return getRequest().getParameterMap();
            }
            return localParams;
        }
    }


    class ResponseWrapper extends HttpServletResponseWrapper {

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

    }
}
