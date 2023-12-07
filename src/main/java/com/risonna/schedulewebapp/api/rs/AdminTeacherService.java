package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.database.DataHelper;
import com.risonna.schedulewebapp.jwt.JWTClass;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/adminTeacher")
public class AdminTeacherService {

    @Inject
    private DataHelper dataHelper;

    @Context
    private HttpHeaders headers;

    @POST
    @Path("/addTeacher")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertAdminsTeachers(String teacherLogin) {
        System.out.println("Before datahelper adding teacher");

        // Verify the JWT
        String jwtToken = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (verifyToken(jwtToken, "admin")) {
            if (teacherLogin != null) {
                dataHelper.insertAdminsTeachers(teacherLogin);
            }
            System.out.println("After datahelper adding teachers");
            return Response.status(Response.Status.OK).build();
        } else {
            System.out.println(jwtToken);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing JWT").build();
        }
    }

    @DELETE
    @Path("/deleteTeacher/{teacherId}")
    public Response deleteAdminTeacher(@PathParam("teacherId") String teacherId) {
        System.out.println("TeacherId is " + teacherId);
        // Verify the JWT
        String jwtToken = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (verifyToken(jwtToken, "admin")) {
            dataHelper.deleteAdminTeacher(teacherId);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing JWT").build();
        }
    }

    private boolean verifyToken(String jwtToken, String systemRole) {
        return new JWTClass().verifyToken(jwtToken, systemRole);
    }
}