package su.systserv.smartBuoy.web.exceptions;

/**
 * Класс исключения, для ошибок возникающих в процессе выполнения сервисов
 */
public class ServletExecutionException extends Exception {
    public ServletExecutionException(String message){
        super(message);
    }
}
