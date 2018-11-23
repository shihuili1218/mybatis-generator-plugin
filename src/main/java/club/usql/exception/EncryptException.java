package club.usql.exception;

/**
 * @author far.liu
 */
public class EncryptException extends GeneratorException {
    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }
}
