package su.systserv.smartBuoy.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 12.05.14
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */
public interface DBConnectorIF {

    /**
     * Connect to database
     * @param host - network host's name
     * @param port - tcp-port
     * @param dbName - name of Database
     * @param user - username
     * @param password - password
     * @return
     */
    Connection getConnection(String host, int port, String dbName, String user, String password) throws SQLException;


}
