package dkart.iec61162;

import org.junit.Assert;
import org.junit.Test;
import su.systserv.smartBuoy.dkart.iec61162.Iec61162AsciiCreator;
import su.systserv.smartBuoy.dkart.iec61162.Iec61162Exception;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 23.07.14
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public class Iec61162AsciiCreatorTest extends Assert {

    Iec61162AsciiCreator creator = new Iec61162AsciiCreator();

    @Test
    public void createMessage21Test1(){

        String messageByCreator = null;
        try{
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
        } catch (Iec61162Exception e){
            assertFalse("Ошибка при создании строки сообщения №21", true);
        }
        if (messageByCreator != null){
            assertTrue(messageByCreator.equals("!AIVDM,1,1,,A,15Uoct0P002:HP<RB6j;R2bv28C4,0*3d\r\n"));
        }
    }

    @Test
    public void createMessage21Test2(){

        String messageByCreator = null;
        try{
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
        } catch (Iec61162Exception e){
            assertFalse("Ошибка при создании строки сообщения №21", true);
        }
        if (messageByCreator != null){
            assertTrue(messageByCreator.equals("!AIVDM,1,1,,A,100000PP002<Mh0S66;P00000000,0*4\r\n"));
        }
    }
}
