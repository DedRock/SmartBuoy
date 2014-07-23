package su.systserv.smartBuoy.buoy;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import su.systserv.smartBuoy.buoy.annotations.BuoySetting;
import su.systserv.smartBuoy.buoy.entity.Buoy;

import javax.persistence.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 18.07.14
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class Serialization {
    private static final Logger log = Logger.getLogger(Serialization.class.getName());

    /**
     * Функция добавления пары "ключ - значение" в JSON-объект.
     * Если значение = null, то в качестве значения пишем пустую строку.
     * @param json - экземпляр JSONObject
     * @param key - ключ
     * @param value - значение
     * @throws JSONException
     */
    static public void putValue(JSONObject json, String key, Object value) throws  JSONException{
        log.debug(key + " = " + value);
        if (value == null){
            json.put(key, "");
        }else{
            json.put(key, value);
        }
    }

    /**
     * Функция сериализации всех полей с аннотацией "Column" объекта
     * @param obj
     * @param objClass
     * @return
     */
    @Deprecated
    static public JSONObject getAllColumnsData(Object obj, Class objClass){

        JSONObject result = new JSONObject();

        // Get all fielda of class
        Field[] fields = objClass.getDeclaredFields();
        for(Field field : fields){
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null){
                Class fieldClass = field.getType();
                boolean privateAccess = false;
                // Если
                if (!field.isAccessible()){
                    privateAccess = true;
                    field.setAccessible(true);
                }
                try {

                    putValue(result, field.getName(), field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (JSONException e){
                    e.printStackTrace();
                }
                // Меняем видимость обратно
                if (privateAccess){
                    field.setAccessible(false);
                }
            }
        }
        return result;
    }

    /**
     * Get names and values of fields with specific annotations
     * @param obj - instance of "objClass"
     * @param filterAnnotation - annotations by that fields are filtered
     * @return
     */
    static public JSONObject getFieldsByAnnotation(Object obj, Class filterAnnotation){
        JSONObject result = new JSONObject();

        // Find fields with specific annotations
        Class clazz = obj.getClass();
        for(Field field : clazz.getDeclaredFields()){
            for(Annotation annotation : field.getDeclaredAnnotations()){
                if (filterAnnotation.isInstance(annotation)){
                    boolean privateAccess = false;
                    // Если поле закрыто для доступа - на время чтения открываем его
                    if (!field.isAccessible()){
                        privateAccess = true;
                        field.setAccessible(true);
                    }
                    try {
                        putValue(result, field.getName(), field.get(obj));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    // Меняем видимость поля обратно
                    if (privateAccess){
                        field.setAccessible(false);
                    }
                }
            }
        }

        return result;
    }

    public static void main(String...args){
        Buoy buoy = new Buoy();
        buoy.setName("Test");
        buoy.setSimNumber(9117350627L);

        //JSONObject json = getAllColumnsData(buoy, Buoy.class);
       JSONObject json = getFieldsByAnnotation(buoy, BuoySetting.class);
        System.out.println(json);

    }
}
