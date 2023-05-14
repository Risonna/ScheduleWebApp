package com.risonna.schedulewebapp.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


@Named
@SessionScoped
public class User implements Serializable {
    private String username;
    private String password;
    private boolean logged;

    public User(){

    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void setUsername(String username){
        this.username = username;

    }
    public void setPassword(String password){
        this.password = password;
    }


    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }

    public boolean isLogged() {
        return this.logged;
    }

    public String login() {
        try {

            HttpServletRequest request =  ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());

            if (request.getSession(false)!=null){
                request.logout();
            }

            request.login(username, password);
            this.logged = true;

            return "homePage";
        } catch (ServletException ex) {
            ResourceBundle bundle = ResourceBundle.getBundle("ru.javabegin.training.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(bundle.getString("login_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("login_form", message);

        }

        return "index";

    }

    public String logout() {
        String result = "/index.xhtml?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
        } catch (ServletException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        this.logged = false;

        return result;
    }

}
