package su.systserv.smartBuoy.buoy;

import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.buoy.entity.MeteoBuoy;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 21.05.14
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class BuoyFactory {

    /**
     * Фабричный метод создания экземпляра класса Buoy
     * @param id - идентификатор
     * @param simNumber - номер SIM-карты
     * @return экземпляр класса Buoy
     */
    public static Buoy cretaeBuoy(int id, String name, Long simNumber){
        return new Buoy(id, name, simNumber, BuoyType.BASIC.getId());
    }

    /**
     * Фабричный метод создания экземпляра класса MeteoBuoy
     * @param id - идентификатор
     * @param simNumber - номер SIM-карты
     * @return экземпляр класса Buoy
     */
    public static MeteoBuoy cretaeMeteoBuoy(int id, Long simNumber){
        return new MeteoBuoy(); //id, simNumber, BuoyType.METEO.getId());
    }
}
