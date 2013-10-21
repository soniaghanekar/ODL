import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObservationCategory {
    static int seqNum;

    static {
        seqNum = 1;
    }

    int ocid;
    String name;

    private ObservationCategory(int ocid, String name) {
        this.ocid = ocid;
        this.name = name;
    }

    static ObservationCategory getById(int ocid, MyConnection conn) {
        try {
            String query = "select * from ObservationCategory where ocid = " + ocid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new ObservationCategory(rs.getInt("ocid"), rs.getString("name"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int insert(int pid, String name, MyConnection conn) {
        try {
            String query = "INSERT INTO ObservationCategory values(?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, seqNum);
            pstmt.setString(2, name);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return seqNum++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
