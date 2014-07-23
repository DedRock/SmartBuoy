package su.systserv.smartBuoy.dkart.dbExchange.connector;

import su.systserv.smartBuoy.database.DBConnectorIF;
import su.systserv.smartBuoy.database.exception.DbConnectException;
import su.systserv.smartBuoy.database.impl.DbConnectorFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс описания настроек и интерфейса взаимодействия с БД программы dKart
 */
public class CruiseDBConnector {

    static CruiseDBConnector singletonInstance;

    String DB_HOST_NAME = "AFRIKANOV\\SQLEXPRESS";
    int DB_TCP_PORT = 1433;
    String DB_NAME = "AISDB";
    String DB_USERNAME = "odu";
    String DB_PASSWORD = "odu";

    //private DBConnectorIF connector;
    //private Connection connection;
    private Statement statement;

    /**
     * Метод возвращающий единственный экземпляр данного класса (singleton)
     * @return экземпляр CruiseDBConnector
     * @throws DbConnectException
     */
    public static CruiseDBConnector getInstance() throws DbConnectException{
        if (singletonInstance == null){
            singletonInstance = new CruiseDBConnector();
        }
        return singletonInstance;
    }

    /**
     * Конструктур с модификатором private длф реализации Singleton
     */
    private CruiseDBConnector() throws DbConnectException{
        connectDB();
    }

    /**
     * Подключение к базе данных
     * @throws DbConnectException
     *  в случае невозможности установить соединение
     */
    private void connectDB() throws DbConnectException{
        try {
            Connection connection = DbConnectorFactory.getMsSqlConnector().getConnection( DB_HOST_NAME, DB_TCP_PORT, DB_NAME,DB_USERNAME, DB_PASSWORD);
            if (connection == null){
                throw new DbConnectException("Не удаётся подключиться к базе данных dKart.");
            }
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectException("Не удаётся подключиться к базе данных dKart.");
        }
    }

    /**
     * Выполнение запроса, в резльтате которого будут получены данные
     * @param sqlQuery - текст SQL-запроса
     * @return результат SQL-запроса
     * @throws SQLException
     *  при возникновении ошибки в запросе
     */
    public ResultSet executeQuery(String sqlQuery) throws SQLException{
        return statement.executeQuery(sqlQuery);
    }

    /**
     * Выполнение SQL-запроса, не подразумевающего получение данных в ответ
     * @param sqlQuery
     * @return
     * @throws SQLException
     *  при возникновении ошибки в запросе
     */
    public boolean execute(String sqlQuery) throws SQLException{
        return statement.execute(sqlQuery);
    }

}
