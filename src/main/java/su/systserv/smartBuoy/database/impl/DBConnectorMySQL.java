package su.systserv.smartBuoy.database.impl;

import org.apache.log4j.Logger;
import su.systserv.smartBuoy.database.DBConnectorIF;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс с доступом в пределах пакета.
 * Работать с ним можно только через интерфейс DBConnectorIF
 * Экземпляр класса следует создавать через фабрику DbConnectorFactory
 */
class DBConnectorMySQL implements DBConnectorIF {
    private static final Logger log = Logger.getLogger(DBConnectorMySQL.class.getName());
    private final String ConnectorClass = "com.mysql.jdbc.Driver";

    @Override
    public Connection getConnection(String host, int port, String dbName, String user, String password) throws SQLException {
        Connection result = null;
        try {
            Class.forName(ConnectorClass);
            result = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbName, user, password);
        } catch (ClassNotFoundException e) {
            log.error("Не найден класс: " + ConnectorClass);
        }
        return result;
    }
}


