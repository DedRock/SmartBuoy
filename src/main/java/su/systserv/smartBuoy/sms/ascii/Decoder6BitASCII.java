package su.systserv.smartBuoy.sms.ascii;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 07.02.14
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class Decoder6BitASCII {

    private static HashMap<Byte, Character> asciiTable = new HashMap<Byte, Character> ();
    /**
     * Initializing ASCII table
     */
    static{
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
        asciiTable.put( (byte) 16, '\'');
        asciiTable.put( (byte) 17, 'Q');
        asciiTable.put( (byte) 18, 'R');
        asciiTable.put( (byte) 19, 'S');
        asciiTable.put( (byte) 20, 'T');
        asciiTable.put( (byte) 21, 'U');
        asciiTable.put( (byte) 22, 'V');
        asciiTable.put( (byte) 23, 'W');
        asciiTable.put( (byte) 24, 'X');
        asciiTable.put( (byte) 25, 'Y');
        asciiTable.put( (byte) 26, 'Z');
        asciiTable.put( (byte) 27, '[');
        asciiTable.put( (byte) 28, '\\');
        asciiTable.put( (byte) 29, ']');
        asciiTable.put( (byte) 30, '^');
        asciiTable.put( (byte) 31, '-');
        asciiTable.put( (byte) 32, ' ');
        asciiTable.put( (byte) 33, '!');
        asciiTable.put( (byte) 34, '\'');
        asciiTable.put( (byte) 35, '#');
        asciiTable.put( (byte) 36, '$');
        asciiTable.put( (byte) 37, '%');
        asciiTable.put( (byte) 38, '&');
        asciiTable.put( (byte) 39, '\'');
        asciiTable.put( (byte) 40, '(');
        asciiTable.put( (byte) 41, ')');
        asciiTable.put( (byte) 42, '*');
        asciiTable.put( (byte) 43, '+');
        asciiTable.put( (byte) 44, ',');
        asciiTable.put( (byte) 45, '_');
        asciiTable.put( (byte) 46, '.');
        asciiTable.put( (byte) 47, '/');
        asciiTable.put( (byte) 48, '0');
        asciiTable.put( (byte) 49, '1');
        asciiTable.put( (byte) 50, '2');
        asciiTable.put( (byte) 51, '3');
        asciiTable.put( (byte) 52, '4');
        asciiTable.put( (byte) 53, '5');
        asciiTable.put( (byte) 54, '6');
        asciiTable.put( (byte) 55, '7');
        asciiTable.put( (byte) 56, '8');
        asciiTable.put( (byte) 57, '9');
        asciiTable.put( (byte) 58, ':');
        asciiTable.put( (byte) 59, ';');
        asciiTable.put( (byte) 60, '<');
        asciiTable.put( (byte) 61, '=');
        asciiTable.put( (byte) 62, '>');
        asciiTable.put( (byte) 63, '?');

    }

    /**
     * Function to get key by value from Hash
     * @param value
     * @return
     */
    private static byte getKeybyValue(char value){
        Iterator it = asciiTable.entrySet().iterator();
        while( it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            if( ((Character)entry.getValue()).equals(value))
                return (Byte)entry.getKey();
        }
        return (byte)0xff;
    }

    /**
     * Function to decode message from 6-bit ASCII table to casual ASCII table
     * @param msg - string to decode
     * @return string in casual ASCII
     */
    public static String decodeMsg(String msg){
        StringBuilder str = new StringBuilder();
        byte[] byteArray = msg.getBytes();
        for (int i=0; i < byteArray.length; i++){
            str.append(asciiTable.get( (int) byteArray[i]));
        }
        return str.toString();
    }

    /**
     * Function to code message from casual ASCII table to 6-bit ASCII table
     * @param msg - string to code
     * @return
     */
    public static String codeMsg(String msg){
        StringBuilder str = new StringBuilder();
        byte[] byteArray = msg.getBytes();
        for (int i=0; i < byteArray.length; i++){
                if ( asciiTable.containsValue( (char) byteArray[i]) ){
                    str.append( (char) getKeybyValue( (char)byteArray[i]));
                } else{
                    System.out.println("Decoder6BitASCII Error: symbol \"" + byteArray[1] + "\" cann't find in 6-bit ASCII table of SMSs.");
                }
        }
        return str.toString();
    }


    public static void main(String...args){

        String testMsg =  new String(new byte[] {(byte)0x08,0x05,0x0c,0x0c,0x0f});
        /*
        StringBuilder str = new StringBuilder();

        byte[] byteArray = testMsg.getBytes();
        System.out.println(byteArray[1]);
        str.append(asciiTable.get(byteArray[1]));
        System.out.println(str);
        */
        System.out.println(Decoder6BitASCII.decodeMsg(testMsg));

        Character cr = 'x';
        System.out.println("Code of symbol \"" + cr + "\" = " + Decoder6BitASCII.getKeybyValue(cr) );

        String codeMsg = Decoder6BitASCII.codeMsg("HELLO!");
        System.out.println("codeMasg of \"HELLO!\" in hex= " + HexBin.encode(codeMsg.getBytes()));
        System.out.println("codeMasg of \"HELLO!\" in chars= " + codeMsg);

    }

}
