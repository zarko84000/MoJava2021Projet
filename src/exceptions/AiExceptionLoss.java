/**
 * @Titre :        AiExceptionLoss
 * @Description :  Implementation of AiExceptionLoss
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package exceptions;

public class AiExceptionLoss extends AiException{
    public AiExceptionLoss(String message, ErrorLevel errorLevel) {
        super(message, errorLevel, ErrorMethode.LOSS);
    }
}
