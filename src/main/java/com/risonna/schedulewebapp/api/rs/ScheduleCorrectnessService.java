package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.controllers.ScheduleController;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/correctnessSchedule")
public class ScheduleCorrectnessService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/checkCorrectness")
    public List<String> checkCorrectness() {
        ScheduleController scheduleController = new ScheduleController();
        return scheduleController.checkCabinetUsageNoFacesContext(scheduleController.getLessonsOk());

    }
}
