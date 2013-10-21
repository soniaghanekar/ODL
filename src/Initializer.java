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

            // Load the driver. This creates an instance of the driver
            // and calls the registerDriver method to make Oracle Thin
            // driver available to clients.

            Class.forName("oracle.jdbc.driver.OracleDriver");

            MyConnection myConn = null;
            ResultSet rs = null;

            try {

                // Get a connection from the first driver in the
                // DriverManager list that recognizes the URL jdbcURL

                myConn = new MyConnection();

                // Create a statement object that will be sending your
                // SQL statements to the DBMS


                myConn.stmt.executeUpdate("CREATE TABLE Patient " +
                        "(pid INTEGER PRIMARY KEY, dob DATE, " +
                        "name VARCHAR2(25), address VARCHAR2(30), sex VARCHAR2(1), publicStatus VARCHAR2(1))");

                myConn.stmt.executeUpdate("CREATE TABLE PatientClass " +
                        "(cid INTEGER PRIMARY KEY, name VARCHAR2(25))");

                myConn.stmt.executeUpdate("CREATE TABLE PatientClassRelationship " +
                        "(pid INTEGER REFERENCES Patient(pid), cid INTEGER REFERENCES PatientClass(cid), " +
                        "PRIMARY KEY (pid, cid))");

                myConn.stmt.executeUpdate("CREATE TABLE ObservationCategory " +
                        "(ocid INTEGER PRIMARY KEY, name VARCHAR2(25))");

                myConn.stmt.executeUpdate("CREATE TABLE ObservationType " +
                        "(otid INTEGER PRIMARY KEY, name VARCHAR2(25), ocid INTEGER REFERENCES ObservationCategory(ocid))");

                myConn.stmt.executeUpdate("CREATE TABLE ObservationQuestions " +
                        "(qid INTEGER PRIMARY KEY, text VARCHAR2(50), otid INTEGER REFERENCES ObservationType(otid))");

                myConn.stmt.executeUpdate("CREATE TABLE Observation " +
                        "(pid INTEGER REFERENCES Patient(pid), otid INTEGER REFERENCES ObservationType(otid), " +
                        "obvTimestamp TIMESTAMP(2), recTimestamp TIMESTAMP(2), " +
                        "qid INTEGER REFERENCES ObservationQuestions(qid), answer VARCHAR2(20), " +
                        "PRIMARY KEY (pid, otid, qid, obvTimestamp) )");

//		stmt.executeUpdate("INSERT INTO COFFEES " +
//			   "VALUES ('Colombian', 101, 7.99, 0, 0)");

                rs = myConn.stmt.executeQuery("select table_name from user_tables");

                while (rs.next()) {
                    System.out.println(rs.getString("table_name") + "   ");
                }

            } finally {
                close(rs);
                if (myConn != null) {
                    myConn.closeStatement();
                    myConn.closeConnection();
                }
            }
        } catch (Throwable oops) {
            oops.printStackTrace();
        }
    }

    static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable whatever) {
            }
        }
    }
}

