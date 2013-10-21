import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientClassRelationship {

    int pid;
    int cid;

    private PatientClassRelationship(int pid, int cid) {
        this.pid = pid;
        this.cid = cid;
    }

    static PatientClassRelationship getById(int pid, int cid, MyConnection conn) {
        try {
            String query = "select * from PatientClassRelationship where pid = " + pid + " AND cid = " + cid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new PatientClassRelationship(rs.getInt("pid"), rs.getInt("cid"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int insert(int pid, int cid, MyConnection conn) {
        try {
            String query = "INSERT INTO PatientClassRelationship values(?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, pid);
            pstmt.setInt(2, cid);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return pid;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
