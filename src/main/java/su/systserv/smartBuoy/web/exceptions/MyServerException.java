package su.systserv.smartBuoy.web.exceptions;

import su.systserv.smartBuoy.web.enums.ServerErrorCode;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 22.05.14
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 */
public class MyServerException extends Exception {
    private ServerErrorCode errorCode;

    public MyServerException(ServerErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode.getCode();
    }
}
