package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.database.DataHelper;
import com.risonna.schedulewebapp.hibernate.entity.Teacher;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/get-all-info")
public class DepartmentsService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-departments")
    public List<String> getDepartmentList(){
        List<String> listOfDepartments = new ArrayList<>();
        for (Teacher teacher: DataHelper.getInstance().getAllTeachers()) {
            if(!listOfDepartments.contains(teacher.getDepartment())){
                listOfDepartments.add(teacher.getDepartment());
            }

        }
        return listOfDepartments;
    }
}
