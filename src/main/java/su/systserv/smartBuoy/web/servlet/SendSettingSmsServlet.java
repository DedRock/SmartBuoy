package su.systserv.smartBuoy.web.servlet;

import gnu.io.SerialPort;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.buoy.BuoyType;
import su.systserv.smartBuoy.buoy.entity.MeteoBuoy;
import su.systserv.smartBuoy.modem.Modem;
import su.systserv.smartBuoy.modem.exceptions.ModemUseException;
import su.systserv.smartBuoy.hibernate.HibernateUtil;
import su.systserv.smartBuoy.web.enums.ServerErrorCode;
import su.systserv.smartBuoy.web.exceptions.ServletExecutionException;
import su.systserv.smartBuoy.web.exceptions.ServletInputParamsException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;

import static su.systserv.smartBuoy.buoy.BuoyType.*;

/**
 * Servlet for send SMS with Setting of Buoy
 *
 * Format of query (JSON):
 * {
 *     "simNumber" : N
 *     "setting" : STRING
 *     "value" : STRING
 * }
 *
 */
public class SendSettingSmsServlet extends HttpServlet {

    SessionFactory sessionFactory = null;

    @Override
    public void init(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response){

        Session session = null;

        //===== Проверка параметров пользовательского запроса ==============================
        try{
            Long simNumber = ServletUtil.getLongParameter("simNumber", request);
            if (simNumber == null || simNumber < 9000000000L || simNumber > 9990000000L){
                throw new ServletInputParamsException("simNumber");
            }
            String setting = ServletUtil.getParameterValue("setting", request);
                if (simNumber == null){
                    throw new ServletInputParamsException("setting");
                }
            String value = ServletUtil.getParameterValue("value", request);
            if (value == null){
                throw new ServletInputParamsException("value");
            }

            // Проверка зарегистрированного буя с указанным номером

            session = sessionFactory.openSession();
            session.beginTransaction();

            // Убедиться что искомый номер сим-карты зарегистрирован за каким-то буем
            Query query = session.createQuery("from Buoy where simNumber = " + simNumber);
            List<Buoy> list = query.list();
            if (list.isEmpty()){
                throw new ServletExecutionException("sim-card number is not found !");
            } if ( list.size() > 1) {
                throw new ServletExecutionException(" sim-card number is not identical !");
            }
            // Определить тип буя
            BuoyType curBuoyType = getTypeById(list.get(0).getBuoyTypeId());
            if (curBuoyType == null){
                throw new ServletExecutionException("unknown type of buoy (with number:" + simNumber + ") !");
            }
            // Убедиться, что такая настройка предусмотрена в буе
            Field curField;
            Class settingClass;
            switch (curBuoyType){
                case BASIC:
                    try{
                        curField = Buoy.class.getField(setting);
                        settingClass = curField.getClass();
                        // Определяем может ли строка 'value' быть приведена к нужному типу параметра
                        if ( ! checkValueType(settingClass, value) ){
                            throw new ServletExecutionException("parameter value has wrong data type !");
                        }
                    }catch (Exception e){
                        throw new ServletExecutionException("unknown setting name !");
                    }
                    break;

                case METEO:
                    try{
                        curField = Buoy.class.getField(setting);
                        settingClass = curField.getClass();
                        // Определяем может ли строка 'value' быть приведена к нужному типу параметра
                        if ( ! checkValueType(settingClass, value) ){
                            throw new ServletExecutionException("parameter value has wrong data type !");
                        }
                    }catch (Exception e1){
                        // Field is not found in class "Buoy" - try to find into class MeteoBuoy
                        try{
                            curField = MeteoBuoy.class.getField(setting);
                            settingClass = curField.getClass();
                            // Определяем может ли строка 'value' быть приведена к нужному типу параметра
                            if ( ! checkValueType(settingClass, value) ){
                                throw new ServletExecutionException("parameter value has wrong data type !");
                            }
                        }catch (Exception e2){
                            throw new ServletExecutionException("unknown setting name !");
                        }
                    }
                    break;
            } // switch

            // Отправить SMS с новым значением настройки
            Modem modem = null;
            try{
                SerialPort modemSerialPort = Modem.findModem();
                if (modemSerialPort == null){
                    throw new ModemUseException("cann't find modem.");
                }
                modem = new Modem(modemSerialPort, 115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, SerialPort.FLOWCONTROL_NONE);
                if (!modem.sendSMS(simNumber, setting + "\n" + value)){
                    throw new ModemUseException("execution error.");
                }
            } catch (ModemUseException e){
                ServletUtil.sendError(ServerErrorCode.SERVER_ERROR.getCode(),"Send SMS error: " + e.getMessage(),  response);
                return;
            } finally {
              if (modem != null){
                  modem.close();
              }
            }

        } catch (ServletInputParamsException e){
            ServletUtil.sendError(ServerErrorCode.CLIENT_QUERY_ERROR.getCode(),"Request parameter error: " + e.getMessage(),  response);
            return;

        } catch (Exception e){
            ;
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
    }

    /**
     * Функция проверки приведения типа параметра из строкового значения
     * @param settingClass - класс типа параметра
     * @param value - строковое значение параметра
     * @return
     *  true - приведение типа возможно
     *  false - приведение типа невозможно
     */
    boolean checkValueType(Class settingClass, String value){
        boolean result = false;

        // String
        if ( settingClass == java.lang.String.class){
            return true;
        }
        // Byte
        else if (settingClass == java.lang.Byte.class){
            try{
                Byte.parseByte(value);
                return true;
            }catch (Exception e){
                return false;
            }
        }
        // Integer
        else if (settingClass == java.lang.Integer.class){
            try{
                Integer.parseInt(value);
                return true;
            }catch (Exception e){
                return false;
            }
        }
        // Long
        else if (settingClass == java.lang.Long.class){
            try{
                Long.parseLong(value);
                return true;
            }catch (Exception e){
                return false;
            }
        }
        // Boolean
        else if (settingClass == Boolean.class){
            try{
                Boolean.parseBoolean(value);
                return true;
            }catch (Exception e){
                return false;
            }
        }
        return result;
    }
}
