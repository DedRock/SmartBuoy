package su.systserv.smartBuoy.modem;

import gnu.io.*;
import org.apache.log4j.Logger;
import su.systserv.smartBuoy.modem.exceptions.ATCommandError;

import java.io.*;


/**
 * Класс низкоуровневого обмена AT-командами с модемом
 */
public class ATExchanger {

    private static final Logger log = Logger.getLogger(ATExchanger.class.getName());

    private SerialPort serialPort;
    private OutputStream out;
    private InputStream in;


    /**
     * Base empty constructor
     */
    protected ATExchanger(){}

    /**
     * Constructor
     * @param serialPort
     * @param baudRate
     * @param dataBits
     * @param stopBits
     * @param parity
     * @param flowControl
     * @throws UnsupportedCommOperationException
     *  in case of wrong Serial-port's parameters
     * @throws IOException
     *  in case of IO-error in using SerialPort
     */
    public ATExchanger(SerialPort serialPort, int baudRate, int dataBits, int stopBits, int parity, int flowControl) throws UnsupportedCommOperationException, IOException  {
        init(serialPort, baudRate, dataBits, stopBits, parity, flowControl);
    }

    /**
     * Initialization of COM-port
     * @param serialPort
     * @param baudRate
     * @param dataBits
     * @param stopBits
     * @param parity
     * @param flowControl
     * @throws UnsupportedCommOperationException
     * @throws IOException
     */
    public void init(SerialPort serialPort, int baudRate, int dataBits, int stopBits, int parity, int flowControl) throws UnsupportedCommOperationException, IOException{
        this.serialPort = serialPort;
        this.serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);
        this.serialPort.setFlowControlMode(flowControl);
        out = serialPort.getOutputStream();
        in = serialPort.getInputStream();
        // Shutdown Echo-mode
        sendATCommand("ATE", "0");
    }

    /**
     * Close all opened instances
     * @throws IOException
     */
    public void close() throws IOException{
        in.close();
        out.close();
        serialPort.close();
    }

    /**
     * Send AT-Command to COM-port of Modem (OutputStream must be openned)
     * @param command
     * @throws IOException
     *  in case of error in open OutputStream of modem
     */
    @Deprecated
    public void send(String command) throws IOException {
        if (out != null){
            out.write(command.getBytes());
            log.debug("Send: " + command);
        }
    }

    /**
     * Send AT-command (auto ends with <CR><LF>)
     * @param command
     * @param parameters
     * @throws IOException
     *  in case of IO-errors of modem's COM-port
     *
     *  Examples:
     *  for send command: "AT+CMGF=1"
     *  use call of this method: sendATCommand("AT+CMGF", "=1")
     */
    public void sendATCommand(String command, String parameters) throws IOException {
        if (out != null){
            String fullCommand = new String(command).concat(parameters).concat("\r\n");
            out.write(fullCommand.getBytes());
            log.debug("Send: " + fullCommand);
        }
    }

    /**
     * Send data with ending of enter by CTRL + Z
     * @param data
     * @throws IOException
     */
    public void enterData(String data) throws IOException{
        if (out != null){
            String ctrlZ = "\u001A";
            out.write(data.concat(ctrlZ).getBytes());
        }

    }

    /**
     * Wait specific answer in period of time
     * @param timeout
     * @return
     * @throws IOException
     */
    public boolean waitSpecAnswer(String answer, int timeout) throws IOException {
        boolean result = false;
        if (in != null){
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            char curChar;
            Long startTime = System.currentTimeMillis();
            while( (System.currentTimeMillis() - startTime) < timeout){
                if( br.ready()) { // new data received
                    if ( (curChar = (char)br.read()) != -1){
                        sb.append(curChar);
                        if (sb.indexOf(answer) != -1){
                            result = true;
                            break;
                        }
                    }
                }
            }
            if (br != null){
                br.close();
            }
            log.debug("Receive:" + sb);
        }
        return result;
    }

    boolean executeATCommand(String command, String goodAnswer, int timeout) throws IOException, ATCommandError {

        boolean result = false;
        if (in != null && out != null){
            // Send AT-command
            send(command);

            // Wait answer
            StringBuilder allReceiveData = new StringBuilder();
            boolean error = false;
            StringBuilder errorMessage = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            char curChar;
            Long startTime = System.currentTimeMillis();
            while( (System.currentTimeMillis() - startTime) < timeout){
                if( br.ready()) { // new data received
                    if ( (curChar = (char)br.read()) != -1){
                        allReceiveData.append(curChar);
                        if (! error){
                            if (allReceiveData.indexOf(goodAnswer) != -1){
                                result = true;
                            } else if ( allReceiveData.indexOf("ERROR") != -1){
                                error = true;
                            }
                        } else { // error
                            errorMessage.append(curChar);
                        }
                    }
                }
            }
            log.debug("Receive:" + allReceiveData);

            if (error){
                throw new ATCommandError(errorMessage.toString());
            }

            // Close input Stream
            if (br != null){
                br.close();
            }

        }
        return result;
    }

    /**
     * Receive all data by period of time
     * @param timeout
     * @return
     * @throws IOException
     *  in case of IO-errors of serial port
     */
    StringBuilder receive(int timeout) throws IOException {
        StringBuilder result = new StringBuilder();
        if (in != null){
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            char curChar;
            long startTime = System.currentTimeMillis();
            while ( (System.currentTimeMillis() - startTime < timeout) ) {
                if (br.ready()){
                    if ( (curChar = (char)br.read()) != -1){
                        result.append(curChar);
                    }
                }
            }
            log.debug("Receive: " + result);
            if (br != null){
                br.close();
            }
        }
        return result;
    }

    public InputStream getIS(){
        return in;
    }
}
