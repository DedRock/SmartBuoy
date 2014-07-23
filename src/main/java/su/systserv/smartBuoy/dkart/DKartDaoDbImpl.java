package su.systserv.smartBuoy.dkart;

import org.apache.log4j.Logger;
import su.systserv.smartBuoy.database.exception.DbConnectException;
import su.systserv.smartBuoy.dkart.dbExchange.connector.CruiseDBConnector;
import su.systserv.smartBuoy.dkart.exception.DKartConnectException;
import su.systserv.smartBuoy.dkart.exception.DkartDAOException;

import java.sql.SQLException;

/**
 * Класс взаимодействия с dKart посредством базы данных
 */
public class DKartDaoDbImpl implements DKartDAO {
    private static final Logger log = Logger.getLogger(DKartDaoDbImpl.class.getName());
    CruiseDBConnector dbh;

    /**
     * Конструктор, в рамках которого осущетвляется подключения к базе данных посредством класса CruiseDBConnector
     * @throws DKartConnectException
     *  при возникновении ошибки подключения к базе данных dKart
     */
    public DKartDaoDbImpl()throws DKartConnectException, DkartDAOException {
        try {
            dbh = CruiseDBConnector.getInstance();
        } catch (DbConnectException e) {
            e.printStackTrace();
            throw new DKartConnectException("Ошибка подключения к dKart.");
        }
    }

    @Override
    public void writeNewMessage(String message) throws DkartDAOException {
        try {
            dbh.executeQuery("SELECT * FROM dbo.NSDB");
        } catch (SQLException e) {
            throw new DkartDAOException("Ошибка работы с данными dKart.");
        }
    }


    public static void main(String...args){
        try {
            DKartDaoDbImpl exchanger = new DKartDaoDbImpl();
            //exchanger.writeNewMessage("");
            System.out.println("Подключение к базе прошло успешно.");
        } catch (DKartConnectException e) {
            log.error(e.getMessage());
        } catch (DkartDAOException e) {
            log.error(e.getMessage());
        }
    }
}
