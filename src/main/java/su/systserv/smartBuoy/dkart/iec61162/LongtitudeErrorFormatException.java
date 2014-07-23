package su.systserv.smartBuoy.dkart.iec61162;

/**
 * Исключение, возникающее при неверном формате записи широты
 */
public class LongtitudeErrorFormatException extends Exception {

    public LongtitudeErrorFormatException(String message){
        super(message);
    }


}
