package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.beans.User;
import com.risonna.schedulewebapp.database.DataHelper;
import com.risonna.schedulewebapp.hibernate.entity.Users;
import com.risonna.schedulewebapp.hibernate.entity.UsersGroups;
import com.risonna.schedulewebapp.jwt.JWTClass;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;

@Path("/auth")
public class AuthorizationService {

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserRegisterRequest request) throws NoSuchAlgorithmException {
        // Validate user input
        if (!isUsernameOkay(request.getName())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


        if(!request.getPassword().equals(request.getPasswordConfirm())){
            return Response.status(Response.Status.CONFLICT).build();
        }

        if(!isPasswordOkay(request.getPassword())){
            return Response.status(Response.Status.CONFLICT).build();
        }

        // Create a new user account in the database
        boolean registrationSuccess = registerUserInDatabase(request);
        if (registrationSuccess) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isUsernameAlreadyExists(String username) {
        // Implement your logic to check if the username already exists in the system
        // Return true if it exists, false otherwise

        List<Users> users = DataHelper.getInstance().getAllUsers();
        for (Users user: users) {
            if(user.getUserid().equals(username))return true;
        }
        return false;
    }

    private boolean isUsernameOkay(String value){
         final int MINIMUM_LENGTH = 6;
         final int MAXIMUM_LENGTH = 20;
         final String PATTERN = "[a-zA-Z0-9]*";
        if (value == null || value.length() < MINIMUM_LENGTH) {
            return false;
        }

        if (value.length() > MAXIMUM_LENGTH) {
            return false;
        }

        if (!value.matches(PATTERN)) {
            return false;
        }

        // Check if the username already exists (replace this with your own logic)
        return !isUsernameAlreadyExists(value);
    }
    private boolean isPasswordStrong(String password) {
        // Implement your logic to check if the password is strong enough
        // Return true if it is strong, false otherwise
        // You can define your own criteria for password complexity, such as requiring uppercase letters, numbers, symbols, etc.
        // Here's a simple example to check if the password contains at least one uppercase letter and one digit:
        return password.matches(".*[A-Z].*") && password.matches(".*\\d.*");
    }

    private boolean isPasswordOkay(String value){
         final int MINIMUM_LENGTH = 7;
         final int MAXIMUM_LENGTH = 25;
         final String PATTERN = "[a-zA-Z0-9]*";

        if (value == null || value.length() < MINIMUM_LENGTH) {
            return false;
        }

        if (value.length() > MAXIMUM_LENGTH) {
            return false;
        }

        if (!value.matches(PATTERN)) {
            return false;
        }

        return isPasswordStrong(value);
    }

    private boolean registerUserInDatabase(UserRegisterRequest request) throws NoSuchAlgorithmException {
        RegisterUserRequest user = new RegisterUserRequest();
        user.setPassword(request.getPassword());
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setRegistered_via_kemsu(false);
        user.registerUser();
        return user.registerUser().equals("homePage");

    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserLoginRequest request) throws NoSuchAlgorithmException {
        // Validate user input
        if (!isUsernameAlreadyExists(request.getUsername())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


        // Authenticate user credentials against the database
        boolean authenticationSuccess = isUsernameDataMatches(request.getUsername(), request.getPassword());
        if (authenticationSuccess) {
            // Generate an access token (you can use JWT or any other token format)
//            String accessToken = generateAccessToken(request.getEmail());
            String role = "";
            List<UsersGroups> usersGroups = DataHelper.getInstance().getAllUsersGroups();
            for (UsersGroups userGroup: usersGroups) {
                if(userGroup.getUserid().equals(request.getUsername()))role=userGroup.getGroupid();
            }

            JWTClass jwtClass = new JWTClass();
            String token = jwtClass.generateToken(request.getUsername(), role);

            // Build the JSON response containing the token
            JsonObjectBuilder jsonResponseBuilder = Json.createObjectBuilder();
            jsonResponseBuilder.add("token", token);
            JsonObject jsonResponse = jsonResponseBuilder.build();


            // Return the access token as a response
//            return Response.ok(accessToken).build();
            return Response.ok(jsonResponse).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    private boolean isUsernameDataMatches(String username, String password) throws NoSuchAlgorithmException {
        // Implement your logic to check if the username already exists in the system
        // Return true if it exists, false otherwise
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        String hashedPasswordStr = sb.toString();

        List<Users> users = DataHelper.getInstance().getAllUsers();
        for (Users user: users) {
            if(user.getUserid().equals(username) && user.getPassword().equals(hashedPasswordStr))return true;
        }
        return false;
    }
}
