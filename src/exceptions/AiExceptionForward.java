package exceptions;

public class AiExceptionForward extends AiException{
    public AiExceptionForward(String message, ErrorLevel errorLevel) {
        super(message, errorLevel, ErrorMethode.FORWARD);
    }
}
