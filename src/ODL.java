import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ODL {

    public static void main(String[] args) {
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            MyConnection myConn = null;
            ResultSet rs = null;
            boolean shouldContinue = true;
            char choice;
            Scanner input = new Scanner(System.in);

            try {
                myConn = new MyConnection();
                while (shouldContinue) {
                    System.out.println("1. Register a Patient");
                    System.out.println("2. Exit System");
                    choice = input.nextLine().charAt(0);

                    switch (choice) {
                        case '1':
                            System.out.println("Please enter patients name");
                            String name = input.nextLine();
                            System.out.println("Please enter patients address");
                            String address = input.nextLine();
                            System.out.println("Please enter patients date of birth in mm dd yyyy format eg. 03 08 1988");
                            String dob = input.nextLine();
                            System.out.println("Please enter patients sex(m/f)");
                            String sex = input.nextLine();
                            System.out.println("Do you want your profile to be public? (y/n)");
                            String publicStatus = input.nextLine();

                            if (checkArgumentsForPatient(dob, sex, publicStatus)) {
                                int patientId = Patient.insert(getDateFromString(dob), name, address, sex.toLowerCase(),
                                        publicStatus.toLowerCase(), myConn);
                                if (patientId > 0) {
                                    System.out.println("A patient has been created with id " + patientId + ".");
                                    System.out.println("Please remember this id as this will be used to login next time");
                                } else {
                                    System.out.println("We were not able to create the patient. Please try again later");
                                }
                            }
                            break;

                        case '2':
                            System.out.println("Thanks for using our system.. Have a nice day..");
                            System.out.println("Bye!!");
                            shouldContinue = false;
                            break;

                        default:
                            System.out.println("Please Select An Option From The Allowed Values");
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
            oops.printStackTrace();
        }
    }

    private static Date getDateFromString(String dob) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM d yyyy", Locale.ENGLISH);
        formatter.setLenient(false);
        return formatter.parse(dob);
    }

    private static boolean checkArgumentsForPatient(String dob, String sex, String publicStatus) {
        try {
            getDateFromString(dob);
        } catch (Exception e) {
            System.out.println("Please Enter date in MM dd yyyy format example 03 08 1988");
            return false;
        }
        if (sex.length() != 1 || !(sex.toLowerCase().equals("m") || sex.toLowerCase().equals("f"))) {
            System.out.println("Please Enter Sex properly. The only available options are m or f");
            return false;
        }
        if (publicStatus.length() != 1 || !(publicStatus.toLowerCase().equals("y") || publicStatus.toLowerCase().equals("n"))) {
            System.out.println("Please Enter Public Status properly. The only available options are y or n");
            return false;
        }
        return true;
    }

    static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable whatever) {
                whatever.printStackTrace();
            }
        }
    }
}
