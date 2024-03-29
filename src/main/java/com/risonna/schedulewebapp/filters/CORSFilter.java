package com.risonna.schedulewebapp.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "CORSFilter", urlPatterns = {"/api/get-all-info/*", "/api/auth/*", "/api/pdf/*", "/websocket/*", "/api/adminTeacher/*", "/api/excel/*"}, asyncSupported = true)
public class CORSFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
//        HttpServletResponse response1 = (HttpServletResponse) response;
//        response1.setHeader("Access-Control-Allow-Origin", "*");
//        response1.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response1.setHeader("Access-Control-Max-Age", "3600");
//        response1.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//

        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        ((HttpServletResponse) response).setHeader("Access-Control-Max-Age", "3600");
        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "x-requested-with, content-type, Upgrade, Sec-WebSocket-Key, Sec-WebSocket-Version, Authorization");


        chain.doFilter(request, response);
    }
}
