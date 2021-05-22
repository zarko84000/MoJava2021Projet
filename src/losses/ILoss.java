/**
 * @Titre :        ILoss
 * @Description :  Implementation of interface ILoss for the MultiLayer Perceptron
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package losses;


/**
 * Interface that declare the principal method that needs to be in the derived cass.
 */
public interface ILoss {
    double[] loss(double[] y, double[] ypred) throws Exception;
}
