package su.systserv.smartBuoy.web.servlet;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import su.systserv.smartBuoy.buoy.Serialization;
import su.systserv.smartBuoy.buoy.annotations.BuoySetting;
import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.hibernate.HibernateUtil;
import su.systserv.smartBuoy.web.enums.ServerErrorCode;
import su.systserv.smartBuoy.web.exceptions.ServletExecutionException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Предоставление данных буя
 */
public class GetBuoySettingsServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(GetBuoySettingsServlet.class.getName());

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

            // Убедиться что искомый номер сим-карты зарегистрирован за каким-то буем
            Query query = session.createQuery("from Buoy where mmsi = " + buoyMmsi);
            List<Buoy> buoyList = query.list();
            if (buoyList.isEmpty()){
                throw new ServletExecutionException("sim-card number is not found !");
            } if ( buoyList.size() > 1) {
                throw new ServletExecutionException(" sim-card number is not identical !");
            }

            /*
            Buoy buoy;
            try{
                log.info("Get buot with mmsi = " + buoyMmsi);
                buoy = (Buoy)session.load(Buoy.class, buoyMmsi);
            } catch (ObjectNotFoundException e){
                throw new MyServerException( ServerErrorCode.SERVER_ERROR, "Record with MMSI# " + buoyMmsi + "is not found in table \"Buoy\"");
            }
            JSONObject answer;// = new JSONObject();
             */
            // Основная информация о буе
            JSONObject answer = Serialization.getFieldsByAnnotation(buoyList.get(0), BuoySetting.class);/*buoy.toJSON();*/
            log.info("JSON-settitings of buoy:" + answer);
            /*
            // Если текущий буй является метеорологическим, добавляем в ответ расширенную информацию
            if (buoy.getBuoyTypeId() == BuoyType.METEO.getId()){
                MeteoBuoy meteoBuoy = buoy.getMeteoBuoy();
                if (meteoBuoy == null){
                    throw new MyServerException( ServerErrorCode.SERVER_ERROR, "Cannot find meteo-information from buoy with MMSI# " + buoyMmsi);
                }
                answer = meteoBuoy.toJSON(answer);
            }
            */
            ServletUtil.sendJsonAsnwer(answer, response);
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
