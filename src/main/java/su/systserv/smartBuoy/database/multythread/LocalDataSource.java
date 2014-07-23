package su.systserv.smartBuoy.database.multythread;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 02.06.14
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class LocalDataSource {

    private String CONNECTION_DRIVER = "com.mysql.jdbc.Driver";
    private String DATABASE_URL = "jdbc:mysql://localhost:3306/smartbuoy";
    private String DATABASE_USERNAME = "root";
    private String DATABASE_PASSWORD = "password";


    volatile private static LocalDataSource datasource;
    private ComboPooledDataSource cpds;

    private LocalDataSource() throws IOException, SQLException, PropertyVetoException {

        cpds = new ComboPooledDataSource();
        cpds.setDriverClass(CONNECTION_DRIVER); //loads the jdbc driver
        cpds.setJdbcUrl(DATABASE_URL);
        cpds.setUser(DATABASE_USERNAME);
        cpds.setPassword(DATABASE_PASSWORD);

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);
    }

    public static LocalDataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new LocalDataSource();
        }
        return datasource;
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

}
