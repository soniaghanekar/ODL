import java.sql.*;

public class MyConnection {
    static final String jdbcURL
            = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";

    Connection conn;
    Statement stmt;

    public MyConnection() {
        String user = "ssghanek";	// For example, "jsmith"
        String passwd = "qweasd";	// Your 9 digit student ID number
        try {
            this.conn = DriverManager.getConnection(jdbcURL, user, passwd);
            this.stmt = conn.createStatement();
            stmt.execute("alter session set nls_comp=ansi");
            stmt.execute("alter session set nls_sort=binary_ci");
            stmt.execute("alter session set nls_comp=linguistic");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    public void closeStatement() {
        if(stmt != null) {
            try { stmt.close(); } catch(Throwable whatever) {}
        }
    }


}
