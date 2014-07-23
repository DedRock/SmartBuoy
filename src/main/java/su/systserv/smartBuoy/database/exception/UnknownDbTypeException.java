package su.systserv.smartBuoy.database.exception;

/**
 * Исключение, возникающее при невозможности подключиться к базе данных
 */
public class UnknownDbTypeException extends Exception {
    public UnknownDbTypeException(String message) {
        super(message);
    }
}
