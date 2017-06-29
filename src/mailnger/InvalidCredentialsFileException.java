package mailnger;

/**
 * Created by Parker on 6/28/2017.
 */
public class InvalidCredentialsFileException extends Exception{
    public InvalidCredentialsFileException(String message){
        super(message);
    }

    public InvalidCredentialsFileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
