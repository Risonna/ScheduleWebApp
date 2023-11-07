package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.pdfConverter.HtmlToPdfConverterAsync;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


@Path("/pdf")
public class PdfResource {
    @POST
    @Consumes(MediaType.TEXT_HTML)
    @Produces("application/pdf")
    public void generatePdf(String html, @Suspended final AsyncResponse asyncResponse) {
        Future<byte[]> pdfDataFuture = new HtmlToPdfConverterAsync().convertPdfAsync(html);
        // Use a CompletableFuture to handle the asynchronous result
        CompletableFuture.supplyAsync(() -> {
            try {
                byte[] pdfData = pdfDataFuture.get(); // Extract the result when it's ready
                asyncResponse.resume(pdfData);
            } catch (Exception e) {
                // Handle any exceptions
                asyncResponse.resume(e);
            }
            return null;
        });
    }
}
