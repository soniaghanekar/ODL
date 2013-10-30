public class MyException extends Exception {
    String message = null;

    public MyException(String message) {
        this.message = message;
    }
}
