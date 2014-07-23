package su.systserv.smartBuoy.dkart;

import su.systserv.smartBuoy.dkart.exception.DkartDAOException;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 12.05.14
 * Time: 17:24
 * To change this template use File | Settings | File Templates.
 */
public interface DKartDAO {

    void writeNewMessage(String message) throws DkartDAOException;
    /*
    RECDAT (datetime,null) -  дата записи по SQL шкале,
    MSSTXT (nvarchar(255),null)  - текст сообщения, например !AIVDM,1,1,,A,15Uoct0P002:HP<RB6j;R2bv28C4,0*3D – (по-моему нормально для СМС)
    MMSI (int,null)- идентификатор приемника информации – допускается несколько приемников в системе отображения
    MSTYPE (smallint,null) – тип сообщения (1 для текстовых сообщений по стандарту IEC61162)
    ERDATE(datetime,null) – момент ошибки приема
    UTCDAT (datetime,null) -  дата данных в сообщении по шкале СЕВ (UTC),
    */
}
