package su.systserv.smartBuoy.web.servlet;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.07.14
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
public class JsonUtils {

    private static final Logger log = Logger.getLogger(JsonUtils.class.getName());

    /**
     * Get Integer-value from JSON
     * @param data
     * @param parameterName
     * @return
     */
    static public Integer getJsonInt(JSONObject data, String parameterName) {
        if (data.has(parameterName)){
            try {
                return data.getInt(parameterName);
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Get Double-value from JSON
     * @param data
     * @param parameterName
     * @return
     */
    static public Double getJsonDouble(JSONObject data, String parameterName) {
        if (data.has(parameterName)){
            try {
                return data.getDouble(parameterName);
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }



    /**
     * Get String-value from JSON
     * @param data
     * @param parameterName
     * @return
     */
    static public String getJsonString(JSONObject data, String parameterName) {
        if (data.has(parameterName)){
            try {
                return data.getString(parameterName);
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }
}
