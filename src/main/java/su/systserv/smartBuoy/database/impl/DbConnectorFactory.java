package su.systserv.smartBuoy.database.impl;

import su.systserv.smartBuoy.database.DBConnectorIF;
import su.systserv.smartBuoy.database.DatabaseType;
import su.systserv.smartBuoy.database.exception.UnknownDbTypeException;

/**
 * Фабрика по созданию подключений(Connection) к разным базам данных
 */
public class DbConnectorFactory  {

    /**
     * Фаюрика по созданию connector-ов к базам данных из перечисления DatabaseType
     * @param databaseType
     * @return
     * @throws su.systserv.smartBuoy.database.exception.UnknownDbTypeException
     */
    public static DBConnectorIF getConnector(DatabaseType databaseType) throws UnknownDbTypeException {
        if (databaseType.equals(DatabaseType.MY_SQL)){
            return new DBConnectorMySQL();
        } else if (databaseType.equals(DatabaseType.MY_SQL)){
            return new DBConnectorMsSQL();
        } else{
            throw new UnknownDbTypeException("Неизвестный тип базы данных");
        }
    }

    /**
     * Фабричный метод создания connector-а к базе данных MySQL
     * @return реализация интерфейса DBConnectorIF
     */
    public static DBConnectorIF getMySqlConnector(){
        return new DBConnectorMySQL();
    }

    /**
     * Фабричный метод создания connector-а к базе данных Microsoft SQL Server
     * @return реализация интерфейса DBConnectorIF
     */
    public static DBConnectorIF getMsSqlConnector(){
        return new DBConnectorMsSQL();
    }
}
