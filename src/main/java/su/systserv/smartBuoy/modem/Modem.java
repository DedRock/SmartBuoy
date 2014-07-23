package su.systserv.smartBuoy.modem;

import gnu.io.*;
import org.apache.log4j.Logger;
import su.systserv.smartBuoy.modem.exceptions.ATCommandError;
import su.systserv.smartBuoy.modem.exceptions.ModemUseException;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Class for using GSM-modem (via COM-port)
 */
public class Modem extends ATExchanger {

    private static final Logger log = Logger.getLogger(Modem.class.getName());

    /**
     * Constructor
     * @param port - instance of SerialPort
     * @param speed - transmission speed
     * @param dataBits - number of data bits
     * @param stopBits
     * @param parity
     * @param flowControl
     * @throws su.systserv.smartBuoy.modem.exceptions.ModemUseException
     *  in case of IO-error's of modem's COM-port or invalid settings of COM-port
     */

    public Modem(
            SerialPort port,
            int speed,
            int dataBits,
            int stopBits,
            int parity,
            int flowControl
    ) throws ModemUseException {
        super();
        try{
            init(port, speed, dataBits, stopBits, parity, flowControl);
        } catch (UnsupportedCommOperationException e) {
            throw new ModemUseException("Invalid serial port settings");
        } catch (IOException e) {
            throw new ModemUseException("Can't initialize modem");
        }
    }

    /**
     * Close all opened streams and ports
     */
    public void close(){
        try {
            super.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Send SMS-message via GSM-modem to phone number
     * @param number
     * @param messageText
     * @return
     * @throws ModemUseException
     *  in case of IO-errors of COM-port
     */
    public boolean sendSMS(Long number, String messageText) throws ModemUseException {
        try{
            //===== 1 step: init text message format =====
            sendATCommand("AT+CMGF", "=1");
            if (!waitSpecAnswer("OK", 50)){
                return false;
            }
            //===== 2 step: set number of sim-card =====
            sendATCommand("AT+CMGS", "=\"+7" + number + "\"\r\n");
            if (!waitSpecAnswer("> ", 50)){
                return false;
            }
            //===== 3 step: set text of message =====
            enterData(messageText);
            if (!waitSpecAnswer("OK", 5000)){
                return false;
            }
            return true;
        } catch (IOException e){
            log.debug(e.getMessage() );
            throw new ModemUseException("Modem's COM-port IO-access error");
        }
    }

    /**
     * Search Serial port of GSM-modem
     * @return
     */
    public static SerialPort findModem(){
        log.debug("Find Modem method");
        SerialPort resultSerialPort = null;
        SerialPort testSerialPort = null;

        // Get All COM-port of System
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier)portEnum.nextElement();
            try {
                log.debug("<<< Check " + currPortId.getName() + " >>>");
                testSerialPort = (SerialPort)currPortId.open("Test", 250);
                ATExchanger exchanger = new ATExchanger(testSerialPort, 115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, SerialPort.FLOWCONTROL_NONE);

                // First check: answer of base AT-command "AT<CR><LF>" to receive "OK"-answer
                exchanger.sendATCommand("AT", "");
                //exchanger.send("AT\r\n");
                if (!exchanger.waitSpecAnswer("OK", 50)){
                    //testSerialPort.close();
                    continue;
                }

                /**
                 * Then I use GSM-modem Huawei E1550, is Windows OS created several COM-ports:
                 * HUAWEI Modile Connect - 3G Application Interface
                 *  - don't answeк to AT-commands
                 * HUAWEI Modile Connect - 3G PC UI Interface
                 *  - answer to AT-commands
                 *  - periodical send ^RSSI and ^BOOT info
                 *
                 * HUAWEI Modile Connect - 3G Modem #2 ()
                 *  - answer to AT-commands
                 *  - have a clear channel
                 */

                // Second check: empty channel (see comment above)
                if (exchanger.waitSpecAnswer("^", 5000)){
                    continue;
                }
                log.debug("Modem found.");
                resultSerialPort = testSerialPort;
                break;

            } catch (IOException e) {
                log.debug(currPortId.getName() + " : Serial port's IO-access error");
            } catch (PortInUseException e) {
                log.debug(currPortId.getName() + " : Port in use");
            } catch (UnsupportedCommOperationException e) {
                System.out.println(currPortId.getName() + " : Invalid init parameters of serial port");
            } catch (ClassCastException e){
                // IF get LPT-port and try to cast to SerialPort.class
                continue;
            } finally {
                if (testSerialPort != null){
                    testSerialPort.close();
                }
            }
        }
        return resultSerialPort;
    }
}
