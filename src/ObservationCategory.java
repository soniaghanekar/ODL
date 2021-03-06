import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObservationCategory {
    static int seqNum;

    static {
        seqNum = 0;
    }

    int ocid;
    String name;

    private ObservationCategory(int ocid, String name) {
        this.ocid = ocid;
        this.name = name;
    }

    private static void setSeqNum(MyConnection connection) throws SQLException {
        if(seqNum == 0) {
            String query = "SELECT COALESCE(MAX(ocid), 0) as ocid from ObservationCategory";
            ResultSet resultSet = connection.stmt.executeQuery(query);
            while (resultSet.next())
                seqNum = resultSet.getInt("ocid") + 1;
        }
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

    static int getCategoryIdByName(String name, MyConnection connection) throws MyException {
        try {
            String query = "SELECT ocid FROM ObservationCategory WHERE name = '" + name + "'";
            ResultSet rs = connection.stmt.executeQuery(query);
            while (rs.next())
                return rs.getInt("ocid");
        } catch (SQLException e) {
            throw new MyException("Could not get category for " + name);
        }
        return 0;
    }

    static List<ObservationCategory> getAllCategories(MyConnection connection) throws MyException {
        List<ObservationCategory> categories = new ArrayList<ObservationCategory>();
        try {
            String query = "SELECT * FROM ObservationCategory";
            ResultSet rs = connection.stmt.executeQuery(query);
            while (rs.next())
                categories.add(new ObservationCategory(rs.getInt("ocid"), rs.getString("name")));
        } catch (SQLException e) {
            throw new MyException("Error in retrieving Observation Categories");
        }
        return categories;
    }

    static int insert(String name, MyConnection conn) throws MyException {
        try {
            setSeqNum(conn);
            String query = "INSERT INTO ObservationCategory values(?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, seqNum);
            pstmt.setString(2, name);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return seqNum++;

        } catch (SQLException e) {
            throw new MyException("Insertion of category failed for " + name);
        }
        return 0;
    }
}
