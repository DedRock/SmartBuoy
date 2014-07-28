package su.systserv.smartBuoy.web.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 20.05.14
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public enum ServerErrorCode{
    CLIENT_QUERY_ERROR (1000),
    SERVER_ERROR(1002);

    private int code;
    ServerErrorCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

}
