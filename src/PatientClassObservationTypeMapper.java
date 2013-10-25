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

    static List<Integer> getByClass(int cid, MyConnection conn) {
        try {
            String query = "select * from PatientClassObvTypeMapper where cid = " + cid;
            ResultSet rs = conn.stmt.executeQuery(query);
            List typeIds = new ArrayList();
            while (rs.next())
                typeIds.add(rs.getInt("otid"));
            return typeIds;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int insert(int cid, int otid, MyConnection conn) {
        try {
            String query = "INSERT INTO PatientClassObvTypeMapper values(?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, cid);
            pstmt.setInt(2, otid);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return cid;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int insertByClassNameAndTypeName(String cname, String tname, MyConnection conn) {
        int cid = PatientClass.getIdByName(cname, conn);
        int otid = ObservationType.getIdFromName(tname, conn);
        if(insert(cid, otid, conn) != 0)
            return cid;
        return 0;
    }
}
