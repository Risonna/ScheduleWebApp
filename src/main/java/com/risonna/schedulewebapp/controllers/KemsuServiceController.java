package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.UserInfo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Named
@RequestScoped
public class KemsuServiceController implements Serializable {
    private UserInfo userInfo;

    public KemsuServiceController(){

    }

    public String authorizeKemsu(String login, String password) throws IOException {
        String url = "https://api-next.kemsu.ru/api/auth";
        String jsonInputString = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}";

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                try (JsonReader jsonReader = Json.createReader(new StringReader(response.toString()))) {
                    JsonObject jsonObject = jsonReader.readObject();

                    this.setUserInfo(new UserInfo());
                    JsonObject userinfo = jsonObject.getJsonObject("userInfo");

                    String accessToken = jsonObject.getString("accessToken");
                    userInfo.setAccessToken(accessToken);

                    String email = userinfo.getString("email");
                    userInfo.setEmail(email);
//
//                    String firstName = userinfo.getString("firstName");
//                    userInfo.setFirstName(firstName);
//
//                    String lastName = userinfo.getString("lastName");
//                    userInfo.setLastName(lastName);
//
//                    String middleName = userinfo.getString("middleName");
//                    userInfo.setMiddleName(middleName);
//
//                    String country = userinfo.getString("country");
//                    userInfo.setCountry(country);
//
//                    String town = userinfo.getString("town");
//                    userInfo.setTown(town);
                    System.out.println("all's okay in good method");

                    jsonReader.close();
                    return accessToken;

                }


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Whtathe shit in hell");
            }
        }
            else {
                System.out.println("Пользователь не прошел авторизацию");

                return "not_authorized";
            }

        connection.disconnect();
        return "somethingbad";
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
