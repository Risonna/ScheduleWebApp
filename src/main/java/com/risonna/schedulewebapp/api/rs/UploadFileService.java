package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.database.DataHelper;
import com.risonna.schedulewebapp.excelparsing.ExcelSearch;
import com.risonna.schedulewebapp.hibernate.entity.Cabinet;
import com.risonna.schedulewebapp.hibernate.entity.Group;
import com.risonna.schedulewebapp.hibernate.entity.Subject;
import com.risonna.schedulewebapp.hibernate.entity.Teacher;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.risonna.schedulewebapp.excelparsing.ExcelSearch;

@Path("/excel")
public class UploadFileService {

    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void uploadExcelFile(byte[] excelBytes, @QueryParam("fileName") String filename) {
        try {
            // Specify the path where you want to save the file on the server
            java.nio.file.Path excelPath = Paths.get("./excelStorage/" + filename); // Specify the directory path
            try {
                Files.createDirectories(excelPath.getParent()); // Ensure the directory exists
                Files.write(excelPath, excelBytes); // Write the PDF bytes to the file
                System.out.println("Saves file on system, fileName is " + filename);
            } catch (IOException e) {
                // Handle exceptions
                e.printStackTrace();
            }

            parse(excelPath.toAbsolutePath().toString());
            System.out.println("Parsed the file with the path " + excelPath.toAbsolutePath());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void parse(String filePath) throws IOException {
        List<Teacher> teachers = DataHelper.getInstance().getAllTeachers();
        List<Subject> subjects = DataHelper.getInstance().getAllSubjects();
        List<Cabinet> cabinets = DataHelper.getInstance().getAllCabinets();
        List<Group> groups = DataHelper.getInstance().getAllGroups();
        ExcelSearch excelSearch = new ExcelSearch(filePath, teachers, subjects, cabinets, groups);



        excelSearch.parseStuff();


        DataHelper.getInstance().insertLessons(excelSearch.getLessonList());


    }
    private byte[] getPdfData(String fileName) {
        java.nio.file.Path pdfPath = Paths.get("./excelStorage/" + fileName);
        try {
            return Files.readAllBytes(pdfPath); // Read the PDF bytes from the file
        } catch (IOException e) {
            // Handle exceptions (e.g., file not found or access error)
            e.printStackTrace();
            return null;
        }
    }
}
