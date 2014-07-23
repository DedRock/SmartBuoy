package su.systserv.smartBuoy.web.servlet;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.buoy.BuoyType;
import su.systserv.smartBuoy.buoy.entity.MeteoBuoy;
import su.systserv.smartBuoy.hibernate.HibernateUtil;
import su.systserv.smartBuoy.web.exceptions.MyServerException;
import su.systserv.smartBuoy.web.enums.ServerErrorCode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Предоставление данных буя
 */
public class GetBuoyDataServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(GetBuoyDataServlet.class.getName());

    SessionFactory sessionFactory = null;

    @Override
    public void init(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * В Http-запросе должен содержаться запрос в JSON-формате со структурой:
     * {
     *     "mmsi" : Number
     * }
     *
     * @param request
     * @param response
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response){
        log.info("=== Service ===");
        //===== Проверка параметров пользовательского запроса ==============================
        Integer buoyMmsi = ServletUtil.getIntParameter("mmsi", request);
        if (buoyMmsi == null) {
            ServletUtil.sendError(ServerErrorCode.CLIENT_QUERY_ERROR.getCode(),"Не указан MMSI номер буя",  response);
            return;
        }

        //===== Изъятие из базы данных информации о интересующем буе
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Buoy buoy;
            try{
                log.info("Get buot with mmsi = " + buoyMmsi);
                buoy = (Buoy)session.load(Buoy.class, buoyMmsi);
            } catch (ObjectNotFoundException e){
                throw new MyServerException( ServerErrorCode.SERVER_ERROR, "Record with MMSI# " + buoyMmsi + "is not found in table \"Buoy\"");
            }
            JSONObject answer = new JSONObject();

            // Основная информация о буе
            answer = buoy.toJSON();
            log.info("JSON-data of buoy:" + answer);

            // Если текущий буй является метеорологическим, добавляем в ответ расширенную информацию
            if (buoy.getBuoyTypeId() == BuoyType.METEO.getId()){
                MeteoBuoy meteoBuoy = buoy.getMeteoBuoy();
                if (meteoBuoy == null){
                    throw new MyServerException( ServerErrorCode.SERVER_ERROR, "Cannot find meteo-information from buoy with MMSI# " + buoyMmsi);
                }
                answer = meteoBuoy.toJSON(answer);
            }
            ServletUtil.sendJsonAsnwer(answer, response);

        } catch (MyServerException e){
            ServletUtil.sendError( e.getErrorCode(), e.getMessage(), response);
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            ServletUtil.sendError( ServerErrorCode.SERVER_ERROR.getCode(),"Server Error: " + e.getMessage() , response);

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
