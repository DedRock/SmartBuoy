package su.systserv.smartBuoy.database.exception;

/**
 * Исключение, возникающее при невозможности подключиться к базе данных
 */
public class DbConnectException extends Exception {
    public DbConnectException(String message) {
        super(message);
    }
}
