package com.risonna.schedulewebapp.database;

import com.risonna.schedulewebapp.hibernate.HibernateUtil;
import com.risonna.schedulewebapp.hibernate.entity.*;
import jakarta.inject.Named;
import jakarta.persistence.criteria.CriteriaDelete;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Named
public class DataHelper {

    private SessionFactory sessionFactory = null;
    private static DataHelper dataHelper;
    CriteriaBuilder builder = null;

    private DataHelper() {

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static DataHelper getInstance() {
        if (dataHelper == null) {
            dataHelper = new DataHelper();
        }
        return dataHelper;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Lesson> getAllLessons() {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
        Root<Lesson> root = query.from(Lesson.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }
    public void insertLessons(List<Lesson> lessonsList) {

        List<Lesson> listOfLessons = getAllLessons();
//        int i = 0;
//        if(listOfLessons.size()>0) i = listOfLessons.get(listOfLessons.size()-1).getId();

        Session session = getSession();
        for (Lesson lesson:lessonsList){
//            i++;
//            lesson.setId(i);
            session.merge(lesson);
        }
    }

    public List<Teacher> getAllTeachers() {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
        Root<Teacher> root = query.from(Teacher.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    public List<Subject> getAllSubjects(){
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
        Root<Subject> root = query.from(Subject.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    public List<Cabinet> getAllCabinets(){
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Cabinet> query = builder.createQuery(Cabinet.class);
        Root<Cabinet> root = query.from(Cabinet.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    public List<Group> getAllGroups(){
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Group> query = builder.createQuery(Group.class);
        Root<Group> root = query.from(Group.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    public List<String> getAllAdminsTeachers(){
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<AdminTeacher> root = query.from(AdminTeacher.class);
        query.select(root.get("userid"));
        return session.createQuery(query).getResultList();
    }

    public void insertAdminsTeachers(String teacherLogin) {
        List<String> listOfAdminsTeachers = getAllAdminsTeachers();

        Session session = getSession();
        boolean isLoginThere = false;
        for (String loginOfTeacher : listOfAdminsTeachers) {
            if (loginOfTeacher.equals(teacherLogin)) {
                isLoginThere = true;
                break;
            }
        }
        if (!isLoginThere) {
            AdminTeacher adminTeacher = new AdminTeacher();
            adminTeacher.setUserid(teacherLogin);
            session.persist(adminTeacher);
        }
    }

    public void deleteAdminTeacher(String teacherId){
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<AdminTeacher> criteriaDelete = builder.createCriteriaDelete(AdminTeacher.class);
        Root<AdminTeacher> root = criteriaDelete.from(AdminTeacher.class);
        criteriaDelete.where(builder.equal(root.get("userid"), teacherId));
        session.createQuery(criteriaDelete).executeUpdate();
    }

    public List<UsersGroups> getAllUsersGroups(){
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UsersGroups> query = builder.createQuery(UsersGroups.class);
        Root<UsersGroups> root = query.from(UsersGroups.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    public void insertUsersGroups(String login, String group) {
        List<UsersGroups> listOfUsersGroups = getAllUsersGroups();

        Session session = getSession();
        boolean isUserGroupThere = false;
        for (UsersGroups usersGroups : listOfUsersGroups) {
            String userid = usersGroups.getUserid();
            String groupid = usersGroups.getGroupid();
            if (userid.equals(login) && groupid.equals(group)) {
                isUserGroupThere = true;
                break;
            }
        }
        if (!isUserGroupThere) {
            UsersGroups usersGroups = new UsersGroups();
            usersGroups.setGroupid(group);
            usersGroups.setUserid(login);
            session.persist(usersGroups);
        }
    }

    public void deleteUsersGroups(String groupid, String userid){
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<UsersGroups> criteriaDelete = builder.createCriteriaDelete(UsersGroups.class);
        Root<UsersGroups> root = criteriaDelete.from(UsersGroups.class);
        criteriaDelete.where(builder.and(builder.equal(root.get("userid"), userid), builder.equal(root.get("groupid"), groupid)));
        session.createQuery(criteriaDelete).executeUpdate();
    }

    public List<Users> getAllUsers(){
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Users> query = builder.createQuery(Users.class);
        Root<Users> root = query.from(Users.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    public void insertUsers(String userid, String password, String email, boolean registered_via_kemsu) {
        List<Users> listOfUsers = getAllUsers();

        Session session = getSession();
        boolean isUserThere = false;
        for (Users users : listOfUsers) {
            String useridt = users.getUserid();
            if (useridt.equals(userid)) {
                isUserThere = true;
                break;
            }
        }
        if (!isUserThere) {
            Users user = new Users();
            user.setUserid(userid);
            user.setPassword(password);
            user.setEmail(email);
            user.setRegisteredViaKemsu(registered_via_kemsu);
            session.persist(user);
        }
    }

    public Users getUserById(String userid) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Users> query = builder.createQuery(Users.class);
        Root<Users> root = query.from(Users.class);
        query.select(root).where(builder.equal(root.get("userid"), userid));
        return session.createQuery(query).uniqueResult();
    }

    public void updateUsersPassword(String userid, String password) throws NoSuchAlgorithmException {
        Session session = getSession();
        Users user = getUserById(userid);
        if (user != null) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            String hashedPasswordStr = sb.toString();
            user.setPassword(hashedPasswordStr);
            session.merge(user);
        }

    }


    public void insertTeachers(List<Teacher> teachers) {
        List<Teacher> listOfTeachers = getAllTeachers();
        List<Teacher> listToInsert = new ArrayList<>();
        Teacher teacherZero = new Teacher();
        teacherZero.setTeacherName("unknown");
        teacherZero.setTeacherPatronymic("unknown");
        teacherZero.setTeacherSurname("unknown");
        teacherZero.setDepartment("unknown");
        teacherZero.setTitle("unknown");
        teacherZero.setId(0);
        teachers.add(teachers.get(0));
        teachers.add(0, teacherZero);

        Session session = getSession();
        for (Teacher teacher : teachers) {
            boolean found = false;
            String teacherName = teacher.getTeacherName();
            String teacherMiddleName = teacher.getTeacherPatronymic();
            String teacherLastName = teacher.getTeacherSurname();
            for(Teacher teacher1:listOfTeachers){
                if (teacher1.getTeacherName().equals(teacherName) && teacher1.getTeacherPatronymic().equals(teacherMiddleName) && teacher1.getTeacherSurname().equals(teacherLastName)) {
                    found = true;
                    break;
                }
            }
            if(!found){
                listToInsert.add(teacher);
            }
        }
        for(Teacher teacher:listToInsert){
            session.merge(teacher);
        }
    }
    public void insertSubjects(List<Subject> subjects) {
        List<Subject> listOfSubjects = getAllSubjects();
        List<Subject> listToInsert = new ArrayList<>();
        Subject subjectZero = new Subject();
        subjectZero.setSubjectName("unknown");
        subjects.add(subjects.get(0));
        subjects.add(0, subjectZero);

        Session session = getSession();
        for (Subject subject : subjects) {
            boolean found = false;
            String subjectName = subject.getSubjectName();
            for(Subject subject1:listOfSubjects){
                if (subject1.getSubjectName().equals(subjectName)) {
                    found = true;
                    break;
                }
            }
            if(!found){
                listToInsert.add(subject);
            }
        }
        for(Subject subject:listToInsert){
            session.merge(subject);
        }
    }
    public void insertGroups(List<Group> groups) {
        List<Group> listOfGroups = getAllGroups();
        List<Group> listToInsert = new ArrayList<>();
        Group groupZero = new Group();
        groupZero.setGroupName("unknown");
        groupZero.setFullGroupName("unknown");
        groupZero.setInstitute("unknown");
        groups.add(groups.get(0));
        groups.add(0, groupZero);

        Session session = getSession();
        for (Group group : groups) {
            boolean found = false;
            String groupName = group.getGroupName();
            String groupNameFull = group.getFullGroupName();
            String instituteName = group.getInstitute();
            for(Group group1:listOfGroups){
                if (group1.getGroupName().equals(groupName) && group1.getFullGroupName().equals(groupNameFull) && group1.getInstitute().equals(instituteName)) {
                    found = true;
                    break;
                }
            }
            if(!found){
                listToInsert.add(group);
            }
        }
        for(Group group:listToInsert){
            session.merge(group);
        }
    }
    public void insertCabinets(List<Cabinet> cabinets) {
        List<Cabinet> listOfTeachers = getAllCabinets();
        List<Cabinet> listToInsert = new ArrayList<>();
        Cabinet cabinetZero = new Cabinet();
        cabinetZero.setCabinetName("unknown");
        cabinetZero.setSeats("unknown");
        cabinetZero.setType("unknown");
        cabinets.add(cabinets.get(0));
        cabinets.add(0, cabinetZero);

        Session session = getSession();
        for (Cabinet cabinet : cabinets) {
            boolean found = false;
            String cabinetName = cabinet.getCabinetName();
            String cabinetSeats = cabinet.getSeats();
            String cabinetType = cabinet.getType();
            for(Cabinet cabinet1:listOfTeachers){
                if (cabinet1.getCabinetName().equals(cabinetName) && cabinet1.getSeats().equals(cabinetSeats) && cabinet1.getType().equals(cabinetType)) {
                    found = true;
                    break;
                }
            }
            if(!found){
                listToInsert.add(cabinet);
            }
        }
        for(Cabinet cabinet:listToInsert){
            session.merge(cabinet);
        }
    }




}
