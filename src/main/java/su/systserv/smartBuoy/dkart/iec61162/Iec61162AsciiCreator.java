package su.systserv.smartBuoy.dkart.iec61162;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Iec61162AsciiCreator {

    /**
     * Constructor
     */
    public Iec61162AsciiCreator(){
        initAsciiTable();
    }

    private HashMap<Byte, Character> asciiTable = new HashMap<Byte, Character> ();

    /**
     * Initializing ASCII table
     */
    private void initAsciiTable(){
        //private void initAsciiTable(){
        asciiTable.put( (byte) 0, '0');
        asciiTable.put( (byte) 1, '1');
        asciiTable.put( (byte) 2, '2');
        asciiTable.put( (byte) 3, '3');
        asciiTable.put( (byte) 4, '4');
        asciiTable.put( (byte) 5, '5');
        asciiTable.put( (byte) 6, '6');
        asciiTable.put( (byte) 7, '7');
        asciiTable.put( (byte) 8, '8');
        asciiTable.put( (byte) 9, '9');
        asciiTable.put( (byte) 10, ':');
        asciiTable.put( (byte) 11, ';');
        asciiTable.put( (byte) 12, '<');
        asciiTable.put( (byte) 13, '=');
        asciiTable.put( (byte) 14, '>');
        asciiTable.put( (byte) 15, '?');
        asciiTable.put( (byte) 16, '@');
        asciiTable.put( (byte) 17, 'A');
        asciiTable.put( (byte) 18, 'B');
        asciiTable.put( (byte) 19, 'C');
        asciiTable.put( (byte) 20, 'D');
        asciiTable.put( (byte) 21, 'E');
        asciiTable.put( (byte) 22, 'F');
        asciiTable.put( (byte) 23, 'G');
        asciiTable.put( (byte) 24, 'H');
        asciiTable.put( (byte) 25, 'I');
        asciiTable.put( (byte) 26, 'J');
        asciiTable.put( (byte) 27, 'K');
        asciiTable.put( (byte) 28, 'L');
        asciiTable.put( (byte) 29, 'M');
        asciiTable.put( (byte) 30, 'N');
        asciiTable.put( (byte) 31, 'O');
        asciiTable.put( (byte) 32, 'P');
        asciiTable.put( (byte) 33, 'Q');
        asciiTable.put( (byte) 34, 'R');
        asciiTable.put( (byte) 35, 'S');
        asciiTable.put( (byte) 36, 'T');
        asciiTable.put( (byte) 37, 'U');
        asciiTable.put( (byte) 38, 'V');
        asciiTable.put( (byte) 39, 'W');
        asciiTable.put( (byte) 40, '\'');
        asciiTable.put( (byte) 41, 'a');
        asciiTable.put( (byte) 42, 'b');
        asciiTable.put( (byte) 43, 'c');
        asciiTable.put( (byte) 44, 'd');
        asciiTable.put( (byte) 45, 'e');
        asciiTable.put( (byte) 46, 'f');
        asciiTable.put( (byte) 47, 'g');
        asciiTable.put( (byte) 48, 'h');
        asciiTable.put( (byte) 49, 'i');
        asciiTable.put( (byte) 50, 'j');
        asciiTable.put( (byte) 51, 'k');
        asciiTable.put( (byte) 52, 'l');
        asciiTable.put( (byte) 53, 'm');
        asciiTable.put( (byte) 54, 'n');
        asciiTable.put( (byte) 55, 'o');
        asciiTable.put( (byte) 56, 'p');
        asciiTable.put( (byte) 57, 'q');
        asciiTable.put( (byte) 58, 'r');
        asciiTable.put( (byte) 59, 's');
        asciiTable.put( (byte) 60, 't');
        asciiTable.put( (byte) 61, 'u');
        asciiTable.put( (byte) 62, 'v');
        asciiTable.put( (byte) 63, 'w');
    }

    /**
     * calculating control summary by IEC-61162
     */
    private char calcCS(String message){
        char result = (char)0;
        byte[] messageBytes = message.getBytes();
        for(byte curByte :messageBytes){
            result ^= curByte;
        }
        return result;
    }

    /**
     * Get 6-bit ASCII-symbol by numeric-code
     * @param code
     * @return
     */
    public char getIec61162CharByCode(int code){
        return asciiTable.get(code);
    }

    /**
     * Fill list of boolean("bitMessage") from "startPosition" by bits(quantaty = "valueBitLength") of number "value"
     * @param value
     * @param valueBitLength
     * @param bitMessage
     * @param startPosition
     */
    private void fillBits(int value, int valueBitLength, List<Boolean> bitMessage, int startPosition){
        for(int i=0; i<valueBitLength; i++){
            bitMessage.add(startPosition + i, (value & (1 << (valueBitLength-1-i))) != 0);
        }
    }

    /**
     * Forming VDM-message by IEC-61162
     * @param messageId
     * @param repeatIndicator
     * @param mmsi
     * @param navigationStatus
     * @param rateOfTurn
     * @param speedOverGround
     * @param positionAccuracy
     * @param longtitude
     * @param latitude
     * @param courseOverGround
     * @param trueHeading
     * @param utcSecondWhenReportGenerated
     * @param regionalApplication
     * @param spare
     * @param raimFlag
     * @param communicationsState
     * @return ASCII-message
     * @throws Iec61162Exception
     *   in case of null-value of input parameters
     */
    public String formVdmMessage(
            int messageId,
            int repeatIndicator,
            int mmsi,
            int navigationStatus,
            int rateOfTurn,
            int speedOverGround,
            boolean positionAccuracy,
            int longtitude,
            int latitude,
            int courseOverGround,
            int trueHeading,
            int utcSecondWhenReportGenerated,
            int regionalApplication,
            boolean spare,
            boolean raimFlag,
            int communicationsState
            ) throws Iec61162Exception {

        // Check input data
        if ( Integer.valueOf(messageId)== null ||
             Integer.valueOf(repeatIndicator) == null ||
             Integer.valueOf(mmsi) == null ||
                Integer.valueOf(navigationStatus) == null ||
                Integer.valueOf(rateOfTurn) == null ||
                Integer.valueOf(speedOverGround) == null ||
                Boolean.valueOf(positionAccuracy) == null ||
                Integer.valueOf(longtitude) == null ||
                Integer.valueOf(latitude) == null ||
                Integer.valueOf(courseOverGround) == null ||
                Integer.valueOf(trueHeading) == null ||
                Integer.valueOf(utcSecondWhenReportGenerated) == null ||
                Integer.valueOf(regionalApplication) == null ||
                Boolean.valueOf(spare) == null ||
                Boolean.valueOf(raimFlag) == null ||
                Integer.valueOf(communicationsState) == null ){
            throw new Iec61162Exception("Некорректные исходные данные для формирования VDM-сообщения (IEC-61162)");
        }

        //List<Character> message = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        message.append('!');
        message.append('A');
        message.append('I');
        message.append('V');
        message.append('D');
        message.append('M');
        message.append(',');
        message.append('1');
        message.append(',');
        message.append('1');
        message.append(',');
        message.append(',');
        message.append('A');
        message.append(',');
        //===== forming  Info-message by bits ============================
        List<Boolean> messageInfoBits = new ArrayList<>();
        int i;
        // Identifier for this message (0 - 5) (not used)
        fillBits(messageId, 6, messageInfoBits, 0);
        // Repeat Indicator (6 - 7) (not used)
        fillBits(repeatIndicator, 2, messageInfoBits, 6);
        // MMSI (8 - 37)
        fillBits(mmsi, 30, messageInfoBits, 8);
        // Navigation status (38 - 41)
        fillBits(navigationStatus, 4, messageInfoBits, 38);
        // Rate of turn (42 - 49)
        fillBits(rateOfTurn, 8, messageInfoBits, 42);
        // Speed over ground (50 - 59)
        fillBits(speedOverGround, 10, messageInfoBits, 50);
        // Position accuracy (60)
        messageInfoBits.add(60, positionAccuracy);
        // Longitude (61 - 88)
        fillBits(longtitude, 28, messageInfoBits, 61);
        // Latitude (89-115)
        fillBits(latitude, 27, messageInfoBits, 89);
        // Course over ground (116-127)
        fillBits(courseOverGround, 12, messageInfoBits, 116);
        // True Heading (128 - 136)
        fillBits(trueHeading, 9, messageInfoBits, 128);
        // UTC second when report generated (137-142)
        fillBits(utcSecondWhenReportGenerated, 6, messageInfoBits, 137);
        // Regional Application (143-146)
        fillBits(regionalApplication, 4, messageInfoBits, 143);
        // Spare (147)
        messageInfoBits.add(147, spare);
        // RAIM-Flag (148)
        messageInfoBits.add(148, raimFlag);
        // Communications State (149-167)
        fillBits(communicationsState, 19, messageInfoBits, 149);
        //===== end of forming info-message by bits ==========================

        // group messageInfoBits to chars
        int curCharCode = 0;
        for(i=0; i<168; i++){
            if (messageInfoBits.get(i)){
                curCharCode += (1 << 5 - i%6);
            }
            // Если подсчитали код очередного символа из 6 бит
            if (i%6 == 5){
                message.append(asciiTable.get((byte) curCharCode));
                curCharCode = 0; // начинаем подсчитывать код очередного исмвола заново
            }
        }
        /*
        System.out.println("Message = " + message);
        for(i=138; i<160; i++){
            System.out.println("messageBits["+i+"] = "+messageInfoBits.get(i));
        }
        */
        message.append(',');
        message.append('0');
        message.append('*');
        // Вычисляем контрольную сумму, и добавляем в конец сообщения символы обоих байтов контрольной суммы в HEX представлении
        // Пример контрольная сумма равна 61 = 0x3D => message.add('3').add('D');
        for(char curCrcByte : Integer.toHexString((int)calcCS(message.substring(1, message.length()-1))).toCharArray() ){
            message.append(curCrcByte);
        }
        message.append((char)0x0D);
        message.append((char)0x0A);
        return message.toString();
    }

    // Test
    public static void main(String...args){

        try{
        Iec61162AsciiCreator creator = new Iec61162AsciiCreator();


        String messageByCreator;
        /* Example by Sitnikov Ivan*/
        /*
        messageByCreator = creator.formVdmMessage(
        1, // messageId,
        0, // repeatIndicator,
        375254000, // mmsi
        0,// navigationStatus,
        128, // RateOfTurn,
        0,// speedOverGround,
        false, // positionAccuracy,
        18138118, // longtitude
        35948232, // latitude,
        2952, // courseOverGround,
        85, //  trueHeading,
        31, //  utcSecondWhenReportGenerated,
        0, // regionalApplication,
        false, // spare,
        true, // raimFlag,
        33988// communicationsState
        );
        */

            // Buoy_1
            messageByCreator = creator.formVdmMessage(
                    1, // messageId, // must be != 0
                    0, // repeatIndicator,
                    2, // mmsi
                    16,// navigationStatus,
                    128, // RateOfTurn,
                    0,// speedOverGround,
                    false, // positionAccuracy,
                    18411008, // longtitude
                    36800046, // latitude,
                    0, // courseOverGround,
                    0, //  trueHeading,
                    0, //  utcSecondWhenReportGenerated,
                    0, // regionalApplication,
                    false, // spare,
                    false, // raimFlag,
                    0// communicationsState
            );

            System.out.print("messageByCreator = \"" + messageByCreator + "\"");

        } catch (Iec61162Exception e){
            e.printStackTrace();
        }

    }

}
