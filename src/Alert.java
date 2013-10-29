import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alert {

    int pid;
    String text;
    String viewed;
    Date timestamp;

    private Alert(int pid, String text, String viewed, Date timestamp) {
        this.pid = pid;
        this.text = text;
        this.viewed = viewed;
        this.timestamp = timestamp;
    }

    static Alert getById(int pid, String text, MyConnection conn) {
        try {
            String query = "select * from alert where pid = " + pid + " AND text = " + text;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next()) {
                return new Alert(rs.getInt("pid"), rs.getString("text"), rs.getString("viewed"), rs.getDate("timestamp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static List<Alert> getByPId(int pid, MyConnection conn) {
        List<Alert> alertList = new ArrayList<Alert>();
        try {
            String query = "select * from alert where pid = " + pid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next()) {
                alertList.add(new Alert(rs.getInt("pid"), rs.getString("text"), rs.getString("viewed"), rs.getDate("timestamp")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alertList;
    }

    static void insert(int pid, String text, String viewed, Date timestamp, MyConnection conn) {
        Timestamp longTimestamp = new Timestamp(timestamp.getTime());
        try {
            String query = "INSERT INTO alert values(?,?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, pid);
            pstmt.setString(2, text);
            pstmt.setString(3, viewed);
            pstmt.setTimestamp(4, longTimestamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void markViewed(MyConnection conn) throws SQLException {
        this.viewed = "1";
        String query = "UPDATE alert SET viewed = '1' where pid = "+this.pid+" AND text = " + this.text;
        conn.stmt.executeUpdate(query);
    }


}
