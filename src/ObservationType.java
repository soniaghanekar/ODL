import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObservationType {
    static int seqNum;

    static {
        seqNum = 0;
    }

    int otid;
    String name;
    int ocid;

    private ObservationType(int otid, String name, int ocid) {
        this.otid = otid;
        this.name = name;
        this.ocid = ocid;
    }

    static ObservationType getById(int otid, MyConnection conn) {
        try {
            String query = "select * from ObservationType where otid = " + otid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new ObservationType(rs.getInt("otid"), rs.getString("name"), rs.getInt("ocid"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int getIdFromName(String name, MyConnection conn) {
        try {
            String query = "select otid from ObservationType where name = '" + name + "'";
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return rs.getInt("otid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int insert(String name, int ocid, MyConnection conn) {
        try {
            setSeqNum(conn);
            String query = "INSERT INTO ObservationType values(?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, seqNum);
            pstmt.setString(2, name);
            pstmt.setInt(3, ocid);
            int ret = pstmt.executeUpdate();

            if (ret != 0)
                return seqNum++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void setSeqNum(MyConnection connection) throws SQLException {
        if(seqNum == 0) {
            String query = "SELECT MAX(otid) as otid from ObservationType";
            ResultSet resultSet = connection.stmt.executeQuery(query);
            while (resultSet.next())
                seqNum = resultSet.getInt("otid") + 1;
        }
    }

    static void insertBehavioralObservationType(String name, MyConnection conn) {
        int ocid = ObservationCategory.getBehavioralCategoryId(conn);
        if (ocid != 0)
            insert(name, ocid, conn);
    }

    static void insertPhysiologicalObservationType(String name, MyConnection conn) {
        int ocid = ObservationCategory.getPhysiologicalCategoryId(conn);
        if (ocid != 0)
            insert(name, ocid, conn);
    }

    static void insertPsychologicalObservationType(String name, MyConnection conn) {
        int ocid = ObservationCategory.getPsychologicalCategoryId(conn);
        if (ocid != 0)
            insert(name, ocid, conn);
    }

    static void insertGeneralObservationType(String name, MyConnection conn) {
        int ocid = ObservationCategory.getGeneralCategoryId(conn);
        if (ocid != 0)
            insert(name, ocid, conn);
    }
}
