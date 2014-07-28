package hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.junit.Assert;
import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.hibernate.HibernateUtil;
import su.systserv.smartBuoy.web.servlet.GetBuoySettingsServlet;
import su.systserv.smartBuoy.web.servlet.JsonUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.07.14
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class Test extends Assert {
    private static final Logger log = Logger.getLogger(Test.class.getName());

    @org.junit.Test
    public void test(){
        SessionFactory sessionFactory = sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Buoy buoy = null;
        buoy = (Buoy)session.get(Buoy.class, 1);
        try{
            log.info("Data before change" + buoy.toJSON());
            buoy.setVoltage(25.0);
            session.save(buoy);
            session.getTransaction().commit();
            log.info("Data after change" + buoy.toJSON());
        }catch(JSONException e){
            e.printStackTrace();
        }finally {
            session.close();
        }

        log.info("New Session");
        session = sessionFactory.openSession();
        session.beginTransaction();
        buoy = null;
        buoy = (Buoy)session.get(Buoy.class, 1);
        try{
            log.info("Data before change" + buoy.toJSON());
        }catch(JSONException e){
            e.printStackTrace();
        }finally {
            session.close();
        }


    }
}
