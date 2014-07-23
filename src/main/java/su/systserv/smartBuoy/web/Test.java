package su.systserv.smartBuoy.web;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.buoy.entity.MeteoBuoy;
import su.systserv.smartBuoy.hibernate.HibernateUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 20.05.14
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    // Log4j logger
    private static final Logger log = Logger.getLogger(Test.class.getName());

    public static void main(String...args){

        List<MeteoBuoy> entities = new ArrayList<MeteoBuoy>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            session.beginTransaction();

            Query query = session.createQuery("from Buoy where simNumber = 9117350627 ");
            List<Buoy> list = query.list();
            System.out.println(list);

            // Получение знаечния по имени поля
            Field field = Buoy.class.getDeclaredField("name");
            Buoy curBuoy = list.get(0);
            field.setAccessible(true);
            System.out.println(field.get(curBuoy));
            field.setAccessible(false);

            System.out.println("Result of type checking = " + checkValueType(java.lang.Byte.class, "200"));


            /*
            // Create Buoy by factory
            Buoy newBuoy  = BuoyFactory.cretaeBuoy( 3, "111222333");
            if (newBuoy != null){
                session.save(newBuoy);
                session.getTransaction().commit();
            }
            */

            //Buoy b = new Buoy();
            //b = (Buoy) session.load(Buoy.class, 123);
            //b = (Buoy)session.createCriteria(Buoy.class).add( Restrictions.idEq(123)).uniqueResult();
            //Buoy b = (Buoy)session.load(Buoy.class, 2);
            //MeteoBuoy m = (MeteoBuoy) session.load(MeteoBuoy.class, 123);
            //log.info(((Buoy)b).toJSON());
            //log.info(m.toJSON());


            /*
            // Get info from DB
            MeteoBuoy m;
            m = (MeteoBuoy) session.load(MeteoBuoy.class, 1);
            log.info(m.toJSON());
            m = (MeteoBuoy) session.load(MeteoBuoy.class, 2);
            log.info(m.toJSON());
            m = (MeteoBuoy) session.load(MeteoBuoy.class, 123);
            log.info(m.toJSON());

            Buoy b;
            b = (Buoy) session.load(Buoy.class, 1);
            log.info(b.toJSON());
            b = (Buoy) session.load(Buoy.class, 2);
            log.info(b.toJSON());
            b = (Buoy) session.load(Buoy.class, 123);
            log.info(b.toJSON());
            */
            /*
            List<MeteoBuoy> buoy = (List<MeteoBuoy>)session.createQuery("select from MeteoBuoy where id = " + id).list();
            */


            /*
            // Serialization
            MeteoBuoy b = BuoyFactory.cretaeMeteoBuoy(123, "1234567890");
            b.setBatteryNeedToChange(true);
            b.setBatteryResource((byte)2);
            b.setFlashBrightness((byte)50);
            b.setFlashNumberInSeries((byte)3);
            b.setAirTemp(25.6f);
            log.info(b.toJSON());
            */
            /*
            // Get ID of all buoys
            List<Integer> ids = (List<Integer>)session.createQuery("select id from Buoy").list();
            //List<String> names = (List<String>)session.createQuery("select stud.name from Student stud order by name").list()

            log.info(ids);
            */


            /*
            entities.add(new MeteoBuoy(1, "9117350627", BuoyType.METEO.getId()));
            //entities.add(new MeteoBuoy(2, "911XXXXXXX", BuoyType.METEO.getId()));
            //entities.add(new MeteoBuoy(3, "1234545646", BuoyType.METEO.getId()));


            // Adding all objects in table
            for(Buoy buoy : entities)
                session.save(buoy);
            */

            /*
            // Change Data
            entities.get(2).setText("Three");
            session.update(entities.get(2));
            */



        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }


        /*
        try {
            LocalDataStore.getInstance().execute("INSERT buoys(id, simNumber, type) VALUES(1, 9117350627, 0)");
        } catch (SQLException e){
            System.out.println("SQL error: " + e.getMessage());
            //e.printStackTrace();
        } catch (DbConnectException e) {
            System.out.println("Database connection error: " + e.getMessage());
            //e.printStackTrace();
        }
        */
    }

    /**
     * Функция проверки приведения типа параметра из строкового значения
     * @param settingClass - класс типа параметра
     * @param value - строковое значение параметра
     * @return
     *  true - приведение типа возможно
     *  false - приведение типа невозможно
     */
    static boolean checkValueType(Class settingClass, String value){
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
