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
                    System.out.println("2. Login as a Patient");
                    System.out.println("3. Exit System");
                    choice = input.nextLine().charAt(0);

                    switch (choice) {
                        case '1':
                            registerPatient(myConn, input);
                            break;

                        case '2':
                            loginAsPatient(myConn, input);
                            break;

                        case '3':
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

    private static void loginAsPatient(MyConnection myConn, Scanner input) {
        System.out.println("Enter patient id: ");
        String patientId = input.nextLine();

        Patient patient = Patient.getById(Integer.parseInt(patientId), myConn);
        if(patient != null) {
            boolean logout = false;
            char choice;

            while(!logout) {
                System.out.println("1. Enter Data");
                System.out.println("2. Logout");
                choice = input.nextLine().charAt(0);

                switch(choice) {

                    case '1':
                        enterData(Integer.parseInt(patientId), myConn, input);
                        break;

                    case '2':
                        logout = true;
                        System.out.println("You have been successfully logged out");
                        break;

                    default:
                        System.out.println("Please Select An Option From The Allowed Values");

                }
            }
        }
        else
            System.out.println("Patient with id " + patientId + " does not exist");
    }

    private static void enterData(int patientId, MyConnection conn, Scanner input) {
        char choice;
        boolean shouldContinue = true;
        while(shouldContinue) {
            System.out.println("1. Enter new observation data");
            System.out.println("2. Add a new observation type");
            System.out.println("3. Go to patient login homepage");
            choice = input.nextLine().charAt(0);

            switch(choice) {
                case '1':
                    enterObservations(patientId, conn, input);
                case '3':
                    shouldContinue = false;
                    break;

                default:
                    System.out.println("Please Select An Option From The Allowed Values");
            }
        }
    }

    private static void enterObservations(int patientId, MyConnection conn, Scanner input) {
        Date obvDate = getObservationDate(input);
        Date recordDate = new Date();

        System.out.println("Please answer the following questions: ");
        List<Integer> cids = PatientClassRelationship.getClassesForPatient(patientId, conn);
        for(Integer cid : cids) {
            List<Integer> otids = PatientClassObservationTypeMapper.getByClass(cid, conn);

            for (Integer otid: otids) {
                ObservationType type = ObservationType.getById(otid, conn);
                System.out.println("Observation Type: " + type.name);
                List<ObservationQuestions> questions = ObservationQuestions.getByObservationType(otid, conn);

                for(ObservationQuestions question: questions) {
                    System.out.println(question.text);
                    String answer = input.nextLine();
                    System.out.println(" quesid = " + question.qid + " answer = " + answer);
                    Observation.insert(patientId, otid, obvDate, recordDate, question.qid, answer, conn);
                }
            }
        }
    }

    private static Date getObservationDate(Scanner input) {
        try {
            System.out.println("Enter date and time of the observation (in MM/dd/yyyy hh:mm AM/PM ex: 10/04/2013 10:15 AM): ");
            String date = input.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date obvDate = dateFormat.parse(date);
            return obvDate;

        } catch (ParseException e) {
            System.out.println("Please enter date and time in proper format ex: 10/04/2013 10:15 AM");
        }
        return null;
    }

    private static void registerPatient(MyConnection myConn, Scanner input) throws ParseException {
        System.out.println("Please enter patients name:");
        String name = input.nextLine();
        System.out.println("Please enter patients address:");
        String address = input.nextLine();
        System.out.println("Please enter patients date of birth in mm/dd/yyyy format eg. 03/08/1988:");
        String dob = input.nextLine();
        System.out.println("Please enter patients sex(m/f):");
        String sex = input.nextLine();
        System.out.println("Do you want your profile to be public? (y/n):");
        String publicStatus = input.nextLine();

        if (checkArgumentsForPatient(dob, sex, publicStatus)) {
            int patientId = Patient.insert(getDateFromString(dob), name, address, sex.toLowerCase(),
                    publicStatus.toLowerCase(), myConn);
            if (patientId > 0) {
                System.out.println("A patient has been created with id " + patientId + ".");
                System.out.println("Please remember this id as this will be used to login next time");
                PatientClassRelationship.insertAsGeneral(patientId, myConn);

            } else {
                System.out.println("We were not able to create the patient. Please try again later");
            }
        }
    }

    private static Date getDateFromString(String dob) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
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
