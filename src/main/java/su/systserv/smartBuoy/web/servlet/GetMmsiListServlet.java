package su.systserv.smartBuoy.web.servlet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import su.systserv.smartBuoy.hibernate.HibernateUtil;
import su.systserv.smartBuoy.web.enums.ServerErrorCode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Получение идентификаторов всех буев
 */
public class GetMmsiListServlet extends HttpServlet {

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
            session.beginTransaction();
            List<Integer> mmsi = (List<Integer>)session.createQuery("select mmsi from Buoy").list();
            JSONObject jsonAnswer = new JSONObject();
            jsonAnswer.put("mmsi", mmsi);
            ServletUtil.sendJsonAsnwer(jsonAnswer, response);
        } catch (Exception e) {
            ServletUtil.sendError(ServerErrorCode.SERVER_ERROR.getCode(), "Server Error", response);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}
