package su.systserv.smartBuoy.web.servlet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.json.JSONObject;
import su.systserv.smartBuoy.buoy.entity.Buoy;
import su.systserv.smartBuoy.hibernate.HibernateUtil;
import su.systserv.smartBuoy.web.enums.ServerErrorCode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

/**
 * Сервлет для приёма входящих данных от буёв
 *
 * Формат принимаемых данных - JSON.
 * Структура:
 * {
 *     "mmsi": number,
 *      "data" :
 * }
 *
 */
public class InputDataServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(GetBuoySettingsServlet.class.getName());

    SessionFactory sessionFactory = null;

    @Override
    public void init(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response){
        final int dataLen = request.getContentLength();
        String contentType = request.getContentType();
        System.out.println("Input Data size = " + dataLen);
        System.out.println("Input Data type = " + contentType);

        try {
            char[]inputData = new char[dataLen];
            request.getReader().read(inputData);

            // добавляем принятые данные на страницу "История принятых данных"
            addRowOfNewData("../webapps" + request.getContextPath() + "/inputdata.html", inputData);


            //===== updateInfo ======================================================================
            if (contentType.equals("updateInfo")){
                JSONObject jsonData = null;
                try{
                    String data = new String(inputData);
                    System.out.println(data);
                    //jsonData = new JSONObject(new String(inputData));
                    jsonData = new JSONObject(data);
                }catch (JSONException e){
                    log.error("Received data is not a JSON-object");
                    return;
                }
                //===== Берём идентификатор буя из сообщения =====
                Integer mmsi = JsonUtils.getJsonInt(jsonData, "mmsi");
                if (mmsi!= null){
                    updateBuoyParameters(mmsi, jsonData);
                }
                else{
                    log.error("Can't find \"mmsi\" in updateInfo-message");
                    return;
                }
            }
        } catch (IOException e){
            log.error("IOException of request.Reader");
        }

    }

    /**
     * Функция добавления строки в таблицу с историей принятых данных
     * @param filename
     * @param data
     */
    void addRowOfNewData(String filename, char[] data){

        RandomAccessFile file = null;
        try {
            // Read Content-data

            // HTML-код новой строки с пинятыми данными для таблицы на странице "inputdata.html"
            StringBuilder newTableRow = new StringBuilder();
            newTableRow.append("\n  <tr>");
            newTableRow.append("\n    <td style=\"text-align: center\">");
            newTableRow.append("\n        " + new SimpleDateFormat("dd.MM.yy HH.mm.ss").format(new Date()));
            newTableRow.append("\n    </td>");
            newTableRow.append("\n    <td style=\"text-align: left\">");
            newTableRow.append("\n        " + new String(data));
            newTableRow.append("\n    </td>");
            newTableRow.append("\n  </tr>\n");
            int newTableRowSize = newTableRow.length();

            // Открываем файл html-страницы "inputdata.html"
            file = new RandomAccessFile(filename, "rw");
            long oldFileSize = file.length();
            // Найти строку: </tr> - конец заголовка таблицы, начало строк данных
            char curChar;
            StringBuilder pageData = new StringBuilder();
            int byteCounter = 0;
            while (( (curChar = (char)file.readByte()) != -1)){
                pageData.append(curChar);
                byteCounter++;

                // Когда нашли место, куда будем вставлять новую строку таблицы
                if ( pageData.indexOf("</tr>") != -1){

                    // Копируем оставшуюся часть страницы в буфер
                    byte[] buffer = new byte[(int)(oldFileSize-byteCounter)];
                    file.seek(byteCounter);
                    file.readFully(buffer);

                    // Задаём файлу новую длину
                    file.setLength(oldFileSize + newTableRowSize);

                    // Записываем в файл HTML-код новой строки с принятыми данными
                    file.seek(byteCounter);
                    file.write(newTableRow.toString().getBytes());
                    //file.writeChars(newTableRow.toString());

                    // Пишем в конец файла оставшуюся часть страницы
                    file.seek(byteCounter + newTableRowSize);
                    file.write(buffer);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            log.error("File \"" + filename + "\" not found.");
        } catch (IOException e){
            //e.printStackTrace();
            log.error("IOException");
        } finally {
            // Close file, if it has been opened
            if (file != null){
                try {
                    file.close();
                } catch (IOException e) {
                    log.error("Can't close file: \"" + filename + "\"");
                }
            }
        }
    }


    /**
     * Обновить параметры буя
     * @param mmsi - идентификатор MMSI
     * @param jsonData - JSON-object с обновлёнными значениями параметров
     */
    void updateBuoyParameters(Integer mmsi, JSONObject jsonData){
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Buoy buoy = null;
            buoy = (Buoy)session.get(Buoy.class, mmsi);
            if (buoy != null){
                //===== Напряжение питания =====
                Double voltage = JsonUtils.getJsonDouble(jsonData, "voltage");
                if (voltage != null){
                    buoy.setVoltage(voltage);
                }else{
                    log.error("\"Can't find parameter\"voltage\" in updateInfo-message\"");
                }
                //===== Координаты =====
                Double curLatitude = JsonUtils.getJsonDouble(jsonData, "curLatitude");
                if (curLatitude != null){
                    buoy.setCurLatitude(curLatitude);
                }else{
                    log.error("\"Can't find parameter\"curLatitude\" in updateInfo-message\"");
                }
                Double curLongtitude = JsonUtils.getJsonDouble(jsonData, "curLongtitude");
                if (curLongtitude != null){
                    buoy.setCurLongtitude(curLongtitude);
                }else{
                    log.error("\"Can't find parameter\"curLongtitude\" in updateInfo-message\"");
                }
            } else {
                log.error("Can't find buoy with mmsi = " + mmsi);
            }
            session.save(buoy);
            session.getTransaction().commit();
            log.info("buoy # " + mmsi + " :\n" + buoy);
            log.info("Data of buoy mmsi = " + mmsi + " has been updated");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}
