package exceptions;

public class AiExceptionLoss extends AiException{
    public AiExceptionLoss(String message, ErrorLevel errorLevel) {
        super(message, errorLevel, ErrorMethode.LOSS);
    }
}
