package su.systserv.smartBuoy.database.exception;

/**
 * Исключение, возникающее, когда фабрика DbConnectorFactory не выдаёт (null) экземпляр класса, реализующего интерфейс DBConnectorIF
 */
public class DBConnectFactoryException extends Exception {

    public DBConnectFactoryException(String message) {
        super(message);
    }
}
