package su.systserv.smartBuoy.web.enums;

/**
 * Доступные строковые идентификаторы команд на доступ к базе данных от клиентской части Web-приложения
 */
public enum ClientQueryType{
    READ_BUOY_DATA,
    UPDATE_BUOY_DATA,
    CREATE_NEW_BUOY,
    DELETE_BUOY,
    READ_BUOY_IDS
}
