package com.risonna.schedulewebapp.beans;

import com.risonna.schedulewebapp.controllers.KemsuServiceController;
import com.risonna.schedulewebapp.database.DataHelper;
import com.risonna.schedulewebapp.hibernate.entity.Users;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


@Named
@RequestScoped
public class User implements Serializable {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String registration_time;
    private boolean registered_via_kemsu;
    private String accessToken;
    private boolean logged;
    private boolean useAlternativeMethod;

    public User(){

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRegistration_time(String registration_time) {
        this.registration_time = registration_time;
    }

    public String getEmail() {
        return email;
    }

    public String getRegistration_time() {
        return registration_time;
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
            HttpSession session = request.getSession(false);
            session.setAttribute("isLogged", isLogged());
            session.setAttribute("username", username);




            this.password = null;
            this.passwordConfirm = null;

            return "homePage";
        }catch (ServletException ex) {
            String baseName = "nls.messages"; // Without the "resources" folder and file extension
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(bundle.getString("login_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("loginForm", message);
            return null;
        }

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

    public String alternativeLogin() throws NoSuchAlgorithmException {
       String kemsuCode = connectToKemsu();
        if(isUserThere()){
            if(kemsuCode.equals("connected")){
                System.out.println("Connected to kemsu and found username");
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedPassword = md.digest(password.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hashedPassword) {
                    sb.append(String.format("%02x", b));
                }
                String hashedPasswordStr = sb.toString();
                for(Users user:DataHelper.getInstance().getAllUsers()){
                    if(user.getUserid().equals(username)){
                        if(!user.getPassword().equals(password)){
                            DataHelper.getInstance().updateUsersPassword(username, password);
                        }
                    }
                }

                login();

                if(isLogged()){
                    HttpServletRequest request =  ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
                    HttpSession session = request.getSession(false);
                    session.setAttribute("accessToken", accessToken);
                    return "homePage";
                }
                else{

                    return null;
                }
            }
            else{
                if(kemsuCode.equals("not_authorized")) {
                    System.out.println("couldn't connect to kemsu and found username");
                    String codeError;
                    String baseName = "nls.messages"; // Without the "resources" folder and file extension
                    ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage(bundle.getString("kemsu_not_authorized"));
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage("loginForm", message);
                }
                else{
                    System.out.println("couldn't connect to kemsu and found username, the kemsu is down perhaps");
                    String codeError;
                    String baseName = "nls.messages"; // Without the "resources" folder and file extension
                    ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage(bundle.getString("kemsu_cannot_connect"));
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage("loginForm", message);

                }
                return null;
            }

        }
        else{
            if(kemsuCode.equals("connected")){
                this.registered_via_kemsu = true;
                registerUser();
                HttpServletRequest request =  ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
                HttpSession session = request.getSession(false);
                session.setAttribute("accessToken", accessToken);
                System.out.println("Connected to kemsu and not found the username");
                return "homePage";

            }
            else{
                if(kemsuCode.equals("not_authorized")) {
                    System.out.println("Coildn't connect to kensu and not found username, username and password are: " + username + " " + password);
                    String codeError;
                    String baseName = "nls.messages"; // Without the "resources" folder and file extension
                    ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage(bundle.getString("kemsu_not_authorized"));
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage("loginForm", message);
                }
                else{
                    System.out.println("couldn't connect to kemsu and couldn't find the username, the kemsu is down perhaps");
                    String codeError;
                    String baseName = "nls.messages"; // Without the "resources" folder and file extension
                    ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage(bundle.getString("kemsu_cannot_connect"));
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage("loginForm", message);
                }
                return null;
            }

        }
    }

    private String connectToKemsu(){
        try {
            KemsuServiceController kemsuApi = new KemsuServiceController();
            String isAllOkay = kemsuApi.authorizeKemsu(username, password);
            if(!isAllOkay.equals("somethingbad") && !isAllOkay.equals("not_authorized")) {
                this.accessToken = kemsuApi.getUserInfo().getAccessToken();
                this.email = kemsuApi.getUserInfo().getEmail();
                System.out.println("Connected, the access and email are: " + email + " " + accessToken);
                return  "connected";

            }
            if(isAllOkay.equals("not_authorized"))return"not_authorized";
            else{
                System.out.println("Couldn't connect, the isAllOkay is " + isAllOkay);
                return "kemsu_error";
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String performLogin() throws NoSuchAlgorithmException {
        if (useAlternativeMethod) {
            return alternativeLogin();
        } else {
            List<Users> users = DataHelper.getInstance().getAllUsers();
            registered_via_kemsu = false;
            for(Users user: users){
                if(user.getUserid().equals(username)){
                    registered_via_kemsu = user.getRegisteredViaKemsu();
                    break;
                }
            }
            if(!registered_via_kemsu) return login();
            else{
                String baseName = "nls.messages"; // Without the "resources" folder and file extension
                ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage message = new FacesMessage(bundle.getString("login_error"));
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage("loginForm", message);
                return null;
            }
        }
    }

    public boolean isUserThere(){
        for(Users user:DataHelper.getInstance().getAllUsers()){
            if(user.getUserid().equals(username)){
                return true;
            }
        }
        return false;
    }

    public boolean isUseAlternativeMethod() {
        return useAlternativeMethod;
    }

    public void setUseAlternativeMethod(boolean useAlternativeMethod) {
        this.useAlternativeMethod = useAlternativeMethod;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isRegistered_via_kemsu() {
        return registered_via_kemsu;
    }

    public void setRegistered_via_kemsu(boolean registered_via_kemsu) {
        this.registered_via_kemsu = registered_via_kemsu;
    }

    public String registerUser() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        String hashedPasswordStr = sb.toString();

        DataHelper.getInstance().insertUsers(username, hashedPasswordStr, email, registered_via_kemsu);

        DataHelper.getInstance().insertUsersGroups(username, "user");
        this.passwordConfirm = null;
        login();
        hashedPasswordStr = null;

        return "homePage";

    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
