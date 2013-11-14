import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    static ObservationType getById(int otid, MyConnection conn) throws MyException {
        try {
            String query = "select * from ObservationType where otid = " + otid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new ObservationType(rs.getInt("otid"), rs.getString("name"), rs.getInt("ocid"));

        } catch (SQLException e) {
            throw new MyException("Could not get observation type for otid = " + otid);
        }
        return null;
    }

    static int getIdFromName(String name, MyConnection conn) throws MyException {
        try {
            String query = "select otid from ObservationType where name = '" + name + "'";
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return rs.getInt("otid");
        } catch (SQLException e) {
            throw new MyException("Could not get observation type with name " + name);
        }
        return 0;
    }

    static int insert(String name, int ocid, MyConnection conn) throws MyException {
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
            throw new MyException("Insertion for observation type with name " + name + " failed");
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

    static int insertForCategory(String obsName, String catName, MyConnection connection) throws MyException {
        int ocid = ObservationCategory.getCategoryIdByName(catName, connection);
        if (ocid != 0)
            return insert(obsName, ocid, connection);
        return 0;
    }

    public static List<ObservationType> getAllTypes(MyConnection myConn) throws MyException {
        List<ObservationType> types = new ArrayList<ObservationType>();
        String query = "SELECT * from ObservationType";
        try {
            ResultSet resultSet = myConn.stmt.executeQuery(query);
            while (resultSet.next())
                types.add(new ObservationType(resultSet.getInt("otid"), resultSet.getString("name"),
                        resultSet.getInt("ocid")));

        } catch (SQLException e) {
            throw new MyException("Error in retrieving Observation Types");
        }
        return types;
    }

    public void updateCategory(int ocid, MyConnection myConnection) throws MyException {
        String query = "UPDATE ObservationType set ocid = " + ocid +" where otid = " + this.otid;
        try {
            myConnection.stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new MyException("Error in updating Observation Type with name " + this.name);

        }
    }

    public void updatePatientClassMapping(Integer cid, MyConnection myConn) throws MyException {
        PatientClassObservationTypeMapper isExisting= PatientClassObservationTypeMapper.getById(cid, this.otid, myConn);
        if(isExisting == null){
            PatientClassObservationTypeMapper.insert(cid, this.otid, myConn);
            System.out.println("Successfully added Observation Type to the Patient Class.");
            return;
        }
        System.out.println("Observation Type already belongs to this class.");
    }
}
