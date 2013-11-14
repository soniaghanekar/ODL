import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Time;
import java.util.List;


public class GenerateAlerts {
    public static void main(String[] args) {

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            MyConnection myConn = null;
            ResultSet rs = null;

            try {
                myConn = new MyConnection();
                List<Observation> observations = Observation.getAllObservations(myConn);
                for( Observation observation : observations){
                    try{
                        if(ObservationThreshold.crossesThreshold(observation.qid, Integer.parseInt(observation.answer), myConn))
                            addThresholdAlert(observation.pid, observation.qid, myConn);
                    } catch (NumberFormatException e){
                        return;
                    }
                }
            } finally {
                close(rs);
                if (myConn != null) {
                    myConn.closeStatement();
                    myConn.closeConnection();
                }
            }
        } catch (Throwable oops) {
            if (oops instanceof MyException)
                System.out.println(((MyException) oops).message);
            oops.printStackTrace();
        }


    }

    private static void addThresholdAlert(int patientId, int qid, MyConnection myConn) throws MyException {
        String message = "ALERT: Your values for the question: " + qid +
                " crosses the threshold values";
        Alert.insert(patientId, message, "0", new Date(), myConn);
    }

    static void close(ResultSet rs) {
        if (rs != null) try {
            rs.close();
        } catch (Throwable whatever) {
            whatever.printStackTrace();
        }
    }

}
