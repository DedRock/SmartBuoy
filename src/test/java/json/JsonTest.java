package json;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.07.14
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
public class JsonTest extends Assert {

    @Test
    public void jsonFindParameter(){
        String str = "{\"voltage\": 24.56}";
        try {
            JSONObject json = new JSONObject(str);
            System.out.println(json);


        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        assertTrue(true);
    }
}
