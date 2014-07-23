package su.systserv.smartBuoy.buoy;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 20.05.14
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public enum BuoyType {
    BASIC(1),
    METEO(2),
    ECOLOG(3);

    private byte id;
    BuoyType(int id){
        this.id = (byte)id;
    }

    public byte getId(){
        return id;
    }


    public static BuoyType getTypeById(byte id){
        for (BuoyType curr : BuoyType.values()){
            if (curr.getId() == id){
                return curr;
            }
        }
        return null;
    }
}
