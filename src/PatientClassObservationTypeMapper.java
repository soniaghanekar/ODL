import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientClassObservationTypeMapper {

    int cid;
    int otid;

    private PatientClassObservationTypeMapper(int cid, int otid) {
        this.cid = cid;
        this.otid = otid;
    }

    static List<Integer> getByClass(int cid, MyConnection conn) throws MyException {
        try {
            String query = "select * from PatientClassObvTypeMapper where cid = " + cid;
            ResultSet rs = conn.stmt.executeQuery(query);
            List<Integer> typeIds = new ArrayList<Integer>();
            while (rs.next())
                typeIds.add(rs.getInt("otid"));
            return typeIds;

        } catch (SQLException e) {
            throw new MyException("Could not get observation types for patient class id  " + cid);
        }
    }

    static int insert(int cid, int otid, MyConnection conn) throws MyException {
        try {
            String query = "INSERT INTO PatientClassObvTypeMapper values(?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, cid);
            pstmt.setInt(2, otid);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return cid;

        } catch (SQLException e) {
            throw new MyException("Insertion for mapping of patient class and observation type failed for class id " + cid);
        }
        return 0;
    }

    static int insertByClassNameAndTypeName(String cname, String tname, MyConnection conn) throws MyException {
        int cid = PatientClass.getIdByName(cname, conn);
        int otid = ObservationType.getIdFromName(tname, conn);
        if(insert(cid, otid, conn) != 0)
            return cid;
        return 0;
    }

    static PatientClassObservationTypeMapper getById(int cid, int otid, MyConnection conn) throws MyException {
        try {
            String query = "select * from PatientClassObvTypeMapper where cid = " + cid + " AND otid = " + otid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new PatientClassObservationTypeMapper(rs.getInt(1), rs.getInt(2));
        } catch (SQLException e) {
            throw new MyException("Error In retrieving Patient Class to Observation type mapper");
        }
        return null;
    }

}
