package modem;

import gnu.io.*;
import org.junit.Before;
import org.junit.Test;
import su.systserv.smartBuoy.modem.ATExchanger;
import su.systserv.smartBuoy.modem.Modem;
import su.systserv.smartBuoy.modem.exceptions.ATCommandError;
import su.systserv.smartBuoy.modem.exceptions.ModemUseException;
import su.systserv.smartBuoy.web.enums.ServerErrorCode;
import su.systserv.smartBuoy.web.servlet.ServletUtil;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 23.07.14
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
public class ModemTest {

    SerialPort modemSerialPort = null;


    /**
     * Тест на нахождение COM-порта модема
     */
    @Test
    @Before
    public void findGsmModem(){
        modemSerialPort = Modem.findModem();
        if (modemSerialPort == null){
            System.out.println("Cann't find modem !");
        }
        System.out.println("Modem on port " + modemSerialPort.getName());
    }

    /**
     * Тест на отправку SMS через заранее найденный модем
     * @param simNumber - номер sim-карты
     * @param setting - имя настроечного параметра
     * @param value - значение настроечного параметра
     */
    //@Test
    public void sendSMS(Long simNumber, String setting, String value){
        if (modemSerialPort!= null){
            try{
                Modem modem = new Modem(modemSerialPort, 115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, SerialPort.FLOWCONTROL_NONE);
                if (!modem.sendSMS(simNumber, setting + "\n" + value)){
                    throw new ModemUseException("execution error.");
                }
            } catch (ModemUseException e){
                System.out.println("Send SMS error: " + e.getMessage());
            }
        }
    }

    /*
    public void sendSmsViaComPort(){
        try{
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier("COM11");
            modemSerialPort = (SerialPort)portId.open("Test", 250);

            ATExchanger exchanger = new ATExchanger(modemSerialPort, 115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, SerialPort.FLOWCONTROL_NONE);
            // ...

        } catch (PortInUseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (modemSerialPort != null){
                modemSerialPort.close();
            }
        }
    }
    */
}
