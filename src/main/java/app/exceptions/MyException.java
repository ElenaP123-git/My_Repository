package app.exceptions;

public class MyException extends Exception {

    public MyException(String mensaje) {
        super(mensaje);
    }

    public MyException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}