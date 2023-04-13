package com.risonna.schedulewebapp.excelparsing;


import com.risonna.schedulewebapp.beans.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Map<String, CellRangeAddress> groupMergedCells = new HashMap<>();
            for (int colIndex = 2; colIndex < sheet.getRow(2).getLastCellNum(); colIndex += 1) {
                Cell cell = sheet.getRow(2).getCell(colIndex);
                if(!cell.getStringCellValue().trim().equals("")){
                    String groupName = cell.getStringCellValue().trim();
                    CellRangeAddress mergedCell = getMergedCellRanges(sheet, cell.getRowIndex(), colIndex);
                    if(mergedCell != null){
                        groupMergedCells.put(groupName, mergedCell);
                    }
                }
            }
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getRichStringCellValue().getString().trim();
                        if (!cellValue.equals("")) {
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
                                            if (subjectIndex >= 0) { // add this check
                                                String betweenText = cellValue.substring(subjectIndex, teacherIndex).trim();
                                                lesson.setSubjectName(betweenText);
                                            } else {
                                                lesson.setSubjectName("Error in cellValue.substring() in ExcelSearch.parseStuff()");
                                            }
                                            lesson.setSubjectId(subj.getId());
                                        }
                                    }
                                    if (!isSubjectThere) {
                                        lesson.setSubjectName("Subject isn't specified or not found");
                                    }


                                    Pattern pattern = Pattern.compile("(?i)" + lesson.getTeacherName() + "\\s*[.,]+\\s*");
                                    Matcher matcher = pattern.matcher(cellValue);

                                    String teacherNameMatched;
                                    if (matcher.find()) {
                                        teacherNameMatched = matcher.group();
                                        // Do something with the teacher name
                                    } else {
                                        // No teacher name found
                                        teacherNameMatched = "";
                                    }

                                    String cabNumber = "unknown";
                                    if (!teacherNameMatched.isEmpty()) {
                                        if (cellValue.contains("ауд")) {
                                            int index = cellValue.indexOf("ауд") + 4;
                                            cabNumber = cellValue.substring(index);
                                        } else {
                                            int startIndex = cellValue.indexOf(teacherNameMatched) + teacherNameMatched.length();
                                            if (startIndex < cellValue.length()) {
                                                cabNumber = cellValue.substring(startIndex).trim();
                                            }
                                        }
                                    }


                                    lesson.setCabinetName(cabNumber);
                                    lesson.setCabinetId(0);
                                    for (Cabinet cabinet : cabinetList) {
                                        if (cabNumber.equals(cabinet.getCabinetName())) {
                                            lesson.setCabinetId(cabinet.getId());
                                            break;
                                        }
                                    }


                                    String weekDay;
                                    weekDay = checkWeek(cell);
                                    lesson.setLessonWeek(weekDay);

                                    String cellTime;


                                    cellTime = this.getTImeOfLesson(sheet, cell.getRowIndex());

                                    //Print the time of thr cell
                                    lesson.setLessonTime(cellTime);


                                    String cellDay; //Variable for lesson's day

                                    cellDay = this.getDayOfLesson(sheet, row, cell);

                                    //Print lesson's day
                                    lesson.setLessonDay(cellDay);


                                    // Print full and short names of a group
                                    lesson.setGroupName(getGroupName(sheet, cell));


                                    String instituteName; // Variable for institute name

                                    instituteName = this.getInstituteNames(sheet, cell);

                                    // Print institute name
                                    lesson.setInstituteName(instituteName);

                                    lesson.setGroupId(0);
                                    for (Group group : groupList) {
                                        if (lesson.getGroupName().equalsIgnoreCase(group.getGroupName())) {
                                            lesson.setGroupId(group.getId());
                                        }

                                    }
                                    lesson.setCellId(cell.getAddress().toString());


                                    List<CellRangeAddress> groupRegions = new ArrayList<>();
                                    CellRangeAddress lessonCell = null;
                                    for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
                                        // Check if merged region intersects with the row of the groups
                                        if (mergedRegion.containsRow(2) && (mergedRegion.getFirstColumn() >= 2)) {
                                            groupRegions.add(mergedRegion);
                                        }

                                        // Check if merged region intersects with the column of the lesson
                                        if (mergedRegion.containsColumn(cell.getColumnIndex()) && mergedRegion.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                                            lessonCell = mergedRegion;
                                        }
                                    }



                                    boolean potochLesson = false;
                                    int groupsForLesson = 0;

                                    //if lessonCell isn't null we treat it as a merged cell
                                    if (lessonCell != null) {
                                        lesson.setLessonCell(true);
                                        int lessonFirstColumn = lessonCell.getFirstColumn();
                                        int lessonLastColumn = lessonCell.getLastColumn();
                                        if (!groupRegions.isEmpty()) {
                                            for (CellRangeAddress groupRegion : groupRegions) {
                                                int groupFirstColumn = groupRegion.getFirstColumn();
                                                int groupLastColumn = groupRegion.getLastColumn();
                                                if (lessonFirstColumn <= groupLastColumn && lessonLastColumn >= groupFirstColumn) {
                                                    groupsForLesson++;
                                                }
                                            }

                                        }
                                        else{
                                            List<Cell> groupCells = new ArrayList<>();
                                            for (int i = 2; i < sheet.getRow(2).getLastCellNum(); i++) {
                                                Cell groupCell = sheet.getRow(2).getCell(i);
                                                if (groupCell != null) {
                                                    groupCells.add(groupCell);
                                                }
                                            }
                                            for (Cell groupCell : groupCells) {
                                                int groupColumnIndex = groupCell.getColumnIndex();
                                                if (lessonFirstColumn <= groupColumnIndex && lessonLastColumn >= groupColumnIndex) {
                                                    groupsForLesson++;
                                                }
                                            }
                                        }
                                    }
                                    //otherwise just treat as normal cell
                                    else {
                                        lesson.setLessonCell(false);
                                        groupsForLesson = 1;
                                    }
                                    if (groupsForLesson >= 2) potochLesson = true;
                                    lesson.setGroupsForLesson(groupsForLesson);
                                    lesson.setPotochLesson(potochLesson);


                                    if (potochLesson) {
                                        for (Map.Entry<String, CellRangeAddress> entry : groupMergedCells.entrySet()) {
                                            String groupName = entry.getKey();
                                            CellRangeAddress mergedCell = entry.getValue();
                                            if (!groupName.equalsIgnoreCase(lesson.getGroupName()) && lessonCell != null) {
                                                int firstRow = mergedCell.getFirstRow();
                                                int lastRow = mergedCell.getLastRow();
                                                int firstCol = mergedCell.getFirstColumn();
                                                int lastCol = mergedCell.getLastColumn();
                                                if (lessonCell.getFirstColumn() <= lastCol && lessonCell.getLastColumn() >= firstCol) {
                                                    Lesson lessonPotoch = new Lesson();
                                                    lessonPotoch.setLessonTime(cellTime);
                                                    lessonPotoch.setLessonDay(cellDay);
                                                    lessonPotoch.setLessonWeek(weekDay);
                                                    lessonPotoch.setSubjectName(lesson.getSubjectName());
                                                    lessonPotoch.setSubjectId(lesson.getSubjectId());
                                                    lessonPotoch.setTeacherName(teacher.getTeacherName());
                                                    lessonPotoch.setTeacherId(teacher.getId());
                                                    lessonPotoch.setInstituteName(instituteName);
                                                    lessonPotoch.setCabinetName(lesson.getCabinetName());
                                                    lessonPotoch.setGroupName(groupName);
                                                    lessonPotoch.setGroupsForLesson(groupsForLesson);
                                                    lessonPotoch.setLessonCell(true);
                                                    lessonPotoch.setPotochLesson(potochLesson);
                                                    boolean found = false;
                                                    for (Group group : groupList) {
                                                        if (lessonPotoch.getGroupName().equalsIgnoreCase(group.getGroupName())) {
                                                            lessonPotoch.setGroupId(group.getId());
                                                            found = true;
                                                        }

                                                    }
                                                    if (!found) {
                                                        lessonPotoch.setGroupId(0);
                                                    }
                                                    lessonArrayList.add(lessonPotoch);


                                                }
                                            }
                                        }
                                    }
                                    lessonArrayList.add(lesson);


                                }
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
    private String getGroupName(Sheet sheet, Cell cell) {
        String groupName = null;
        int numMergedRegions = sheet.getNumMergedRegions();
        boolean foundMergedRegion = false;
        // Search for merged region that contains the current cell in row 3
        for (int i = 0; i < numMergedRegions; i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(2, cell.getColumnIndex())) {
                foundMergedRegion = true;
                Row groupRow = sheet.getRow(2);
                if (groupRow != null) {
                    Cell groupCell = groupRow.getCell(mergedRegion.getFirstColumn());
                    if (groupCell != null) {
                        String groupCellValue = groupCell.getRichStringCellValue().getString().trim();
                        if (!groupCellValue.isEmpty()) {
                            groupName = groupCellValue;
                            break;
                        }
                    }
                }
            }
        }

        if(!foundMergedRegion){
            Row groupRow = sheet.getRow(2);
            if(groupRow != null){
                Cell groupCell = groupRow.getCell(cell.getColumnIndex());
                if(groupCell != null){
                    String groupCellValue = groupCell.getRichStringCellValue().getString().trim();
                    if(!groupCellValue.isEmpty()){
                        groupName = groupCellValue;
                    }
                }
            }
        }
        // If no groupName and groupNameFull were found, assign "unknown"
        if (groupName == null) {
            groupName = "unknown";
        }

        return groupName;
    }

    private String checkWeek(Cell cell){

        String week;
        String lessonText = cell.getRichStringCellValue().getString().trim().toLowerCase();
        if (lessonText.startsWith("неч")) {
            week = "неч";
        } else if (lessonText.startsWith("чет")) {
            week = "чет";

        } else {
            // If nothing is specified, assume the lesson is on every week
            week = "любая";
        }
        return week;
    }

    private CellRangeAddress getMergedCellRanges(Sheet sheet, int row, int colIndex) {
        CellRangeAddress mergedCellRanges;
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.containsRow(row) && mergedRegion.containsColumn(colIndex)) {
                return mergedRegion;
            }
        }
        return null;
    }






    public ArrayList<Lesson> getLessonList(){

        return this.lessonArrayList;
    }
}
