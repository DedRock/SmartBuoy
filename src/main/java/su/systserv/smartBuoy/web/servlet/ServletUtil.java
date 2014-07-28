package su.systserv.smartBuoy.web.servlet;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 03.06.14
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public class ServletUtil {

    /**
     * Отправить в качестве HTTP-ответа JSON-объект
     * @param jsonAnswer
     * @param response
     */
    public static void sendJsonAsnwer(JSONObject jsonAnswer, HttpServletResponse response){
        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonAnswer.toString());
        } catch (Exception e){

            e.printStackTrace();
        }
    }

    /**
     * Формирование ответа с кодом ошибки
     * @param errorCode - код ошибки
     * @param response - ответ, экземпляр HttpServletResponse
     */
    public static void sendError(int errorCode, String message, HttpServletResponse response) {
        try{
            response.setStatus(errorCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Получение параметра из HTTP-запроса
     * @param parameterName - имя параметра
     * @param request - экземпляр класса HttpServletRequest, содержащий запрос
     * @return значение параметра
     */
    public static String getParameterValue(String parameterName, HttpServletRequest request) {
        if(parameterName != null && !parameterName.isEmpty()) {
            return request.getParameter(parameterName);
        }
        return null;
    }

    /**
     * Получение значения типа Integer из параметров HTTP-запроса
     * @param parameterName
     * @param request
     * @return
     */
    public static Integer getIntParameter(String parameterName, HttpServletRequest request) {
        if(parameterName != null && !parameterName.isEmpty()) {
            String parameterValue = getParameterValue(parameterName, request);
            if(parameterValue != null && !parameterValue.isEmpty()) {
                return Integer.parseInt(parameterValue);
            }
        }
        return null;
    }

    /**
     * Получение значения типа Integer из параметров HTTP-запроса
     * @param parameterName
     * @param request
     * @return
     */
    public static Long getLongParameter(String parameterName, HttpServletRequest request) {
        if(parameterName != null && !parameterName.isEmpty()) {
            String parameterValue = getParameterValue(parameterName, request);
            if(parameterValue != null && !parameterValue.isEmpty()) {
                return Long.parseLong(parameterValue);
            }
        }
        return null;
    }

     /**
     * Функция проверки приведения типа параметра из строкового значения
     * @param settingClass - класс типа параметра
     * @param value - строковое значение параметра
     * @return
     *  true - приведение типа возможно
     *  false - приведение типа невозможно
     */
     public static boolean checkValueType(Class settingClass, String value){
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
