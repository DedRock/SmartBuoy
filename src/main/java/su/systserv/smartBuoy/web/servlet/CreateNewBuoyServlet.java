package su.systserv.smartBuoy.web.servlet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.buoy.BuoyType;
import su.systserv.smartBuoy.buoy.entity.MeteoBuoy;
import su.systserv.smartBuoy.hibernate.HibernateUtil;
import su.systserv.smartBuoy.web.enums.ServerErrorCode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Сервлет для регистрации нового буя (внесении записи в БД)
 */
public class CreateNewBuoyServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(CreateNewBuoyServlet.class.getName());

    SessionFactory sessionFactory = null;

    @Override
    public void init(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }


    /**
     * Ожидаемый формат данных (JSON):
     *  {
     *      "mmsi" : Number,
     *      "name" : String,
     *      "buoyType" : Number,
     *      "simNumber" : String
     *  }
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response){
        log.debug("Parameters of query to CreateNewBuoyServlet:");
        //===== Проверка входных параметров запроса ==============================================================
        // mmsi
        Integer mmsi = ServletUtil.getIntParameter("mmsi", request);
        log.debug("mmsi = " + mmsi);
        if (mmsi == null) {
            ServletUtil.sendError(ServerErrorCode.CLIENT_QUERY_ERROR.getCode(),"Не указан MMSI буя",  response);
            return;
        }
        // name
        String name = ServletUtil.getParameterValue("name", request);
        log.debug("name = " + name);
        if (name == null) {
            ServletUtil.sendError(ServerErrorCode.CLIENT_QUERY_ERROR.getCode(),"Не указано имя буя",  response);
            return;
        }

        // buoyType
        Integer buoyTypeId = ServletUtil.getIntParameter("buoyType", request);
        log.debug("buoyType = " + buoyTypeId);
        if (buoyTypeId == null) {
            ServletUtil.sendError(ServerErrorCode.CLIENT_QUERY_ERROR.getCode(),"Не указан тип буя",  response);
            return;
        }
        BuoyType buoyType = BuoyType.getTypeById(Byte.valueOf(buoyTypeId.toString()));
        log.debug("buoyType = " + buoyType);
        if (buoyType == null){
            ServletUtil.sendError(ServerErrorCode.CLIENT_QUERY_ERROR.getCode(),"Неизвестный тип буя",  response);
            return;
        }

        // simNumber
        Long simNumber = ServletUtil.getLongParameter("simNumber", request);
        if (simNumber == null) {
            ServletUtil.sendError(ServerErrorCode.CLIENT_QUERY_ERROR.getCode(),"Не указан номе SIM-карты буя",  response);
            return;
        }

        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            Buoy newBuoy = new Buoy();
            newBuoy.setMMSI(mmsi);
            newBuoy.setName(name);
            newBuoy.setSimNumber(simNumber);
            // Расширяем текущий буй до функций метео-буя
            if (buoyType == BuoyType.METEO){
                newBuoy.setMeteoBuoy(new MeteoBuoy(mmsi));
            }
            /*
            else if (buoyType == BuoyType.ECOLOG){
                newBuoy.setMeteoBuoy(new MeteoBuoy(mmsi));
            }
            */
            session.save(newBuoy);
            session.getTransaction().commit();
        } catch (Exception e){

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
