// Acknowledgments: This example is a modification of code provided 
// by Dimitri Rakitine.

// Usage from command line on key.csc.ncsu.edu: 
// see instructions in FAQ
// Website for Oracle setup at NCSU : http://www.csc.ncsu.edu/techsupport/technotes/oracle.php

//Note: If you run the program more than once, it will not be able to create the COFFEES table anew after the first run; 
//	you can remove the COFFEES tables between the runs by typing "drop table COFFEES;" in SQL*Plus.


import java.sql.*;

public class Initializer {

    public static void main(String[] args) {
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            MyConnection myConn = null;
            ResultSet rs = null;

            try {
                myConn = new MyConnection();

                createTables(myConn);
                insertData(myConn);

                // show all tables: select TABLE_NAME from user_tables;
                // delete all tables: select 'drop table '||table_name||' cascade constraints;' from user_tables;

            } finally {
                close(rs);
                if (myConn != null) {
                    myConn.closeStatement();
                    myConn.closeConnection();
                }
            }
        } catch (Throwable oops) {
            if (oops instanceof MyException)
                System.out.println(((MyException) oops).message);
            oops.printStackTrace();
        }
    }

    private static void insertData(MyConnection myConn) throws MyException {
        insertPatientClasses(myConn);
        insertObservationCategories(myConn);
        insertObservationTypes(myConn);
        insertQuestions(myConn);
        insertClassObvTypeData(myConn);
    }

    private static void insertObservationCategories(MyConnection myConn) throws MyException {
        ObservationCategory.insert("Behavioral", myConn);
        ObservationCategory.insert("Physiological", myConn);
        ObservationCategory.insert("Psychological", myConn);
        ObservationCategory.insert("General", myConn);
    }

    private static void insertObservationTypes(MyConnection myConn) throws MyException {
        ObservationType.insertForCategory("Diet", "Behavioral", myConn);
        ObservationType.insertForCategory("Weight", "Behavioral", myConn);
        ObservationType.insertForCategory("Exercise", "Behavioral", myConn);

        ObservationType.insertForCategory("Blood Pressure", "Physiological", myConn);
        ObservationType.insertForCategory("Exercise Tolerance", "Physiological", myConn);
        ObservationType.insertForCategory("Oxygen Saturation", "Physiological", myConn);
        ObservationType.insertForCategory("Pain", "Physiological", myConn);

        ObservationType.insertForCategory("Mood", "Psychological", myConn);
        ObservationType.insertForCategory("Contraction", "Psychological", myConn);
        ObservationType.insertForCategory("Temperature", "Psychological", myConn);
    }

    private static void insertQuestions(MyConnection myConn) throws MyException {
        ObservationQuestion.insertByTypeName("Diet", "What was consumed?", myConn);
        ObservationQuestion.insertByTypeName("Diet", "How much was consumed?", myConn);
        ObservationQuestion.insertByTypeName("Weight", "How much was your weight?", myConn);
        ObservationQuestion.insertByTypeName("Exercise", "What kind of exercise was done?", myConn);
        ObservationQuestion.insertByTypeName("Exercise", "For how much time?", myConn);
        ObservationQuestion.insertByTypeName("Blood Pressure", "How much was the Systolic pressure?", myConn);
        ObservationQuestion.insertByTypeName("Blood Pressure", "How much was the Diastolic pressure?", myConn);
        ObservationQuestion.insertByTypeName("Exercise Tolerance", "Enter the number of steps before exhaustion", myConn);
        ObservationQuestion.insertByTypeName("Oxygen Saturation", "Enter the oxygen saturation amount", myConn);
        ObservationQuestion.insertByTypeName("Pain", "Enter the pain levels: Scale[1-10] ", myConn);
        ObservationQuestion.insertByTypeName("Mood", "Enter mood from one amongst these: {Happy, Sad, Neutral}", myConn);
        ObservationQuestion.insertByTypeName("Contraction", "Enter frequency (Number of times every half an hour)", myConn);
        ObservationQuestion.insertByTypeName("Temperature", "Enter your temperature", myConn);
    }

    private static void insertClassObvTypeData(MyConnection myConn) throws MyException {
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("General", "Diet", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("General", "Weight", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("General", "Exercise", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("General", "Mood", myConn);
    }

    private static void insertPatientClasses(MyConnection myConn) throws MyException {
         PatientClass.insert("HIV", myConn);
         PatientClass.insert("Obesity", myConn);
         PatientClass.insert("High Risk Pregnancy", myConn);
         PatientClass.insert("COPD", myConn);
         PatientClass.insert("General", myConn);
    }

    private static void createTables(MyConnection myConn) throws SQLException {
        myConn.stmt.executeUpdate("CREATE TABLE Patient " +
                "(pid INTEGER PRIMARY KEY, dob DATE, " +
                "name VARCHAR2(25), address VARCHAR2(30), sex VARCHAR2(1), publicStatus VARCHAR2(1), " +
                "password VARCHAR2(30))");

        myConn.stmt.executeUpdate("CREATE TABLE PatientClass " +
                "(cid INTEGER PRIMARY KEY, name VARCHAR2(25))");

        myConn.stmt.executeUpdate("CREATE TABLE PatientClassRelationship " +
                "(pid INTEGER REFERENCES Patient(pid), cid INTEGER REFERENCES PatientClass(cid), " +
                "PRIMARY KEY (pid, cid))");

        myConn.stmt.executeUpdate("CREATE TABLE ObservationCategory " +
                "(ocid INTEGER PRIMARY KEY, name VARCHAR2(25))");

        myConn.stmt.executeUpdate("CREATE TABLE ObservationType " +
                "(otid INTEGER PRIMARY KEY, name VARCHAR2(25), ocid INTEGER REFERENCES ObservationCategory(ocid))");

        myConn.stmt.executeUpdate("CREATE TABLE ObservationQuestion " +
                "(qid INTEGER PRIMARY KEY, text VARCHAR2(100), otid INTEGER REFERENCES ObservationType(otid))");

        myConn.stmt.executeUpdate("CREATE TABLE Observation " +
                "(pid INTEGER REFERENCES Patient(pid), otid INTEGER REFERENCES ObservationType(otid), " +
                "obvTimestamp TIMESTAMP(2), recTimestamp TIMESTAMP(2), " +
                "qid INTEGER REFERENCES ObservationQuestion(qid), answer VARCHAR2(20), " +
                "PRIMARY KEY (pid, otid, qid, obvTimestamp) )");

        myConn.stmt.executeUpdate("CREATE TABLE PatientClassObvTypeMapper " +
                "(cid INTEGER REFERENCES PatientClass(cid), otid INTEGER REFERENCES ObservationType(otid), " +
                "PRIMARY KEY (cid, otid))");

        myConn.stmt.executeUpdate("CREATE TABLE Alert " +
                "(pid INTEGER REFERENCES Patient(pid), text VARCHAR2(100), viewed CHAR(1), timestamp TIMESTAMP(2), " +
                "PRIMARY KEY (pid, text), CONSTRAINT viewed_values CHECK (viewed IN ('1', '0')))");

        myConn.stmt.executeUpdate("CREATE TABLE ObservationThreshold " +
                "(qid INTEGER REFERENCES ObservationQuestion(qid) PRIMARY KEY, minValue VARCHAR2(20), maxValue VARCHAR2(20))");

        myConn.stmt.executeUpdate("CREATE TABLE HealthFriend " +
                "(pid INTEGER REFERENCES Patient(pid), fid INTEGER REFERENCES Patient(pid), PRIMARY KEY (pid, fid))");


    }

    static void close(ResultSet rs) {
        if (rs != null) try {
            rs.close();
        } catch (Throwable whatever) {
            whatever.printStackTrace();
        }
    }
}

