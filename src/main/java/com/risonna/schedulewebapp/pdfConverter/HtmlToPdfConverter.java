package com.risonna.schedulewebapp.pdfConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;


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
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;


@Named
@RequestScoped
public class HtmlToPdfConverter implements Serializable {
    public HtmlToPdfConverter() {

    }

    public void convertPdf() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Convert the HTML content to a PDF byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PdfWriter writer1 = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer1)) {
            pdf.setDefaultPageSize(new PageSize(1812, 756));
            pdf.addNewPage();

            // Configure CSS styling
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.SCREEN));

            // Save the current HTML page to an InputStream
            ByteArrayOutputStream baosd = new ByteArrayOutputStream();
            saveCurrentHtmlPageToOutputStream(baosd);
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
        }

        // Prepare the HTTP response for downloading the PDF
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        // Configure the response headers
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"table.pdf\"");

        // Write the PDF file content to the response output stream
        try (OutputStream responseOutputStream = response.getOutputStream()) {
            baos.writeTo(responseOutputStream);
        }

        // End the JSF request
        facesContext.responseComplete();
        System.out.println("After writing PDF content to response output stream");
    }



    public void saveCurrentHtmlPageToOutputStream(OutputStream outputStream) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResponseWriter originalWriter = facesContext.getResponseWriter();
        StringWriter writer = new StringWriter();

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
            String htmlContent = writer.toString();

            // Write the HTML content to the output stream
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write(htmlContent);
            outputStreamWriter.flush();

            System.out.println("HTML page saved to output stream");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
