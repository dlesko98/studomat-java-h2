package com.example.studomat.database;

import com.example.studomat.AdminShowAllStudentsController;
import com.example.studomat.model.Exam;
import com.example.studomat.model.News;
import com.example.studomat.model.Subject;
import com.example.studomat.model.User;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/*To drop whole DB:
drop table user_exam;
drop table user_subject;
drop table exams;
drop table subject;
drop table news;
drop table users;
 */

public class Database {

    private User selectedUser = AdminShowAllStudentsController.selectedUser;

    public static Connection connectToDatabase () throws SQLException, IOException {

        Properties configuration = new Properties();
        configuration.load(new FileReader("database.properties"));

        String databaseURL = configuration.getProperty("databaseURL");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
    }

    public static Connection disconnectFromDatabase (Connection connection) throws SQLException {

        connection.close();

        return connection;
    }
    public static void createTable () throws SQLException, IOException {
        Connection connection = connectToDatabase();
        Statement sqlStatement = connection.createStatement();
        sqlStatement.execute("CREATE TABLE IF NOT EXISTS USERS( ID LONG, USERNAME VARCHAR(50) NOT NULL, PASSWORD VARCHAR(30) NOT NULL, FIRST_NAME VARCHAR(30) NOT NULL, LAST_NAME VARCHAR(30) NOT NULL, DATE_OF_BIRTH DATE NOT NULL, PICTURE_NAME VARCHAR(50) NOT NULL, PRIMARY KEY (ID) );");
        sqlStatement.execute("CREATE TABLE IF NOT EXISTS SUBJECT( ID LONG, NAME VARCHAR(50) NOT NULL, NUMBER_OF_ECTS INT NOT NULL, NUMBER_OF_SEMESTER INT NOT NULL, PRIMARY KEY (ID) );");
        sqlStatement.execute("CREATE TABLE IF NOT EXISTS USER_SUBJECT( USER_ID LONG NOT NULL, SUBJECT_ID LONG NOT NULL, PRIMARY KEY (USER_ID, SUBJECT_ID), FOREIGN KEY (USER_ID) REFERENCES USERS(ID), FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT(ID) );");
        sqlStatement.execute("CREATE TABLE IF NOT EXISTS NEWS( ID LONG, TEXT VARCHAR(500) NOT NULL, PRIMARY KEY (ID));");
        sqlStatement.execute("CREATE TABLE IF NOT EXISTS EXAMS( ID LONG, SUBJECT_ID LONG, DATE_OF_EXAM DATE NOT NULL, SUBJECT_NAME VARCHAR(50), PRIMARY KEY (ID));");
        sqlStatement.execute("CREATE TABLE IF NOT EXISTS USER_EXAM( USER_ID LONG NOT NULL, EXAM_ID LONG NOT NULL, PRIMARY KEY (USER_ID, EXAM_ID), FOREIGN KEY (USER_ID) REFERENCES USERS(ID), FOREIGN KEY (EXAM_ID) REFERENCES EXAMS(ID) );");

        List<News> newsList = getAllNewsFromDatabase();
        if (newsList.isEmpty()) {
            News news = new News(0L, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation \n" +
                    "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in \n" +
                    "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            News news1 = new News(1L, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation \n" +
                    "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in \n" +
                    "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            registerNewNews(news);
            registerNewNews(news1);
        }
        List<User> userList = getAllUsersFromDatabase();
        if (userList.isEmpty()) {
            User admin = new User(0L, "admin", "admin", "Domagoj", "Leško", LocalDate.of(1998, 05, 25), Collections.emptyList(), "avatar.png");
            User student = new User(1L, "domagoj", "domagoj", "Domagoj", "Leško", LocalDate.of(1998, 05, 25), Collections.emptyList(), "avatar.png");
            registerNewUser(admin);
            registerNewUser(student);
        }
        disconnectFromDatabase(connection);
    }
    public static String loginCheck (List<User> userList, String userName, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(userName)) {
                if (user.getPassword().equals(password)) {
                    return "Uspjeh";
                }else return "Kriva sifra";
            }
        }return "Neuspjeh";
    }
    public static List<User> getAllUsersFromDatabase() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<User> userList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet userResultSet = sqlStatement.executeQuery("SELECT * FROM USERS;");

        while(userResultSet.next()) {
            Long userId = userResultSet.getLong("ID");
            String userName = userResultSet.getString("USERNAME");
            String password = userResultSet.getString("PASSWORD");
            String firstName = userResultSet.getString("FIRST_NAME");
            String lastName = userResultSet.getString("LAST_NAME");
            String dateOfBirthString = userResultSet.getString("DATE_OF_BIRTH");
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthString);
            String pictureName = userResultSet.getString("PICTURE_NAME");

            User user = new User(userId, userName, password, firstName, lastName, dateOfBirth, Collections.emptyList(), pictureName);
            List<Subject> activeList = Database.getAllSubjectsForUserFromDatabase(user);
            user.setSubjectList(activeList);
            userList.add(user);
        }

        disconnectFromDatabase(connection);

        return userList;
    }
    public static void registerNewUser (User user) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO USERS(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, PICTURE_NAME)VALUES(?, ?, ?, ?, ?, ?, ?);");

        stmt.setString(1, user.getId().toString());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getFirstName());
        stmt.setString(5, user.getLastName());
        stmt.setString(6, user.getDateOfBirth().toString());
        stmt.setString(7, "avatar.png");
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void updateUser (String name, String lastName, LocalDate dateOfBirth, String username, String password, Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE USERS set FIRST_NAME = ?, LAST_NAME = ?, DATE_OF_BIRTH = ?, USERNAME = ?, PASSWORD = ? WHERE ID = ?;");

        stmt.setString(1, name);
        stmt.setString(2, lastName);
        stmt.setString(3, dateOfBirth.toString());
        stmt.setString(4, username);
        stmt.setString(5, password);
        stmt.setLong(6, id);
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void deleteUser (User user) throws SQLException, IOException {

        deleteAllExamsFromUser(user);
        deleteAllSubjectsFromUser(user);

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM USERS WHERE ID = ?;");

        stmt.setString(1, user.getId().toString());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void updateSubject (String name, String ects, String numberOfSemester, Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE SUBJECT set NAME = ?, NUMBER_OF_ECTS = ?, NUMBER_OF_SEMESTER = ? WHERE ID = ?;");

        stmt.setString(1, name);
        stmt.setString(2, ects);
        stmt.setString(3, numberOfSemester);
        stmt.setLong(4, id);
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void deleteSubject (Subject subject) throws SQLException, IOException {

        deleteSubjectFromUser(subject);
        List<Exam> examList = getAllExamsForSubjectFromDatabase(subject);

        for (Exam exam : examList) {
            removeExamsFromUserByIdForSubject(exam.getId().intValue());
            deleteExam(exam);
        }



        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM SUBJECT WHERE ID = ?;");

        stmt.setString(1, subject.getId().toString());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void deleteSubjectFromUser (Subject subject) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM USER_SUBJECT WHERE SUBJECT_ID = ?;");

        stmt.setString(1, subject.getId().toString());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void deleteExamFromUser (Exam exam) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM USER_EXAM WHERE EXAM_ID = ?;");

        stmt.setString(1, exam.getId().toString());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void deleteAllSubjectsFromUser (User user) throws SQLException, IOException {
        List<Subject> subjectList = Database.getAllSubjectsForUserFromDatabase(user);
        for (Subject subject : subjectList) {
            Database.removeSubjectFromUserById(user.getId().intValue(), subject.getId().intValue());
        }
    }
    public static void deleteAllExamsFromUser (User user) throws SQLException, IOException {
        List<Exam> examList = Database.getAllExamsForUserFromDatabase(user);
        for (Exam exam : examList) {
            Database.removeExamFromUserById(user.getId().intValue(), exam.getId().intValue());
        }
    }
    public static void updatePictureUser (User user, String pictureName) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE USERS SET PICTURE_NAME = ? WHERE ID = ?;");

        stmt.setString(1, pictureName);
        stmt.setString(2, user.getId().toString());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static List<Subject> getAllSubjectsFromDatabase() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Subject> subjectList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet subjectResultSet = sqlStatement.executeQuery("SELECT * FROM SUBJECT;");

        while(subjectResultSet.next()) {
            Long subjectId = subjectResultSet.getLong("ID");
            String name = subjectResultSet.getString("NAME");
            Integer numberOfECTS = subjectResultSet.getInt("NUMBER_OF_ECTS");
            Integer numberOfSemester = subjectResultSet.getInt("NUMBER_OF_SEMESTER");

            Subject subject = new Subject(subjectId, name, numberOfECTS, numberOfSemester);
            subjectList.add(subject);
        }

        disconnectFromDatabase(connection);

        return subjectList;
    }
    public static void insertNewSubject (Subject subject) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO SUBJECT(ID, NAME, NUMBER_OF_ECTS, NUMBER_OF_SEMESTER)VALUES(?, ?, ?, ?);");

        stmt.setString(1, subject.getId().toString());
        stmt.setString(2, subject.getName());
        stmt.setString(3, subject.getNumberOfECTS().toString());
        stmt.setString(4, subject.getNumberOfSemester().toString());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static List<Subject> getAllSubjectsForUserFromDatabase(User user) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Subject> subjectList = new ArrayList<>();

        PreparedStatement sqlStatement = connection.prepareStatement
                ("SELECT * FROM USER_SUBJECT US, SUBJECT S WHERE US.USER_ID = ? AND US.SUBJECT_ID = S.ID;");

        String userId = user.getId().toString();

        sqlStatement.setString(1, userId);

        ResultSet subjectResultSet = sqlStatement.executeQuery();

        List<Subject> listOfSelectedSubjects = new ArrayList<>();

        while (subjectResultSet.next()) {
            Long subjectId = subjectResultSet.getLong("SUBJECT_ID");
            List<Subject> listOfSubjects = Database.getAllSubjectsFromDatabase();
            for (Subject subject : listOfSubjects) {
                if (subject.getId().equals(subjectId)) {
                    listOfSelectedSubjects.add(subject);
                }
            }
        }


        connection.close();

        return listOfSelectedSubjects;
    }
    public static void addNewSubjectToUserById (int userId, int subjectId) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO USER_SUBJECT(USER_ID, SUBJECT_ID) VALUES(?, ?);");


        stmt.setString(1, String.valueOf(userId));
        stmt.setString(2, String.valueOf(subjectId));
        stmt.executeUpdate();

        connection.close();
    }
    public static void removeSubjectFromUserById (int userId, int subjectId) throws SQLException, IOException {

        Subject chosenSubject = null;
        List<Subject> listOfSubjects = Database.getAllSubjectsFromDatabase();
        for (Subject subject : listOfSubjects) {
            if (subject.getId().intValue() == subjectId) {
                chosenSubject = subject;
            }
        }
        List<Exam> examList = Database.getAllExamsForSubjectFromDatabase(chosenSubject);

        for (Exam exam : examList) {
            Database.removeExamFromUserById(userId, exam.getId().intValue());
        }

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM USER_SUBJECT WHERE USER_ID = ? AND SUBJECT_ID = ?;");


        stmt.setString(1, String.valueOf(userId));
        stmt.setString(2, String.valueOf(subjectId));
        stmt.executeUpdate();

        connection.close();
    }
    public static List<News> getAllNewsFromDatabase() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<News> newsList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet userResultSet = sqlStatement.executeQuery("SELECT * FROM NEWS;");

        while(userResultSet.next()) {
            Long newsId = userResultSet.getLong("ID");
            String text = userResultSet.getString("TEXT");

            News news = new News(newsId, text);
            newsList.add(news);
        }

        disconnectFromDatabase(connection);

        return newsList;
    }
    public static void registerNewNews (News news) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO NEWS(ID, TEXT)VALUES(?, ?);");

        stmt.setString(1, news.getId().toString());
        stmt.setString(2, news.getText());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void updateNews (String text, Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE NEWS set TEXT = ? WHERE ID = ?;");

        stmt.setString(1, text);
        stmt.setLong(2, id);
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static List<Exam> getAllExamsFromDatabase() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Exam> examsList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet examResultSet = sqlStatement.executeQuery("SELECT * FROM EXAMS;");

        while(examResultSet.next()) {
            Long examId = examResultSet.getLong("ID");
            Long subjectId = examResultSet.getLong("SUBJECT_ID");
            String dateOfExamString = examResultSet.getString("DATE_OF_EXAM");
            LocalDate dateOfExam = LocalDate.parse(dateOfExamString);
            String subjectName = examResultSet.getString("SUBJECT_NAME");

            Exam exam = new Exam(examId, dateOfExam, subjectId, subjectName);
            examsList.add(exam);
        }

        disconnectFromDatabase(connection);

        return examsList;
    }
    public static void addNewExamToUserById (int userId, int examId) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO USER_EXAM(USER_ID, EXAM_ID) VALUES(?, ?);");


        stmt.setString(1, String.valueOf(userId));
        stmt.setString(2, String.valueOf(examId));
        stmt.executeUpdate();

        connection.close();
    }
    public static void removeExamFromUserById (int userId, int examId) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM USER_EXAM WHERE USER_ID = ? AND EXAM_ID = ?;");


        stmt.setString(1, String.valueOf(userId));
        stmt.setString(2, String.valueOf(examId));
        stmt.executeUpdate();

        connection.close();
    }
    public static void removeExamsFromUserByIdForSubject (int examId) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM USER_EXAM WHERE EXAM_ID = ?;");


        stmt.setString(1, String.valueOf(examId));
        stmt.executeUpdate();

        connection.close();
    }
    public static List<Exam> getAllExamsForUserFromDatabase(User user) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Exam> examList = new ArrayList<>();

        PreparedStatement sqlStatement = connection.prepareStatement
                ("SELECT * FROM USER_EXAM UE, EXAMS E WHERE UE.USER_ID = ? AND UE.EXAM_ID = E.ID;");

        String userId = user.getId().toString();

        sqlStatement.setString(1, userId);

        ResultSet examResultSet = sqlStatement.executeQuery();

        List<Exam> listOfSelectedExams = new ArrayList<>();

        while (examResultSet.next()) {
            Long examId = examResultSet.getLong("EXAM_ID");
            List<Exam> listOfExams = Database.getAllExamsFromDatabase();
            for (Exam exam : listOfExams) {
                if (exam.getId().equals(examId)) {
                    listOfSelectedExams.add(exam);
                }
            }
        }


        connection.close();

        return listOfSelectedExams;
    }
    public static List<Exam> getAllExamsForSubjectFromDatabase(Subject subject) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Exam> examList = new ArrayList<>();

        PreparedStatement sqlStatement = connection.prepareStatement
                ("SELECT * FROM EXAMS E WHERE E.SUBJECT_ID = ?;");

        sqlStatement.setString(1, subject.getId().toString());

        ResultSet examResultSet = sqlStatement.executeQuery();

        List<Exam> listOfSelectedExams = new ArrayList<>();

        while (examResultSet.next()) {
            Long examId = examResultSet.getLong("ID");
            List<Exam> listOfExams = Database.getAllExamsFromDatabase();
            for (Exam exam : listOfExams) {
                if (exam.getId().equals(examId)) {
                    listOfSelectedExams.add(exam);
                }
            }
        }


        connection.close();

        return listOfSelectedExams;
    }
    public static void insertNewExam (Exam exam) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO EXAMS(ID, SUBJECT_ID, DATE_OF_EXAM, SUBJECT_NAME)VALUES(?, ?, ?, ?);");

        stmt.setString(1, exam.getId().toString());
        stmt.setString(2, exam.getSubjectId().toString());
        stmt.setString(3, exam.getDate().toString());
        stmt.setString(4, exam.getSubjectName());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void updateExam (LocalDate dateOfExam, Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE EXAMS set DATE_OF_EXAM = ? WHERE ID = ?;");

        stmt.setString(1, dateOfExam.toString());
        stmt.setLong(2, id);
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
    public static void deleteExam (Exam exam) throws SQLException, IOException {

        deleteExamFromUser(exam);

        Connection connection = connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM EXAMS WHERE ID = ?;");

        stmt.setString(1, exam.getId().toString());
        stmt.executeUpdate();

        disconnectFromDatabase(connection);
    }
}
