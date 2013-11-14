// Acknowledgments: This example is a modification of code provided 
// by Dimitri Rakitine.

// Usage from command line on key.csc.ncsu.edu: 
// see instructions in FAQ
// Website for Oracle setup at NCSU : http://www.csc.ncsu.edu/techsupport/technotes/oracle.php

//Note: If you run the program more than once, it will not be able to create the COFFEES table anew after the first run; 
//	you can remove the COFFEES tables between the runs by typing "drop table COFFEES;" in SQL*Plus.


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
        insertQuestionsWithThreshold(myConn);
        insertClassObvTypeData(myConn);
        insertPatients(myConn);
        insertHealthProfessionals(myConn);
        insertHealthFriends(myConn);
        insertObservations(myConn);
    }

    private static void insertObservations(MyConnection myConn) throws MyException {
        Observation.insert(4, 1, getDateFromString("04/05/2013 8:15 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:40 AM", "MM/dd/yyyy hh:mm a"), 1, "egg", myConn);
        Observation.insert(4, 1, getDateFromString("04/05/2013 8:15 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:40 AM", "MM/dd/yyyy hh:mm a"), 2, "1", myConn);
        Observation.insert(4, 2, getDateFromString("04/05/2013 8:00 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:05 AM", "MM/dd/yyyy hh:mm a"), 3, "100", myConn);
        Observation.insert(4, 3, getDateFromString("04/05/2013 6:30 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 7:35 AM", "MM/dd/yyyy hh:mm a"), 4, "walking", myConn);
        Observation.insert(4, 3, getDateFromString("04/05/2013 6:30 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 7:35 AM", "MM/dd/yyyy hh:mm a"), 5, "30", myConn);
        Observation.insert(4, 8, getDateFromString("04/05/2013 9:00 PM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 9:00 PM", "MM/dd/yyyy hh:mm a"), 11, "neutral", myConn);
        Observation.insert(4, 10, getDateFromString("04/05/2013 6:00 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 6:10 AM", "MM/dd/yyyy hh:mm a"), 13, "98.2", myConn);
        Observation.insert(4, 5, getDateFromString("04/05/2013 11:00 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 12:00 PM", "MM/dd/yyyy hh:mm a"), 8, "20", myConn);
        Observation.insert(4, 6, getDateFromString("04/05/2013 10:00 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 10:10 AM", "MM/dd/yyyy hh:mm a"), 9, "78", myConn);
        Observation.insert(4, 2, getDateFromString("04/06/2013 8:00 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:05 AM", "MM/dd/yyyy hh:mm a"), 3, "102", myConn);

        Observation.insert(1, 2, getDateFromString("04/05/2013 7:50 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:00 AM", "MM/dd/yyyy hh:mm a"), 3, "150", myConn);
        Observation.insert(1, 2, getDateFromString("04/06/2013 8:00 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:05 AM", "MM/dd/yyyy hh:mm a"), 3, "156", myConn);

        Observation.insert(2, 4, getDateFromString("04/06/2013 7:50 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:00 AM", "MM/dd/yyyy hh:mm a"), 6, "150", myConn);
        Observation.insert(2, 4, getDateFromString("04/06/2013 7:50 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:00 AM", "MM/dd/yyyy hh:mm a"), 7, "96", myConn);
        Observation.insert(2, 4, getDateFromString("04/08/2013 8:00 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:05 AM", "MM/dd/yyyy hh:mm a"), 6, "170", myConn);
        Observation.insert(2, 4, getDateFromString("04/08/2013 8:00 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:05 AM", "MM/dd/yyyy hh:mm a"), 7, "90", myConn);

        Observation.insert(3, 4, getDateFromString("04/06/2013 7:50 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:00 AM", "MM/dd/yyyy hh:mm a"), 6, "162", myConn);
        Observation.insert(3, 4, getDateFromString("04/06/2013 7:50 AM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 8:00 AM", "MM/dd/yyyy hh:mm a"), 7, "110", myConn);

        Observation.insert(7, 7, getDateFromString("04/06/2013 1:00 PM", "MM/dd/yyyy hh:mm a"),
                getDateFromString("04/05/2013 6:00 PM", "MM/dd/yyyy hh:mm a"), 10, "8", myConn);

    }

    private static void insertHealthFriends(MyConnection myConn) throws MyException {
        HealthFriend.insert(6, 2, myConn);
        HealthFriend.insert(6, 5, myConn);
        HealthFriend.insert(4, 1, myConn);
        HealthFriend.insert(4, 5, myConn);
        HealthFriend.insert(4, 6, myConn);
    }

    private static void insertHealthProfessionals(MyConnection myConn) throws MyException {
        HealthProfessional.insert("Altaf Hussain", "Dayview", "hussain123", myConn);
        HealthProfessional.insert("Manu Joseph", "Dayview", "joseph123", myConn);
        HealthProfessional.insert("Shane Lee", "Huntington", "lee123", myConn);
        HealthProfessional.insert("Shyam Prasad", "Huntington", "prasad123", myConn);
    }

    private static java.util.Date getDateFromString(String dob, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            formatter.setLenient(false);

            return formatter.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private static void insertPatients(MyConnection connection) throws MyException {
        int pid;
        pid = Patient.insert(getDateFromString("01/01/1988", "MM/dd/yyyy"), "Gary George", 2806, "Conifer Drive", "NC 27606",
                "m", "y", "geo123", connection);
        PatientClassRelationship.insert(pid, 1, connection);

        pid = Patient.insert(getDateFromString("02/01/1982", "MM/dd/yyyy"), "Adnan Kazi", 1234, "Capability Drive", "NC 27655",
                "f", "y", "kazi123", connection);
        PatientClassRelationship.insert(pid, 2, connection);
        PatientClassRelationship.insert(pid, 3, connection);

        pid = Patient.insert(getDateFromString("01/01/1973", "MM/dd/yyyy"), "Neha Shetty", 440, "Sullivan Drive", "NC 27517",
                "f", "y", "shetty123", connection);
        PatientClassRelationship.insert(pid, 2, connection);
        PatientClassRelationship.insert(pid, 3, connection);

        pid = Patient.insert(getDateFromString("01/01/1980", "MM/dd/yyyy"), "Sheldon Cooper", 2808, "Avent Ferry Road", "NC 27616",
                "f", "y", "cooper123", connection);
        PatientClassRelationship.insert(pid, 4, connection);
        PatientClassRelationship.insert(pid, 1, connection);

        pid = Patient.insert(getDateFromString("01/01/1966", "MM/dd/yyyy"), "Michael Watson", 2222, "Gorman Street", "NC 27678",
                "m", "y", "watson123", connection);
        PatientClassRelationship.insert(pid, 4, connection);

        pid = Patient.insert(getDateFromString("01/01/1973", "MM/dd/yyyy"), "Tom Kerr", 1430, "Collegeview Ave", "NC 27701",
                "m", "y", "tkerr123", connection);
        PatientClassRelationship.insert(pid, 4, connection);
        PatientClassRelationship.insert(pid, 2, connection);

        pid = Patient.insert(getDateFromString("01/01/1976", "MM/dd/yyyy"), "Maya Tran", 100, "Brown Circle", "NC 27516",
                "f", "y", "tran123", connection);
        PatientClassRelationship.insert(pid, 3, connection);

    }

    private static void insertObservationCategories(MyConnection myConn) throws MyException {
        ObservationCategory.insert("Behavioral", myConn);
        ObservationCategory.insert("Physiological", myConn);
        ObservationCategory.insert("Psychological", myConn);
        ObservationCategory.insert("General", myConn);
    }

    private static void insertObservationTypes(MyConnection myConn) throws MyException {
        ObservationType.insertForCategory("Diet", "Behavioral", myConn);
        ObservationType.insertForCategory("Weight", "Physiological", myConn);
        ObservationType.insertForCategory("Exercise", "Behavioral", myConn);

        ObservationType.insertForCategory("Blood Pressure", "Physiological", myConn);
        ObservationType.insertForCategory("Exercise Tolerance", "Physiological", myConn);
        ObservationType.insertForCategory("Oxygen Saturation", "Physiological", myConn);
        ObservationType.insertForCategory("Pain", "Physiological", myConn);

        ObservationType.insertForCategory("Mood", "Psychological", myConn);
        ObservationType.insertForCategory("Contraction", "Physiological", myConn);
        ObservationType.insertForCategory("Temperature", "Physiological", myConn);
    }

    private static void insertQuestionsWithThreshold(MyConnection myConn) throws MyException {
        int qid;
        ObservationQuestion.insertByTypeName("Diet", "What was consumed?", myConn);
        ObservationQuestion.insertByTypeName("Diet", "Amount in servings", myConn);
        ObservationQuestion.insertByTypeName("Weight", "Amount in pounds", myConn);
        ObservationQuestion.insertByTypeName("Exercise", "What kind: walking, cycling or jogging?", myConn);
        ObservationQuestion.insertByTypeName("Exercise", "Duration: number of minutes", myConn);
        qid = ObservationQuestion.insertByTypeName("Blood Pressure", "How much was the Systolic pressure?", myConn);
        ObservationThreshold.insert(qid, 140, myConn);
        qid = ObservationQuestion.insertByTypeName("Blood Pressure", "How much was the Diastolic pressure?", myConn);
        ObservationThreshold.insert(qid, 90, myConn);
        qid = ObservationQuestion.insertByTypeName("Exercise Tolerance", "Number of steps before exhaustion", myConn);
        ObservationThreshold.insert(qid, -180, myConn);
        qid = ObservationQuestion.insertByTypeName("Oxygen Saturation", "The percentage of hemoblogin that is saturated by oxygen, eg: 95", myConn);
        ObservationThreshold.insert(qid, -88, myConn);
        qid = ObservationQuestion.insertByTypeName("Pain", "Enter the pain levels: Scale[1-10] ", myConn);
        ObservationThreshold.insert(qid, 7, myConn);
        ObservationQuestion.insertByTypeName("Mood", "One of the values: happy, sad, neutral", myConn);
        qid = ObservationQuestion.insertByTypeName("Contraction", "Enter frequency (Number of times every half an hour)", myConn);
        ObservationThreshold.insert(qid, 4, myConn);
        qid = ObservationQuestion.insertByTypeName("Temperature", "Amount in Fahrenheit", myConn);
        ObservationThreshold.insert(qid, 102, myConn);
    }

    private static void insertClassObvTypeData(MyConnection myConn) throws MyException {
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("General", "Diet", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("General", "Weight", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("General", "Exercise", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("General", "Mood", myConn);

        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("HIV", "Temperature", myConn);

        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("COPD", "Oxygen Saturation", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("COPD", "Exercise Tolerance", myConn);

        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("High Risk Pregnancy", "Pain", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("High Risk Pregnancy", "Contraction", myConn);
        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("High Risk Pregnancy", "Blood Pressure", myConn);

        PatientClassObservationTypeMapper.insertByClassNameAndTypeName("Obesity", "Blood Pressure", myConn);
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
                "name VARCHAR2(25), aptNo INTEGER, city varchar2(20), country varchar2(20), sex VARCHAR2(1), publicStatus VARCHAR2(1), " +
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
                "(qid INTEGER REFERENCES ObservationQuestion(qid) PRIMARY KEY, threshold NUMBER)");

        myConn.stmt.executeUpdate("CREATE TABLE HealthFriend " +
                "(pid INTEGER REFERENCES Patient(pid), fid INTEGER REFERENCES Patient(pid), timestamp TIMESTAMP(2), " +
                "PRIMARY KEY (pid, fid))");

        myConn.stmt.executeUpdate("CREATE TABLE HealthProfessional " +
                "(hpid INTEGER PRIMARY KEY, name VARCHAR2(25), clinic VARCHAR2(30), password VARCHAR2(30))");

    }

    static void close(ResultSet rs) {
        if (rs != null) try {
            rs.close();
        } catch (Throwable whatever) {
            whatever.printStackTrace();
        }
    }
}

