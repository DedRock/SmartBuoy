package su.systserv.smartBuoy.web.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 22.07.14
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */
public class FileAccessTEst {

    public static void main(String...args){

        String data = "134567899";

        // HTML-код новой строки с пинятыми данными для таблицы на странице "inputdata.html"
        StringBuilder newTableRow = new StringBuilder();
        newTableRow.append("\n  <tr>");
        newTableRow.append("\n    <td>");
        newTableRow.append("\n        " + new SimpleDateFormat("dd.MM.yy HH.mm.ss").format(new Date()));
        newTableRow.append("\n    </td>");
        newTableRow.append("\n    <td>");
        newTableRow.append("\n        " + data);
        newTableRow.append("\n    </td>");
        newTableRow.append("\n  </tr>\n");

        System.out.println("HTML-код новой строки таблицы: \n" + data);

        // Открываем текст страницы "inputdata.html"
        RandomAccessFile file = null;
        try {
            System.out.println("Openning file...");
            file = new RandomAccessFile("../inputdata.html", "rw");
            System.out.println("File is open");
            // Найти строку: </tr> - конец заголовка таблицы, начало строк данных
            String curLine;
            System.out.println("Finding...");

            file.writeChars("TEST WRITE");
            /*
            while (( (curLine = file.readLine()) != null)){
                System.out.println("CurLine = " + curLine);
                if ( curLine.indexOf("</tr>") != -1){
                    System.out.println("Finded: \"</tr> ");
                    // Записываем в файл HTML-код новой строки с принятыми данными
                    file.writeChars(newTableRow.toString());
                    System.out.println("Записали новую строку");
                    break;
                }
            }
            */
            System.out.println(file.readLine());
            System.out.println("END");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e){
            e.printStackTrace();
        } finally {
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
