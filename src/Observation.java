import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Observation {

    int pid;
    int otid;
    Date obvTimestamp;
    Date recTimestamp;
    int qid;
    String answer;

    private Observation(int pid, int otid, Date obvTimestamp, Date recTimestamp, int qid, String answer) {
        this.pid = pid;
        this.otid = otid;
        this.obvTimestamp = obvTimestamp;
        this.recTimestamp = recTimestamp;
        this.qid = qid;
        this.answer = answer;
    }

    static Observation getById(int pid, int otid, int qid, Date obvTimestamp, MyConnection conn) throws MyException {
        Timestamp obvTime = new Timestamp(obvTimestamp.getTime());

        try {
            String query = "select * from Observation where pid = ? AND otid = ? AND qid = ? AND obvTimestamp = ?";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, pid);
            pstmt.setInt(2, otid);
            pstmt.setInt(3, qid);
            pstmt.setTimestamp(4, obvTime);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
                return new Observation(rs.getInt("pid"), rs.getInt("otid"), rs.getTimestamp("obvTimestamp"),
                        rs.getTimestamp("recTimestamp"), rs.getInt("qid"), rs.getString("answer"));

        } catch (SQLException e) {
            throw new MyException("Could not get observation for pid = " + pid + " otid = " +
                    otid + " qid = " + qid + " obsTimestamp = " + obvTimestamp);
        }
        return null;
    }

    static int insert(int pid, int otid, Date obvTimestamp, Date recTimestamp, int qid, String answer, MyConnection conn) throws MyException {
        Timestamp obvTime = new Timestamp(obvTimestamp.getTime());
        Timestamp recTime = new Timestamp(recTimestamp.getTime());

        try {
            String query = "INSERT INTO Observation values(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, pid);
            pstmt.setInt(2, otid);
            pstmt.setTimestamp(3, obvTime);
            pstmt.setTimestamp(4, recTime);
            pstmt.setInt(5, qid);
            pstmt.setString(6, answer);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return pid;

        } catch (SQLException e) {
            throw new MyException("Insertion of observation failed for pid = " + pid);
        }
        return 0;
    }

    static List<Observation> filter(int pid, int otid, Date beginDate, Date endDate, MyConnection conn) throws MyException {
        List<Observation> observations = new ArrayList<Observation>();
        Timestamp beginTimestamp = new Timestamp(beginDate.getTime());
        Timestamp endTimestamp = new Timestamp(endDate.getTime());

        try {
            String query = "select * from Observation where pid = ? AND otid = ? AND obvTimestamp between ? and ?";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, pid);
            pstmt.setInt(2, otid);
            pstmt.setTimestamp(3, beginTimestamp);
            pstmt.setTimestamp(4, endTimestamp);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                observations.add(Observation.getById(pid, otid, rs.getInt("qid"), rs.getTimestamp("obvTimestamp"), conn));

            return observations;

        } catch (SQLException e) {
            throw new MyException("Filtering of observation failed for pid = " + pid);
        }
    }

    static List<Observation> getAllObservations(MyConnection connection) throws MyException {
        List<Observation> observations = new ArrayList<Observation>();
        try {
            String query = "SELECT * FROM Observation";
            ResultSet rs = connection.stmt.executeQuery(query);
            while (rs.next())
                observations.add(new Observation(rs.getInt(1),rs.getInt(2), rs.getTimestamp(3),
                        rs.getTimestamp(4),rs.getInt(5),rs.getString(6)));
        } catch (SQLException e) {
            throw new MyException("Error in retrieving Observation Categories");
        }
        return observations;
    }


}
