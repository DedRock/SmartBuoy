package su.systserv.smartBuoy.dkart.iec61162;

public class Longitude {
    private Integer bigFormatView;
    private boolean availableValue;
    private byte degrees;
    private byte minutes;
    private float seconds;
    private boolean westSide;

    private final static int NOT_AVAILABLE_VALUE = 108600000;

    /**
     * Constructor
     * @param value - Longitude in 1/10 000 min (±180 degrees, East = positive, West = negative.
     *                181 degrees (6791AC0 hex) = not available = default)
     * @throws LongtitudeErrorFormatException
     *  in case of wrong format
     */
    public Longitude(int value) throws LongtitudeErrorFormatException{
        bigFormatView = value;
        // check for true diapason
        if (value >= -108000000 && value <= 108000000){
            availableValue = true;
            //===== eastSide ==========================
            if (value < 0){
                westSide = true;
                value = Math.abs(value);
            }
            int minutesPart;
            minutesPart = value / 10000;
            degrees = (byte)(minutesPart/60); // degrees
            minutes = (byte)(minutesPart % 60); // minutes
            seconds = 60*(value/10000.0f - minutesPart);
        } else if (value == NOT_AVAILABLE_VALUE) {
            availableValue = false;
        } else{
            bigFormatView = null;
            throw new LongtitudeErrorFormatException("Некорректное значение долготы");
        }
    }

    @Override
    public String toString(){
        if (availableValue)
            return String.format("Longtitude: + %d°%d\'%.2f\"%s", degrees, minutes, seconds , (westSide ? "W" : "E"));
        else
            return "Unavailable value";
    }

    public boolean isAvailableValue() {
        return availableValue;
    }

    public boolean isWestSide() {
        return westSide;
    }

    public float getSeconds() {
        return seconds;
    }

    public byte getMinutes() {
        return minutes;
    }

    public byte getDegrees() {
        return degrees;
    }

    public Integer getBigFormatView() {
        return bigFormatView;
    }

    /**
     * Test
     * @param args
     */
    public static void main(String...args){
        try{
            Longitude longitude = new Longitude(18138118);
            System.out.println(longitude.toString());
        } catch (LongtitudeErrorFormatException e){
            e.printStackTrace();
        }


    }

}
