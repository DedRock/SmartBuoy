package su.systserv.smartBuoy.modem.exceptions;

/**
 * Класс исключения, возникающего при возврате сообщения об ошибке в ответ на AT-команду
 */
public class ATCommandError extends Exception {

    public ATCommandError(String message){
        super(message);
    }
}
