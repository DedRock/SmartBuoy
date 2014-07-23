package su.systserv.smartBuoy.web.servlet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
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
        System.out.println("Input Data size = " + dataLen);
        //System.out.println("Input Data type = " + request.getContentType());
        String rootDir = request.getContextPath();

        RandomAccessFile file = null;
        try {
            // Read Content-data
            char[]inputData = new char[dataLen];
            request.getReader().read(inputData);

            // HTML-код новой строки с пинятыми данными для таблицы на странице "inputdata.html"
            StringBuilder newTableRow = new StringBuilder();
            newTableRow.append("\n  <tr>");
            newTableRow.append("\n    <td style=\"text-align: center\">");
            newTableRow.append("\n        " + new SimpleDateFormat("dd.MM.yy HH.mm.ss").format(new Date()));
            newTableRow.append("\n    </td>");
            newTableRow.append("\n    <td style=\"text-align: left\">");
            newTableRow.append("\n        " + new String(inputData));
            newTableRow.append("\n    </td>");
            newTableRow.append("\n  </tr>\n");
            int newTableRowSize = newTableRow.length();

            // Открываем файл html-страницы "inputdata.html"
            file = new RandomAccessFile("../webapps" + rootDir + "/inputdata.html", "rw");
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            // Close file, if it has been opened
            if (file != null){
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

}
