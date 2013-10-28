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
            String query = "SELECT COALESCE(MAX(otid), 0) as otid from ObservationType";
            ResultSet resultSet = connection.stmt.executeQuery(query);
            while (resultSet.next())
                seqNum = resultSet.getInt("otid") + 1;
        }
    }

    static void insertForCategory(String obsName, String catName, MyConnection connection) {
        int ocid = ObservationCategory.getCategoryIdByName(catName, connection);
        if (ocid != 0)
            insert(obsName, ocid, connection);
        else
            System.out.println("Could not get " + catName + " category");
    }
}
