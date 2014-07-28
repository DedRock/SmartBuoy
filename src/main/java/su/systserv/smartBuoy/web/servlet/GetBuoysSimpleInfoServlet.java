package su.systserv.smartBuoy.web.servlet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import su.systserv.smartBuoy.buoy.Serialization;
import su.systserv.smartBuoy.buoy.annotations.BuoySimpleInfo;
import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.hibernate.HibernateUtil;
import su.systserv.smartBuoy.web.enums.ServerErrorCode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервлет для выдачи базовой информации о зарегистрированных в системе буях
 * Формат запроса:
 *   - без параметров
 *
 * Формат ответа: JSON
 *  {
 *      "simpleBuoyData" :
 *          [
 *              {
 *                  "mmsi" : Number,
 *                  "name" : String,
 *                  "simNumber" : String,
 *                  "type" : Number
 *              },
 *              . . .*
 *          ]
 *  }
 */
 public class GetBuoysSimpleInfoServlet extends HttpServlet {

    SessionFactory sessionFactory = null;

    @Override
    public void init(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response){
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.clear();
            session.beginTransaction();

            List<Buoy> buoys = new ArrayList<Buoy>();
            buoys = (List<Buoy>)session.createQuery("FROM su.systserv.smartBuoy.buoy.entity.Buoy").list();            ;


            List<JSONObject> buoysSimpleInfo = new ArrayList<JSONObject>();
            for( Buoy buoy : buoys){
                //buoysSimpleInfo.add(b.getSimpleInfo());
                buoysSimpleInfo.add(Serialization.getFieldsByAnnotation(buoy, BuoySimpleInfo.class));
            }
            JSONObject jsonAnswer = new JSONObject();
            jsonAnswer.put("simpleBuoyData", buoysSimpleInfo);
            ServletUtil.sendJsonAsnwer(jsonAnswer, response);
        } catch (Exception e) {

            e.printStackTrace();
            ServletUtil.sendError(ServerErrorCode.SERVER_ERROR.getCode(), "Server Error", response);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
