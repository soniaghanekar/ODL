import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PatientClass {
    static int seqNum;

    static {
        seqNum = 0;
    }

    int cid;
    String name;

    private PatientClass(int cid, String name) {
        this.cid = cid;
        this.name = name;
    }

    private static void setSeqNum(MyConnection connection) throws SQLException {
        if(seqNum == 0) {
            String query = "SELECT COALESCE(MAX(cid), 0) as cid from PatientClass";
            ResultSet resultSet = connection.stmt.executeQuery(query);
            while (resultSet.next())
                seqNum = resultSet.getInt("cid") + 1;
        }
    }

    static PatientClass getById(int cid, MyConnection conn) {
        try {
            String query = "select * from PatientClass where cid = " + cid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new PatientClass(rs.getInt("cid"), rs.getString("name"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int getIdByName(String name, MyConnection conn) {
        try {
            String query = "select cid from PatientClass where name = '" + name + "'";
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return rs.getInt("cid");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int insert(String name, MyConnection conn) {
        try {
            setSeqNum(conn);
            String query = "INSERT INTO PatientClass values(?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, seqNum);
            pstmt.setString(2, name);
            int ret = pstmt.executeUpdate();

            if (ret != 0)
                return seqNum++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int getIdForGeneralClass(MyConnection conn) {
        try {
            String query = "SELECT cid from PatientClass WHERE name = 'General'";
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return rs.getInt("cid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
