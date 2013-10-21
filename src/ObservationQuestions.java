import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObservationQuestions {
    static int seqNum;

    static {
        seqNum = 1;
    }

    int qid;
    String text;
    int otid;

    private ObservationQuestions(int qid, String text, int otid) {
        this.qid = qid;
        this.text = text;
        this.otid = otid;
    }

    static ObservationQuestions getById(int qid, MyConnection conn) {
        try {
            String query = "select * from ObservationQuestions where qid = " + qid;
            ResultSet rs = conn.stmt.executeQuery(query);
            while (rs.next())
                return new ObservationQuestions(rs.getInt("otid"), rs.getString("text"), rs.getInt("otid"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int insert(String text, int otid, MyConnection conn) {
        try {
            String query = "INSERT INTO ObservationQuestions values(?,?,?)";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, seqNum);
            pstmt.setString(2, text);
            pstmt.setInt(3, otid);
            int ret = pstmt.executeUpdate();

            if(ret != 0)
                return seqNum++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
