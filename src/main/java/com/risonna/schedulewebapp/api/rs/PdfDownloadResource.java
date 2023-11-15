package com.risonna.schedulewebapp.api.rs;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/pdf/{taskId}")
public class PdfDownloadResource {

    @GET
    @Produces("application/pdf")
    public Response getPdf(@PathParam("taskId") String taskId) {
        byte[] pdfData = getPdfData(taskId);
        if (pdfData != null) {
            return Response.ok(pdfData).header("Content-Disposition", "attachment; filename=\"" + taskId + ".pdf\"").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private byte[] getPdfData(String taskId) {
        java.nio.file.Path pdfPath = Paths.get("./PDFstorage/" + taskId + ".pdf");
        try {
            return Files.readAllBytes(pdfPath); // Read the PDF bytes from the file
        } catch (IOException e) {
            // Handle exceptions (e.g., file not found or access error)
            e.printStackTrace();
            return null;
        }
    }
}
