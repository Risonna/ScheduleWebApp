package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.database.DataHelper;
import com.risonna.schedulewebapp.hibernate.HibernateUtil;
import com.risonna.schedulewebapp.hibernate.entity.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.SessionFactory;

import java.util.List;

@Path("/get-all-info")
public class RSData {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-lessons")
    public List<Lesson> getAllLessons(@QueryParam("teacherId") int teacherId, @QueryParam("cabinetId") int cabinetId) {
        if (teacherId != 0 && cabinetId==0) {
            // If department is provided, filter by department
            return DataHelper.getInstance().getLessonsByTeacher(teacherId);
        } else if (teacherId==0 && cabinetId!=0) {
            return DataHelper.getInstance().getLessonsByCabinet(cabinetId);
        } else {
            // If department is not provided, get all teachers
            return DataHelper.getInstance().getAllLessons();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-teachers")
    public List<Teacher> getAllTeachers(@QueryParam("department") String department) {
        if (department != null && !department.isEmpty()) {
            // If department is provided, filter by department
            return DataHelper.getInstance().getTeachersByParameters(department);
        } else {
            // If department is not provided, get all teachers
            return DataHelper.getInstance().getAllTeachers();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-subjects")
    public List<Subject> getAllSubjects() {
        return DataHelper.getInstance().getAllSubjects();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-cabinets")
    public List<Cabinet> getAllCabinets() {
        return DataHelper.getInstance().getAllCabinets();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-groups")
    public List<Group> getAllGroups() {
        return DataHelper.getInstance().getAllGroups();
    }
}