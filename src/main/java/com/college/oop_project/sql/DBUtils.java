package com.college.oop_project.sql;

import com.college.oop_project.model.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    private static final Driver dr = new Driver();

    public static void getDataFromDB() {
        dr.startConnection();

        getAccessDataFromDB();
        getSchoolsFromDB();
        getProfessorsFromDB();
        getStudentsFromDB();
        getSubjectsFromDB();
        getQuestionsFromDB();

        dr.endConnection();
    }

    private static void getAccessDataFromDB() {
        try {
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from pristupni_podaci");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String userName = resultSet.getString("korisnicko_ime");
                String mail = resultSet.getString("email");
                String pw = resultSet.getString("sifra");

                new AccessData(id, userName, mail, pw);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    private static void getSchoolsFromDB() {
        try {
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from skola");

            while (resultSet.next()) {
                String naziv = resultSet.getString("naziv");
                String mjesto = resultSet.getString("mjesto");
                String grad = resultSet.getString("grad");
                String drzava = resultSet.getString("drzava");

                new School(naziv, mjesto, grad, drzava);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    private static void getProfessorsFromDB() {
        try {
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from profesor");

            while (resultSet.next()) {
                String name = resultSet.getString("ime");
                String surname = resultSet.getString("prezime");
                int sexCode = resultSet.getInt("pol");
                int dataID = resultSet.getInt("pristupni_podaci_id") - 1;

                if (sexCode == 1) {
                    new Professor(name, surname, "muski", dataID);
                } else {
                    new Professor(name, surname, "zenski", dataID);
                }
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    private static void getStudentsFromDB() {
        try {
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from ucenik");

            while (resultSet.next()) {
                String name = resultSet.getString("ime");
                String surname = resultSet.getString("prezime");
                int sexCode = resultSet.getInt("pol");
                int dataID = resultSet.getInt("pristupni_podaci_id") - 1;

                if (sexCode == 1) {
                    new Student(name, surname, "muski", dataID);
                } else {
                    new Student(name, surname, "zenski", dataID);
                }
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    private static void getSubjectsFromDB() {
        try {
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from predmet");

            while (resultSet.next()) {
                new Subject(resultSet.getString("naziv"), resultSet.getInt("razred"));
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    private static void getQuestionsFromDB() {
        try {
            Statement statement = dr.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from pitanje");

            while (resultSet.next()) {
                new Question(resultSet.getString("pitanje"), "");
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    public static void addSchoolToDB(String name, String place, String city, String country) {
        dr.startConnection();

        try {
            String query = "INSERT INTO skola(naziv, grad, mjesto, drzava) VALUES (?,?,?,?)";
            PreparedStatement statement = dr.getConn().prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, city);
            statement.setString(3, place);
            statement.setString(4, country);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dr.endConnection();
    }

    public static void addProfessorToDB(String firstName, String lastName, int sex, int id) {
        dr.startConnection();

        try {
            String query = "INSERT INTO profesor(ime, prezime, pol, pristupni_podaci_id) VALUES (?,?,?,?)";
            PreparedStatement statement = dr.getConn().prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, sex);
            statement.setInt(4, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dr.endConnection();
    }

    public static void addStudentToDB(String firstName, String lastName, int sex, int id) {
        dr.startConnection();

        try {
            String query = "INSERT INTO ucenik(ime, prezime, pol, pristupni_podaci_id) VALUES (?,?,?,?)";
            PreparedStatement statement = dr.getConn().prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, sex);
            statement.setInt(4, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dr.endConnection();
    }

    public static void addSubjectToDB(String name, int grade) {
        dr.startConnection();

        try {
            String query = "INSERT INTO predmet(naziv, razred) VALUES (?,?)";
            PreparedStatement statement = dr.getConn().prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, grade);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dr.endConnection();
    }

    public static String getHashValue(String password) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8);
            byte[] hash = md5.digest(bytesOfPassword);

            // Convert hash into HEX value
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
