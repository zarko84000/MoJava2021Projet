package exceptions;


public class AiException extends Exception {

    ErrorLevel errorLevel;
    ErrorMethode errorMethode;

    public AiException(String message, ErrorLevel errorLevel, ErrorMethode errorMethode) {
        super(message);
        this.errorLevel = errorLevel;
        this.errorMethode = errorMethode;

    }

    @Override
    public String getMessage() {
        return super.getMessage() + errorLevel + "->" + errorMethode;
    }
}
