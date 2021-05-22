/**
 * @Titre :        AiException
 * @Description :  Implementation of AiException
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
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
