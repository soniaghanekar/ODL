import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthFriend {

    int pid;
    int fid;
    Date timestamp;

    private HealthFriend(int pid, int fid, Date timestamp) {
        this.pid = pid;
        this.fid = fid;
        this.timestamp = timestamp;
    }

    static HealthFriend getById(int pid, int fid, MyConnection conn) throws MyException {
        try {
            String query = "select * from HealthFriend where pid = " + pid + " AND fid = " + fid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new HealthFriend(rs.getInt("pid"), rs.getInt("fid"), rs.getTimestamp("timestamp"));

        } catch (SQLException e) {
            throw new MyException("Could not get class for patient with id = " + pid);
        }
        return null;
    }

    static List<Integer> getFriendsOfPatient(int pid, MyConnection conn) throws MyException {
        try {
            String query = "select * from HealthFriend where pid = " + pid;
            ResultSet rs = conn.stmt.executeQuery(query);
            List<Integer> ids = new ArrayList<Integer>();
            while (rs.next())
                ids.add(rs.getInt("fid"));
            return ids;
        } catch (SQLException e) {
            throw new MyException("Could not get friends for patient with id = " + pid);
        }
    }

    static int insert(int pid, int fid, MyConnection conn) throws MyException {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        try {
            String query = "INSERT INTO HealthFriend values(?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, pid);
            pstmt.setInt(2, fid);
            pstmt.setTimestamp(3, timestamp);
            int ret = pstmt.executeUpdate();

            if (ret != 0)
                return pid;

        } catch (SQLException e) {
            throw new MyException("Insertion of friends for patient with id " + pid + " failed");
        }
        return 0;
    }
}
