import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientClassRelationship {

    int pid;
    int cid;

    private PatientClassRelationship(int pid, int cid) {
        this.pid = pid;
        this.cid = cid;
    }

    static PatientClassRelationship getById(int pid, int cid, MyConnection conn) throws MyException {
        try {
            String query = "select * from PatientClassRelationship where pid = " + pid + " AND cid = " + cid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new PatientClassRelationship(rs.getInt("pid"), rs.getInt("cid"));

        } catch (SQLException e) {
            throw new MyException("Could not get class for patient with id = " + pid);
        }
        return null;
    }

    static List<Integer> getClassesForPatient(int pid, MyConnection conn) throws MyException {
        try {
            String query = "select * from PatientClassRelationship where pid = " + pid;
            ResultSet rs = conn.stmt.executeQuery(query);
            List<Integer> ids = new ArrayList<Integer>();
            while (rs.next())
                ids.add(rs.getInt("cid"));
            return ids;
        } catch (SQLException e) {
            throw new MyException("Could not get classes for patient with id = " + pid);
        }
    }

    static int insert(int pid, int cid, MyConnection conn) throws MyException {
        try {
            String query = "INSERT INTO PatientClassRelationship values(?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, pid);
            pstmt.setInt(2, cid);
            int ret = pstmt.executeUpdate();

            if (ret != 0)
                return pid;

        } catch (SQLException e) {
            throw new MyException("Insertion of class for patient with id " + pid + " failed");
        }
        return 0;
    }

    public static int insertAsGeneral(int patientId, MyConnection conn) throws MyException {
        int cid = PatientClass.getIdByName("General", conn);
        if (cid != 0) {
            insert(patientId, cid, conn);
            return patientId;
        }
        return 0;
    }
}
