/**
 * @Titre :        AiExceptionBacward
 * @Description :  Implementation of AiExceptionBackward
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package exceptions;

public class AiExceptionBackward extends AiException{
    public AiExceptionBackward(String message, ErrorLevel errorLevel) {
        super(message, errorLevel, ErrorMethode.BACKWARD);
    }
}
