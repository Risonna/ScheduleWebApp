package com.risonna.schedulewebapp.pdfConverter;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.risonna.schedulewebapp.websocket.PdfWebSocket;
import jakarta.annotation.Resource;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Named
@Stateless
public class HtmlToPdfConverterAsync {

    public HtmlToPdfConverterAsync() {
    }
    public void takeThePdf() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        ResponseWriter originalWriter = facesContext.getResponseWriter();
        StringWriter writer = new StringWriter();
        String htmlContent;

        try {
            // Create a new ResponseWriter to capture the HTML content
            ResponseWriter capturingWriter = facesContext.getRenderKit().createResponseWriter(writer, null, "UTF-8");
            facesContext.setResponseWriter(capturingWriter);

            // Render the current view to capture the HTML content
            facesContext.getViewRoot().encodeAll(facesContext);

            // Flush the captured HTML content to the writer
            capturingWriter.flush();

            // Restore the original ResponseWriter
            if (originalWriter != null) {
                facesContext.setResponseWriter(originalWriter);
            } else {
                ResponseWriter newOriginalWriter = facesContext.getRenderKit().createResponseWriter(
                        facesContext.getExternalContext().getResponseOutputWriter(),
                        null, "UTF-8");
                facesContext.setResponseWriter(newOriginalWriter);
            }

            // Get the captured HTML content
            htmlContent = writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Prepare the HTTP response for downloading the PDF
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        // Configure the response headers
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"table.pdf\"");

        // Write the PDF file content to the response output stream
        try (OutputStream responseOutputStream = response.getOutputStream()) {
            byte[] pdfContent = convertToPdf(htmlContent);
            responseOutputStream.write(pdfContent);
        }
        // End the JSF request
        facesContext.responseComplete();
        System.out.println("After writing PDF content to response output stream");
    }

    @Asynchronous
    public void convertPdfAsync(String html, String taskId) {
        try {
            InitialContext context = new InitialContext();
            ManagedScheduledExecutorService managedScheduledExecutorService =
                    (ManagedScheduledExecutorService) context.lookup(
                            "concurrent/ScheduledPdfExecutorService");


            CompletableFuture<byte[]> pdfFuture = CompletableFuture.supplyAsync(() -> convertToPdf(html), managedScheduledExecutorService);
            pdfFuture.thenAccept(pdfData -> {
                // Store the pdfData with the taskId as a reference for later retrieval
                savePdfToFileSystem(taskId, pdfData);


                System.out.println("Before managedExecutorService");

                managedScheduledExecutorService.schedule(() -> {
                    PdfWebSocket.notifyClient(taskId); // Notify the client using WebSocket
                    System.out.println("In managedExecutorService");
                }, 5, TimeUnit.SECONDS);
                System.out.println("outside managedExecutorService");


            }).exceptionally(e -> {
                // Handle exceptions and possibly notify the client about the failure
                return null;
            });
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] convertToPdf(String html) {
        // Create a byte array output stream to store the PDF content
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PdfWriter writer1 = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer1)) {
            pdf.setDefaultPageSize(new PageSize(1812, 756));
            pdf.addNewPage();

            // Configure CSS styling
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.ALL));

            ByteArrayOutputStream baosd = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baosd, StandardCharsets.UTF_8);
            outputStreamWriter.write(html);
            outputStreamWriter.flush();
            try (InputStream htmlInputStream = new ByteArrayInputStream(baosd.toByteArray())) {

                // Parse the HTML using Jsoup
                Document document = Jsoup.parse(htmlInputStream, "UTF-8", "");

                // Extract the desired table element
                Element table = document.getElementById("lessonTable");

                // Remove all other elements from the document
                document.body().children().remove();

                // Append the desired table element to the body
                document.body().appendChild(table);

                // Convert the modified HTML to PDF with CSS styling
                HtmlConverter.convertToPdf(document.outerHtml(), pdf, converterProperties);
            }


        } catch (IOException ex) {
            System.out.println(ex);
        }

        // Convert the generated PDF content to a byte array
        return baos.toByteArray();
    }

    @Asynchronous
    private void savePdfToFileSystem(String taskId, byte[] pdfData) {
        Path pdfPath = Paths.get("./PDFstorage/" + taskId + ".pdf"); // Specify the directory path
        try {
            Files.createDirectories(pdfPath.getParent()); // Ensure the directory exists
            Files.write(pdfPath, pdfData); // Write the PDF bytes to the file
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }


}
