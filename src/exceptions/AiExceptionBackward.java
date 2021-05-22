package exceptions;

public class AiExceptionBackward extends AiException{
    public AiExceptionBackward(String message, ErrorLevel errorLevel) {
        super(message, errorLevel, ErrorMethode.BACKWARD);
    }
}
