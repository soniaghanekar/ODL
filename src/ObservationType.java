import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObservationType {
    static int seqNum;

    static {
        seqNum = 1;
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

    static int insert(String name, int ocid, MyConnection conn) {
        try {
            String query = "INSERT INTO ObservationType values(?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, seqNum);
            pstmt.setString(2, name);
            pstmt.setInt(3, ocid);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return seqNum++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
