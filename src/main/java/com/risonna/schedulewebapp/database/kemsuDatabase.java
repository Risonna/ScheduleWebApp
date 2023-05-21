package com.risonna.schedulewebapp.database;

import com.risonna.schedulewebapp.beans.Teacher;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class kemsuDatabase {

    private List<Teacher> teacherList;
    private List<String> subjectList;

    public List<String> getSubjectList() {
        getSubjectsFromAPI();
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public List<Teacher> getTeacherList() {
        getTeachersFromAPI();
        return teacherList;
    }
    private List<String> groupList;

    public List<String> getGroupList() {
        getGroupsFromAPI();
        return groupList;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }

    public void setTeacherList(List<Teacher> teacher) {
        this.teacherList = teacher;
    }

    public void getGroupsFromAPI(){
        List<String> groupList = new ArrayList<>();
        try {
            URL url = new URL("https://api-next.kemsu.ru/api/stud-sch/main/groupList");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiYWNjZXNzIiwiaXAiOiIxNzYuMTk2LjE0OC43OSIsInVz" +
                    "ZXJJZCI6MjI0NTMsImlhdCI6MTY4NDY5MTU5MSwiZXhwIjoxNjg0Nzc3OTkxfQ.xzZardZBDRthMeJ3qjTIASsT-KFTVpvtaPd4MkD3vIY");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            try (JsonReader jsonReader = Json.createReader(new StringReader(content.toString()))) {
                JsonObject json = jsonReader.readObject();
                JsonArray results = json.getJsonArray("result");
                for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                    String groupName = result.getString("groupName");

                    groupList.add(groupName);

                    // Do something with the name, surname, and patronymic...
                    System.out.println("The group is: " + groupName);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        this.groupList = groupList;
    }
    public void getSubjectsFromAPI(){
        List<String> subjectList = new ArrayList<>();
        try {
            URL url = new URL("https://api-next.kemsu.ru/api/stud-sch/main/disciplineList");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiYWNjZXNzIiwiaXAiOiIxNzYuMTk2LjE0OC43OSIsInVz" +
                    "ZXJJZCI6MjI0NTMsImlhdCI6MTY4NDY5MTU5MSwiZXhwIjoxNjg0Nzc3OTkxfQ.xzZardZBDRthMeJ3qjTIASsT-KFTVpvtaPd4MkD3vIY");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            try (JsonReader jsonReader = Json.createReader(new StringReader(content.toString()))) {
                JsonObject json = jsonReader.readObject();
                JsonArray results = json.getJsonArray("result");
                subjectList.add("unknown");
                for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                    String disciplineName = result.getString("disciplineName");

                    subjectList.add(disciplineName);

                    // Do something with the name, surname, and patronymic...
                    System.out.println("The subject is: " + disciplineName);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        this.subjectList = subjectList;
    }
    public void getTeachersFromAPI() {
        List<Teacher> teachersList = new ArrayList<>();
        try {
            URL url = new URL("https://api-next.kemsu.ru/api/stud-sch/main/prepList");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiYWNjZXNzIiwiaXAiOiIxNzYuMTk2LjE0OC43OSIsInVz" +
                    "ZXJJZCI6MjI0NTMsImlhdCI6MTY4NDY5MTU5MSwiZXhwIjoxNjg0Nzc3OTkxfQ.xzZardZBDRthMeJ3qjTIASsT-KFTVpvtaPd4MkD3vIY");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            try (JsonReader jsonReader = Json.createReader(new StringReader(content.toString()))) {
                JsonObject json = jsonReader.readObject();
                JsonArray results = json.getJsonArray("result");
                for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                    String surname = result.getString("surname");
                    String name = result.getString("name");
                    String patronymic = result.getString("patronymic");
                    Teacher teacher = new Teacher();
                    teacher.setTeacherName(name);
                    teacher.setTeacherSurname(surname);
                    teacher.setTeacherPatronymic(patronymic);
                    teacher.setDepartment("Кафедра Цифровых Технологий");
                    teacher.setTitle("unknown");
                    teachersList.add(teacher);

                    // Do something with the name, surname, and patronymic...
                    System.out.println("The teacher is: " + surname + " " + name + " " + patronymic);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        this.teacherList = teachersList;
    }
}
