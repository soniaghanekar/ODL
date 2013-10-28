import java.sql.*;
import java.util.Date;

public class Patient {
    static int seqNum;

    static {
        seqNum = 0;
    }

    int pid;
    Date dob;
    String name;
    String address;
    String sex;
    String publicStatus;
    String password;

    private Patient(int pid, Date dob, String name, String address, String sex, String publicStatus, String password) {
        this.pid = pid;
        this.dob = dob;
        this.name = name;
        this.address = address;
        this.sex = sex;
        this.publicStatus = publicStatus;
        this.password = password;
    }
    private static void setSeqNum(MyConnection connection) throws SQLException {
        if(seqNum == 0) {
            String query = "SELECT MAX(pid) as pid from Patient";
            ResultSet resultSet = connection.stmt.executeQuery(query);
            while (resultSet.next())
                seqNum = resultSet.getInt("pid") + 1;
        }
    }


    static Patient getById(int pid, MyConnection conn) {
        try {
            String query = "select * from patient where pid = " + pid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next()) {
                return new Patient(rs.getInt("pid"), rs.getDate("dob"), rs.getString("name"), rs.getString("address"),
                        rs.getString("sex"), rs.getString("publicStatus"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int insert(Date dob, String name, String address, String sex, String publicStatus, String password, MyConnection conn) {
        java.sql.Date longDOB = new java.sql.Date(dob.getTime());
        try {
            setSeqNum(conn);
            String query = "INSERT INTO patient values(?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, seqNum);
            pstmt.setDate(2, longDOB);
            pstmt.setString(3, name);
            pstmt.setString(4, address);
            pstmt.setString(5, sex);
            pstmt.setString(6, publicStatus);
            pstmt.setString(7, password);
            int ret = pstmt.executeUpdate();

            if (ret != 0)
                return seqNum++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
