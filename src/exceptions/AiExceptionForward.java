/**
 * @Titre :        AiExceptionForward
 * @Description :  Implementation of AiExceptionForward
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package exceptions;

public class AiExceptionForward extends AiException{
    public AiExceptionForward(String message, ErrorLevel errorLevel) {
        super(message, errorLevel, ErrorMethode.FORWARD);
    }
}
