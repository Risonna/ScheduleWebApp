package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.pdfConverter.HtmlToPdfConverterAsync;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;



@Path("/pdf")
public class PdfResource {
    @POST
    @Consumes(MediaType.TEXT_HTML)
    public Response generatePdf(String html, @QueryParam("taskId") String taskId) {
        new HtmlToPdfConverterAsync().convertPdfAsync(html, taskId); // Start async PDF generation
        // Use a CompletableFuture to handle the asynchronous result
        return Response.ok().build(); // Return the unique identifier to the client
    }
}
