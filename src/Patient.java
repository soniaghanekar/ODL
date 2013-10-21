import java.sql.*;
import java.util.Date;

public class Patient {
    static int seqNum;

    static {
        seqNum = 1;
    }

    int pid;
    Date dob;
    String name;
    String address;
    String sex;
    String publicStatus;

    private Patient(int pid, Date dob, String name, String address, String sex, String publicStatus) {
        this.pid = pid;
        this.dob = dob;
        this.name = name;
        this.address = address;
        this.sex = sex;
        this.publicStatus = publicStatus;
    }

    static Patient getById(int pid, MyConnection conn) {
        try {
            String query = "select * from patient where pid = " + pid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next()) {
                return new Patient(rs.getInt("pid"), rs.getDate("dob"), rs.getString("name"), rs.getString("address"),
                        rs.getString("sex"), rs.getString("publicStatus"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int insert(Date dob, String name, String address, String sex, String publicStatus, MyConnection conn) {
        try {
            String query = "INSERT INTO patient values(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, seqNum);
            pstmt.setDate(2, (java.sql.Date) dob);
            pstmt.setString(3, name);
            pstmt.setString(4, address);
            pstmt.setString(5, sex);
            pstmt.setString(6, publicStatus);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return seqNum++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
