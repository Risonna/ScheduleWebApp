package com.risonna.schedulewebapp.excelparsing;


import com.risonna.schedulewebapp.hibernate.entity.Subject;
import com.risonna.schedulewebapp.hibernate.entity.Group;
import com.risonna.schedulewebapp.hibernate.entity.Teacher;
import com.risonna.schedulewebapp.hibernate.entity.Cabinet;
import com.risonna.schedulewebapp.hibernate.entity.Lesson;
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

        teacherNames = namesOfTeachers;
        subjects = subjectList;
        this.cabinetList = cabinetList;
        this.groupList = groupList;


//        if (inputStreamFile.toString().endsWith(".xls")) {
//            workbook = new HSSFWorkbook();
//        } else {
//            workbook = new XSSFWorkbook();
//        }
        inputStreamFile = new FileInputStream(pathString);
        workbook = WorkbookFactory.create(inputStreamFile);
    }


    public void parseStuff() throws IOException {
        for (Sheet sheet: workbook) {
            Map<String, CellRangeAddress> groupMergedCells = new HashMap<>();
            for (int colIndex = 2; colIndex < sheet.getRow(2).getLastCellNum(); colIndex += 1) {
                Cell cell = sheet.getRow(2).getCell(colIndex);
                if (cell != null) {


                    if (!cell.getStringCellValue().trim().equals("")) {
                        String groupName = cell.getStringCellValue().trim();
                        CellRangeAddress mergedCell = getMergedCellRanges(sheet, cell.getRowIndex(), colIndex);
                        if (mergedCell != null) {
                            groupMergedCells.put(groupName, mergedCell);
                        }
                    }
                }
            }
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getRichStringCellValue().getString().trim();
                        if (!cellValue.equals("")) {
                            List<Integer> cabinetPositions = new ArrayList<>();

                            int cabAmount = 0;
                            for (Cabinet cabinet : cabinetList) {
                                String processedCabinetName = cabinet.getCabinetName().trim().toLowerCase().replaceAll("\\s",
                                        "");
                                String processedCellValue = cellValue.toLowerCase().replaceAll("\\s", "");

                                if (processedCellValue.contains(processedCabinetName)) {
                                    int certainCabAmount = (processedCellValue.length()-processedCellValue.replace(processedCabinetName,
                                            "").length())/processedCabinetName.length();
                                    cabAmount += certainCabAmount;

                                    int startIndex = 0;
                                    for(int i = 0; i < certainCabAmount; i++) {
                                        int cabinetPosition = processedCellValue.indexOf(processedCabinetName, startIndex)+
                                                processedCabinetName.length();

                                        if (cabinetPosition != -1) {
                                            cabinetPositions.add(cabinetPosition);
                                            startIndex = cabinetPosition + 1;
                                        }
                                    }

                                }
                            }
                            // bool variable to determine whether a cell contains multiple lessons  or not(for multiple lessons we
                            // will take first lesson's week and set its value for all lessons in a cell)
                            boolean isMultiple = false;

                            if(cabAmount <2){
                                int teachAmount = 0;
                                for (Teacher teacher : teacherNames) {
                                    String processedCellValue = cellValue.toLowerCase().replaceAll("\\s", "");
                                    String processedTeacherName = getProcessedTeacherName(teacher).trim().toLowerCase().replaceAll("\\s", "");
                                    if (processedCellValue.contains(processedTeacherName)) {
                                        teachAmount += (processedCellValue.length()-processedCellValue.replace(processedTeacherName,
                                                "").length())/processedTeacherName.length();

                                    }
                                    if(teachAmount>1)break;
                                }

                                if(teachAmount<2){
                                    if(cabAmount >0 || teachAmount >0){
                                        actualParsing(cellValue, sheet, cell, row, groupMergedCells, isMultiple);
                                    }
                                }
                            }
                            else {
                                String processedCellValue = cellValue.toLowerCase().replaceAll("\\s", "");
                                isMultiple = true;
                                // If there are multiple cabinet names in the cell, split the cell into multiple lessons
                                Collections.sort(cabinetPositions);
                                int start = 0;
                                for (int i = 0; i < cabinetPositions.size()-1; i++) {
                                        int end = cabinetPositions.get(i);
                                        String lesson = processedCellValue.substring(start, end).trim();
                                        actualParsing(lesson, sheet, cell, row, groupMergedCells, isMultiple);
                                        start = end;
                                }
                                String lastLesson = processedCellValue.substring(start);
                                System.out.println("multiple lesson is :" + lastLesson);
                                actualParsing(lastLesson, sheet, cell, row, groupMergedCells, isMultiple);
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
    private String getDayOfLesson(Sheet sheet, Cell cell){
        String cellday1 = null;
        int index = sheet.getNumMergedRegions();

        for(int i = 0; i < index; i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(cell.getRowIndex(), cell.getRow().getFirstCellNum())) {
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

    //get group column if a group's cell is a single cell(not merged cell)
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

    //check whether a group is a merged cell or just a cell
    private boolean isGroupMerged(Sheet sheet, Cell cell){
        int numMergedRegions = sheet.getNumMergedRegions();
        boolean foundMergedRegion = false;
        // Search for merged region that contains the current cell in row 3
        for (int i = 0; i < numMergedRegions; i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(2, cell.getColumnIndex())) {
                foundMergedRegion = true;
                }
            }

        return foundMergedRegion;
    }
    private int getGroupColumn(Sheet sheet, Cell cell){
        int groupCol = -1;
        Row groupRow = sheet.getRow(2);
        if(groupRow != null){
            Cell groupCell = groupRow.getCell(cell.getColumnIndex());
            if(groupCell != null){
                groupCol = groupCell.getColumnIndex();
            }
        }

        return groupCol;
    }
    private String checkWeek(String lessonNameString){

        String week;
        if (lessonNameString.startsWith("неч")) {
            week = "неч";
        } else if (lessonNameString.startsWith("чет")) {
            week = "чет";

        } else {
            // If nothing is specified, assume the lesson is on every week
            week = "";
        }
        return week;
    }
    private String getProcessedTeacherName(Teacher teacher){
        if(!teacher.getTeacherSurname().equalsIgnoreCase("unknown")){
            return getTeacherNameIfLastNameIsKnown(teacher);
        }
        else{
            return teacher.getTeacherName();
        }

    }
    private String getTeacherNameIfLastNameIsKnown(Teacher teacher){
        return teacher.getTeacherSurname()+" "+teacher.getTeacherName().charAt(0)+"."+teacher.getTeacherPatronymic().charAt(0)+".";
    }
    private String getCabinetName(String teacherName, String lessonString){
        Pattern pattern = Pattern.compile("(?i)" + teacherName + "\\s*[.,]+\\s*");
        Matcher matcher = pattern.matcher(lessonString);

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
            if (lessonString.contains("ауд")) {
                int index = lessonString.indexOf("ауд") + 4;
                cabNumber = lessonString.substring(index);
            } else {
                int startIndex = lessonString.indexOf(teacherNameMatched) + teacherNameMatched.length();
                if (startIndex < lessonString.length()) {
                    cabNumber = lessonString.substring(startIndex).trim();
                }
            }
        }

        if (cabNumber.toLowerCase().contains(" c ")) {
            cabNumber = cabNumber.substring(1);
        }

        return cabNumber;
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

    private void actualParsing(String cellValue, Sheet sheet, Cell cell, Row row, Map<String, CellRangeAddress> groupMergedCells, boolean isMultiple){
       String processedCellValue = cellValue.toLowerCase().replaceAll("\\s", "");
        Lesson lesson = new Lesson();
        lesson.setTeacherName("unknown");
        lesson.setTeacherId(1);
        for (Teacher teacher : teacherNames) {
            String regex;
            String checking;
            if(!teacher.getTeacherSurname().equalsIgnoreCase("unknown")){
                String surPatrTeacherName = getTeacherNameIfLastNameIsKnown(teacher);
                regex = "(?i)\\b" + surPatrTeacherName + "\\b";
                checking = surPatrTeacherName;
            }
            else{
                // remove dots and spaces from teacher name
                String cleanedTeacherName = teacher.getTeacherName().replaceAll("\\.", "");
                checking = cleanedTeacherName;
                // create a regex pattern for the teacher name
                regex = "(?i)\\b" + cleanedTeacherName + "\\b";
            }



            Pattern pattern = Pattern.compile(regex.trim().replaceAll("\\.", "").replaceAll("\\s", ""));
            String processedChecking = checking.trim().toLowerCase().replaceAll("\\s",
                    "");
            if (processedCellValue.contains(processedChecking) ||
            processedCellValue.replaceAll("\\.", "").replaceAll("\\s",
                    "").contains(processedChecking.replaceAll("\\.", "").replaceAll("\\s", "")) ||
                    pattern.matcher(processedCellValue.replaceAll("\\.",
                    "").replaceAll("\\s", "")).find()) {


                lesson.setTeacherId(teacher.getId());
                lesson.setTeacherName(checking);
            }
        }
        

        lesson.setLessonType("");
        if(processedCellValue.contains("(л)") || processedCellValue.contains("(пр)") || processedCellValue.contains("(лаб)")){
            if(processedCellValue.contains("(л)"))lesson.setLessonType("(л)");
            if(processedCellValue.contains("(пр)"))lesson.setLessonType("(пр)");
            if(processedCellValue.contains("(лаб)"))lesson.setLessonType("(лаб)");
        }

        lesson.setCabinetName("unknown");
        lesson.setCabinetId(1);
        for (Cabinet cabinet : cabinetList) {
            String processedCabinetName = cabinet.getCabinetName().trim().toLowerCase().replaceAll("\\s", "");
            if (processedCellValue.contains(processedCabinetName)) {
                lesson.setCabinetId(cabinet.getId());
                lesson.setCabinetName(cabinet.getCabinetName());
                break;
            }
        }

        String weekDay;
        if(!isMultiple) {
            weekDay = checkWeek(cellValue.toLowerCase());
        }
        else{
            weekDay = checkWeek(cell.getStringCellValue().trim().toLowerCase());
        }
        lesson.setLessonWeek(weekDay);
        lesson.setMultipleLessonsInOneCell(isMultiple);

        boolean isSubjectThere = false;
        String subjectname = getSubjectName(cellValue);
        if(!subjectname.equals("nope")) {
            for (Subject subj : subjects) {
                String processedSubject = subj.getSubjectName().trim().toLowerCase().replaceAll("\\s", "");
                if (subjectname.toLowerCase().replaceAll("\\s", "").equals(processedSubject)) {
                    lesson.setSubjectId(subj.getId());
                    lesson.setSubjectName(subj.getSubjectName());
                    isSubjectThere = true;
                }
            }
            if (!isSubjectThere) {
                lesson.setSubjectName("Subject isn't specified or not found");
                lesson.setSubjectId(1);
            }
        }
        else{
            lesson.setSubjectName("Couldn't parse the subjectName");
            lesson.setSubjectId(1);
        }

        String fromWeekToWeek;
        fromWeekToWeek = getFromWeekToWeek(cellValue);

        //unjoin the c n-m нед
        if(!fromWeekToWeek.equals("")){
            String regexweeknums = "\\d+(?:-\\d+)?";
            Pattern patternnums = Pattern.compile(regexweeknums);
            Matcher matchernums = patternnums.matcher(fromWeekToWeek);
            String firstLetterRegex = "^(со?)\\s*\\d+(?:-\\d+)?\\s*нед\\.?";
            String lastLetterRegex = "со?\\s*\\d+(?:-\\d+)?\\s*(нед\\.?)?$";
            Pattern firstLetterPattern = Pattern.compile(firstLetterRegex);
            Matcher firstLetterMatcher = firstLetterPattern.matcher(fromWeekToWeek);
            Pattern lastLetterPattern = Pattern.compile(lastLetterRegex);
            Matcher lastLetterMatcher = lastLetterPattern.matcher(fromWeekToWeek);
            if (matchernums.find() && firstLetterMatcher.find() && lastLetterMatcher.find()) {
                String numbers = matchernums.group();
                String firstLetter = firstLetterMatcher.group(1);
                String lastLetter = lastLetterMatcher.group(1);
                lesson.setFromWeekToWeek(firstLetter + " " + numbers + " " + lastLetter);
            } else {
                System.out.println("No match found.");
                lesson.setFromWeekToWeek("nomatch");
            }
        }
        else{
            lesson.setFromWeekToWeek("");
        }


        String cellTime;
        cellTime = this.getTImeOfLesson(sheet, cell.getRowIndex());
        //Print the time of thr cell
        lesson.setLessonTime(cellTime);


        String cellDay; //Variable for lesson's day
        cellDay = this.getDayOfLesson(sheet, cell);
        //Print lesson's day
        lesson.setLessonDay(cellDay);


        // Print full and short names of a group
        lesson.setGroupName(getGroupName(sheet, cell));


        String instituteName; // Variable for institute name
        instituteName = this.getInstituteNames(sheet, cell);
        // Print institute name
        lesson.setInstituteName(instituteName);

        lesson.setGroupId(1);


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
            //set lesson's rows and columns from excel
            lesson.setColFirst(lessonCell.getFirstColumn());
            lesson.setColLast(lessonCell.getLastColumn());
            lesson.setRowFirst(lessonCell.getFirstRow());
            lesson.setRowLast(lessonCell.getLastRow());

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
            //set lesson's rows and columns from excel
            lesson.setColFirst(cell.getColumnIndex());
            lesson.setColLast(cell.getColumnIndex());
            lesson.setRowFirst(cell.getRowIndex());
            lesson.setRowLast(cell.getRowIndex());



            lesson.setLessonCell(false);
            groupsForLesson = 1;
        }
        if (groupsForLesson >= 2) potochLesson = true;
        lesson.setGroupsForLesson(groupsForLesson);
        lesson.setPotochLesson(potochLesson);




        if(isByChoice(processedCellValue))lesson.setByChoice("(по выбору)");
        else lesson.setByChoice("");

        if (potochLesson) {
            lesson.setPotochLesson(true);
            for (Map.Entry<String, CellRangeAddress> entry : groupMergedCells.entrySet()) {
                String groupName = entry.getKey();
                CellRangeAddress mergedCell = entry.getValue();
                //if lesson's group name is not from an entry, which means that the entry group isn't straight above the lesson(case for potoch lessons)
                //then there is a check if lesson's borders(first/last columns) join the group's borders
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
                        lessonPotoch.setTeacherName(lesson.getTeacherName());
                        lessonPotoch.setTeacherId(lesson.getTeacherId());
                        lessonPotoch.setInstituteName(instituteName);
                        lessonPotoch.setCabinetName(lesson.getCabinetName());
                        lessonPotoch.setCabinetId(lesson.getCabinetId());
                        lessonPotoch.setGroupName(groupName);
                        lessonPotoch.setGroupsForLesson(groupsForLesson);
                        lessonPotoch.setMultipleLessonsInOneCell(lesson.isMultipleLessonsInOneCell());
                        lessonPotoch.setLessonCell(true);
                        lessonPotoch.setPotochLesson(true);
                        lessonPotoch.setRowFirst(lessonCell.getFirstRow());
                        lessonPotoch.setRowLast(lessonCell.getLastRow());
                        lessonPotoch.setColFirst(lessonCell.getFirstColumn());
                        lessonPotoch.setColLast(lessonCell.getLastColumn());
                        lessonPotoch.setGroupColFirst(firstCol);
                        lessonPotoch.setGroupColLast(lastCol);
                        lessonPotoch.setFromWeekToWeek(fromWeekToWeek);
                        lessonPotoch.setLessonType(lesson.getLessonType());
                        lessonPotoch.setByChoice(lesson.getByChoice());

                        if(lessonCell.getFirstColumn() <= firstCol) {
                            lessonPotoch.setColFirst(firstCol);

                            if(lessonCell.getLastColumn() >= lastCol){
                                lessonPotoch.setColLast(lastCol);
                            }
                            else{
                                lessonPotoch.setColLast(firstCol);
                            }
                        }

                        if(lessonCell.getFirstColumn() == lastCol){
                            lessonPotoch.setColFirst(lastCol);
                            lessonPotoch.setColLast(lastCol);
                        }

                        if(lessonCell.getLastColumn() == firstCol){
                            lessonPotoch.setColFirst(firstCol);
                            lessonPotoch.setColLast(firstCol);
                        }

                        lesson.setPotochLesson(true);

                        lessonPotoch.setForWholeGroup(lessonPotoch.getColFirst()<= firstCol && lessonPotoch.getColLast() >= lastCol);


                        boolean found = false;
                        for (Group group : groupList) {
                            if (lessonPotoch.getGroupName().equalsIgnoreCase(group.getGroupName())) {
                                lessonPotoch.setGroupId(group.getId());
                                found = true;
                            }

                        }
                        if (!found) {
                            lessonPotoch.setGroupId(1);
                        }
                        lessonArrayList.add(lessonPotoch);


                    }
                }
                else{
                    int firstRow = mergedCell.getFirstRow();
                    int lastRow = mergedCell.getLastRow();
                    int firstCol = mergedCell.getFirstColumn();
                    int lastCol = mergedCell.getLastColumn();

                    lesson.setForWholeGroup(lesson.getColFirst()<= firstCol && lesson.getColLast() >= lastCol);

                    lesson.setGroupColFirst(firstCol);
                    lesson.setGroupColLast(lastCol);

                    if(lessonCell.getFirstColumn() <= firstCol) {
                        lesson.setColFirst(firstCol);

                        if(lessonCell.getLastColumn() >= lastCol){
                            lesson.setColLast(lastCol);
                        }
                        else{
                            lesson.setColLast(firstCol);
                        }
                    }

                    if(lessonCell.getFirstColumn() == lastCol){
                        lesson.setColFirst(lastCol);
                        lesson.setColLast(lastCol);
                    }

                    if(lessonCell.getLastColumn() == firstCol){
                        lesson.setColFirst(firstCol);
                        lesson.setColLast(firstCol);
                    }
                }

            }
        }
        //if not potochLesson, then the program will just assign this lesson its forWholeGroup bool variable
        else{
            lesson.setPotochLesson(false);
            if(isGroupMerged(sheet, cell)) {
                for (Map.Entry<String, CellRangeAddress> entry : groupMergedCells.entrySet()) {
                    String groupName = entry.getKey();
                    CellRangeAddress mergedCell = entry.getValue();
                    //if lesson's group name is not from an entry, which means that the entry group isn't straight above the lesson(case for potoch lessons)
                    //then there is a check if lesson's borders(first/last columns) join the group's borders
                    if (groupName.equalsIgnoreCase(lesson.getGroupName())) {
                        int firstRow = mergedCell.getFirstRow();
                        int lastRow = mergedCell.getLastRow();
                        int firstCol = mergedCell.getFirstColumn();
                        int lastCol = mergedCell.getLastColumn();
                        lesson.setForWholeGroup(lesson.getColFirst() <= firstCol && lesson.getColLast() >= lastCol);
                        lesson.setGroupColFirst(firstCol);
                        lesson.setGroupColLast(lastCol);
                    }
                }
            }
            else{
                lesson.setForWholeGroup(lesson.getColFirst() == getGroupColumn(sheet, cell) && lesson.getColLast() == getGroupColumn(sheet, cell));
                lesson.setGroupColFirst(getGroupColumn(sheet, cell));
                lesson.setGroupColLast(getGroupColumn(sheet, cell));
            }

        }

        if (lesson != null&& lesson.getLessonTime() != null && lesson.getLessonDay() != null &&
                !(lesson.getLessonDay().equalsIgnoreCase("день") ||
                        lesson.getLessonTime().equalsIgnoreCase("время") ||
                        lesson.getLessonDay().equalsIgnoreCase("") ||
                        lesson.getLessonTime().equalsIgnoreCase(""))) {
            lessonArrayList.add(lesson);
        }

    }

    private String getFromWeekToWeek(String cellValue) {
        String regex = "со?\\s*(\\d+)(?:-(\\d+))?\\s*нед\\.?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cellValue);

        if(matcher.find()){
            System.out.println("Week cell is " + cellValue + " matcher is " + matcher.group());
            return matcher.group();
        }
        else{
            System.out.println("Week cell is " + cellValue + " and no matcher");
            return "";
        }
    }

    private boolean isByChoice(String processedValue){
        return processedValue.contains("повыбору");
    }
    private String getSubjectName(String lesson) {
        String weekDay = checkWeek(lesson);
        boolean weekDayOk = weekDay.length() > 1;
        String lessonProcessed = lesson.toLowerCase().replaceAll("\\s", "");
        String typeOfLesson = "none";
        boolean isThereType = false;
        int typeOfLessonInt = 0;
        String lessonString = "nope";
        int byChoiceInt;
        if(lessonProcessed.contains("(пр)")){
            typeOfLesson = "(пр)";
            typeOfLessonInt = lessonProcessed.indexOf("(пр)");
            isThereType = true;
        }
        if (lessonProcessed.contains("(л")) {
            typeOfLesson = "(л)";
            typeOfLessonInt = lessonProcessed.indexOf("(л)");
            isThereType = true;
        }
        if (lessonProcessed.contains("(лаб")) {
            typeOfLesson = "(лаб)";
            typeOfLessonInt = lessonProcessed.indexOf("(лаб)");
            isThereType = true;

        }
        if(!lessonProcessed.contains("нед.")){
            if (weekDayOk) {
                if (!lessonProcessed.contains("повыбору") && typeOfLessonInt != 0 && typeOfLessonInt != -1) {
                    lessonString = lesson.toLowerCase().replaceAll("\\s", "").substring(lesson.indexOf(weekDay) + weekDay.length() + 1, typeOfLessonInt);
                }
                if (lessonProcessed.contains("повыбору")) {
                    lessonString = lesson.toLowerCase().replaceAll("\\s", "").substring(lesson.indexOf(weekDay) + weekDay.length() + 1, lessonProcessed.indexOf("(повыбору"));
                }
            }
            else{
                if (!lessonProcessed.contains("повыбору") && typeOfLessonInt != 0 && typeOfLessonInt != -1) {
                    lessonString = lesson.toLowerCase().replaceAll("\\s", "").substring(0, typeOfLessonInt);
                }
                if(typeOfLessonInt == 0 || typeOfLessonInt == -1) {
                    String teachTitle = "none";
                    if (lessonProcessed.contains("доц.")) teachTitle = "доц.";
                    if (lessonProcessed.contains("асс.")) teachTitle = "асс.";
                    if (lessonProcessed.contains("проф.")) teachTitle = "проф.";
                    if (lessonProcessed.contains("ст.пр.")) teachTitle = "ст.пр.";
                    if (lessonProcessed.contains("работодатель")) teachTitle = "работодатель";

                    if (!teachTitle.equals("none")) {
                        int teachTitleInt = lessonProcessed.indexOf(teachTitle);
                        if (!lessonProcessed.contains("нед.")) {
                                lessonString = lesson.toLowerCase().replaceAll("\\s", "").substring(0, teachTitleInt);


                        } else {
                            lessonString = lessonProcessed.substring(lessonProcessed.indexOf("нед.") + "нед.".length(), teachTitleInt);
                        }
                    }
                }
                if (lessonProcessed.contains("повыбору")) {
                    lessonString = lesson.toLowerCase().replaceAll("\\s", "").substring(0, lessonProcessed.indexOf("(повыбору"));
                }
            }

        }
        else{
            if (!lessonProcessed.contains("повыбору") && typeOfLessonInt != 0 && typeOfLessonInt != -1) {
                lessonString = lessonProcessed.substring(lessonProcessed.indexOf("нед.") + "нед.".length(), typeOfLessonInt);
            }
            if(typeOfLessonInt == 0 || typeOfLessonInt == -1) {
                String teachTitle = "none";
                if (lessonProcessed.contains("доц.")) teachTitle = "доц.";
                if (lessonProcessed.contains("асс.")) teachTitle = "асс.";
                if (lessonProcessed.contains("проф.")) teachTitle = "проф.";
                if (lessonProcessed.contains("ст.пр.")) teachTitle = "ст.пр.";
                if (lessonProcessed.contains("работодатель")) teachTitle = "работодатель";

                if (!teachTitle.equals("none")) {
                    int teachTitleInt = lessonProcessed.indexOf(teachTitle);
                    if (!lessonProcessed.contains("нед.")) {
                        if (weekDayOk) {
                            lessonString = lesson.toLowerCase().replaceAll("\\s", "").substring(lesson.indexOf(weekDay) + weekDay.length() + 1, teachTitleInt);
                        }
                        else {
                            lessonString = lesson.toLowerCase().replaceAll("\\s", "").substring(0, teachTitleInt);
                        }

                    } else {
                        lessonString = lessonProcessed.substring(lessonProcessed.indexOf("нед.") + "нед.".length(), teachTitleInt);
                    }
                }
            }
            if (lessonProcessed.contains("повыбору")) {
                lessonString = lessonProcessed.substring(lessonProcessed.indexOf("нед.") + "нед.".length(), lessonProcessed.indexOf("(повыбору"));
            }
        }


        return lessonString;

    }


    public ArrayList<Lesson> getLessonList(){

        return this.lessonArrayList;
    }
}
