/**
 * @Titre :        INeuron
 * @Description :  Implementation of INeuron
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package neurons;


import exceptions.AiException;

/**
 * Implementation of abstract class INeuron that define the main methods.
 */
public abstract class INeuron {

    public abstract double forward(double[] X) throws AiException;

    public abstract double[] backward(double dy, double[] grads) throws Exception;

    public abstract int getWSize();
}
