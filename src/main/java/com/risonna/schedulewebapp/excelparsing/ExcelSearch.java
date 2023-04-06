package com.risonna.schedulewebapp.excelparsing;


import com.risonna.schedulewebapp.beans.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelSearch {
    private Workbook workbook;
    private FileInputStream inputStreamFile;
    private List<Teacher> teacherNames;
    private List<Subject> subjects;
    private List<Cabinet> cabinetList;
    private List<Group> groupList;

    private ArrayList<Lesson> lessonArrayList = new ArrayList<>();

    public ExcelSearch(String pathString, List<Teacher> namesOfTeachers, List<Subject> subjectList, List<Cabinet> cabinetList, List<Group> groupList) throws IOException {

        inputStreamFile = new FileInputStream(pathString);
        workbook = new HSSFWorkbook(inputStreamFile);
        teacherNames = namesOfTeachers;
        subjects = subjectList;
        this.cabinetList = cabinetList;
        this.groupList = groupList;
    }


    public void parseStuff() throws IOException {
        for (Sheet sheet: workbook) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getRichStringCellValue().getString().trim();
                        for (Teacher teacher : teacherNames) {
                            if (cellValue.toLowerCase().contains(teacher.getTeacherName().toLowerCase())) {
                                // Print the position of the cell
                                Lesson lesson = new Lesson();
                                lesson.setTeacherId(teacher.getId());
                                lesson.setTeacherName(teacher.getTeacherName());
                                boolean isSubjectThere = false;
                                for (Subject subj : subjects) {
                                    if (cellValue.toLowerCase().contains(subj.getSubjectName().toLowerCase())) {
                                        isSubjectThere = true;
                                        int teacherIndex = cellValue.indexOf(teacher.getTeacherName());
                                        int subjectIndex = cellValue.indexOf(subj.getSubjectName());
                                        String betweenText = cellValue.substring(subjectIndex, teacherIndex).trim();
                                        lesson.setSubjectName(betweenText);
                                        lesson.setSubjectId(subj.getId());
                                    }
                                }
                                if (!isSubjectThere) {
                                    lesson.setSubjectName("Subject isn't specified or not found");
                                }
                                if (cellValue.toLowerCase().contains("ауд")) {
                                    int index = cellValue.indexOf("ауд") + 4; // add 4 to skip "ауд."
                                    String cabNumber = cellValue.substring(index).trim();
                                    lesson.setCabinetName(cabNumber);
                                    lesson.setCabinetId(0);
                                    for (Cabinet cabinet : cabinetList) {
                                        if(cabNumber.equals(cabinet.getCabinetName())){
                                            lesson.setCabinetId(cabinet.getId());
                                            break;
                                        }

                                    }
                                } else {
                                    lesson.setCabinetName("Cabinet not found or not specified");
                                    lesson.setCabinetId(0);
                                }
                                String cellTime;


                                cellTime = this.getTImeOfLesson(sheet, cell.getRowIndex());

                                //Print the time of thr cell
                                lesson.setLessonTime(cellTime);


                                String cellDay; //Variable for lesson's day

                                cellDay = this.getDayOfLesson(sheet, row, cell);

                                //Print lesson's day
                                lesson.setLessonDay(cellDay);


                                String[] groupNames; // Array for 2 group names above but from the method return

                                //returns array of 2 and moves it to our groupNames array
                                groupNames = this.getGroupNameAndFullName(sheet, cell);

                                // Print full and short names of a group
                                lesson.setGroupName(groupNames[0]);
                                lesson.setGroupNameFull(groupNames[1]);


                                String instituteName; // Variable for institute name

                                instituteName = this.getInstituteNames(sheet, cell);

                                // Print institute name
                                lesson.setInstituteName(instituteName);
                                lesson.setGroupId(0);
                                int sis = 0;
                                for (Group group : groupList) {
                                    if (sis==3){
                                        lesson.setGroupId(group.getId());
                                    }
                                    sis++;

                                }


                                lessonArrayList.add(lesson);


                            }
                        }
                    }
                }
            }
        }
        //workbook.close();
        this.closeStuff();
    }
    private void closeStuff() throws IOException {
        inputStreamFile.close();
        workbook.close();
    }

    private String getTImeOfLesson(Sheet sheet, int rowIndex) {
        String cellTime = "Error in finding time";
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            // Check the B column of the given row for the time value
            Cell timeCell = row.getCell(1);
            if (timeCell != null && !Objects.equals(timeCell.getStringCellValue(), "")) {
                cellTime = timeCell.getRichStringCellValue().getString().trim();
            } else {
                // Check for merged regions in the row that contain the input cell
                int index = sheet.getNumMergedRegions();
                for (int i = 0; i < index; i++) {
                    CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
                    if (mergedRegion.isInRange(rowIndex, 1)) {
                        for (int j = mergedRegion.getFirstRow(); j <= mergedRegion.getLastRow(); j++) {
                            Row regionRow = sheet.getRow(j);
                            if (regionRow != null) {
                                Cell regionCell = regionRow.getCell(1);
                                if (regionCell != null && regionCell.getRichStringCellValue() != null) {
                                    cellTime = regionCell.getRichStringCellValue().getString().trim();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return cellTime;
    }



    private String getDayOfLesson(Sheet sheet, Row row, Cell cell){
        String cellday1 = null;
        int index = sheet.getNumMergedRegions();

        for(int i = 0; i < index; i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(cell.getRowIndex(), row.getFirstCellNum())) {
                for (int j = mergedRegion.getFirstRow(); j <= mergedRegion.getLastRow(); j++) {
                    Row regionRow = sheet.getRow(j);
                    for (int k = mergedRegion.getFirstColumn(); k <= mergedRegion.getLastColumn(); k++) {
                        Cell regionCell = regionRow.getCell(k);
                        String regionCellValue = regionCell.getRichStringCellValue().getString().trim();
                        if(!regionCellValue.equals("")){
                            cellday1 = regionCell.getRichStringCellValue().getString().trim();
                        }
                    }
                }
            }
        }
        return cellday1;
    }

    private String getInstituteNames(Sheet sheet, Cell cell){
        String instituteName = null;

        int index3 = sheet.getNumMergedRegions();
        for(int i = 0; i < index3; i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(0, cell.getColumnIndex()) || mergedRegion.isInRange(1, cell.getColumnIndex())) {
                for (int j = mergedRegion.getFirstRow(); j <= mergedRegion.getLastRow(); j++) {
                    Row regionRow = sheet.getRow(j);
                    for (int k = mergedRegion.getFirstColumn(); k <= mergedRegion.getLastColumn(); k++) {
                        Cell regionCell = regionRow.getCell(k);
                        String regionCellValue = regionCell.getRichStringCellValue().getString().trim();
                        if(!regionCellValue.equals("")){
                            instituteName = regionCell.getRichStringCellValue().getString().trim();
                        }
                    }
                }
            }
        }
        return instituteName;
    }
    private String[] getGroupNameAndFullName(Sheet sheet, Cell cell){
        String groupName = null;
        String groupNameFull = null;
        String[] groupNames = new String[2];

        int index1 = sheet.getNumMergedRegions();
        for (int i = 0; i < index1; i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(2, cell.getColumnIndex())) {
                Row groupRow = sheet.getRow(2);
                for (int j = mergedRegion.getFirstColumn(); j <= mergedRegion.getLastColumn(); j++) {
                    Cell groupCell = groupRow.getCell(j);
                    String groupCellValue = groupCell.getRichStringCellValue().getString().trim();
                    if (!groupCellValue.equals("")) {
                        groupName = groupCell.getRichStringCellValue().getString().trim();
                    }
                }
                Row groupRow1 = sheet.getRow(3);
                for (int j = mergedRegion.getFirstColumn(); j <= mergedRegion.getLastColumn(); j++) {
                    Cell groupCell = groupRow1.getCell(j);
                    String groupCellValue = groupCell.getRichStringCellValue().getString().trim();
                    if (!groupCellValue.equals("")) {
                        groupNameFull = groupCell.getRichStringCellValue().getString().trim();
                    }
                }
            }
        }
        groupNames[0] = groupName;
        groupNames[1] = groupNameFull;
        return groupNames;
    }

    public ArrayList<Lesson> getLessonList(){

        return this.lessonArrayList;
    }
}
