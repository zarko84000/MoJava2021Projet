/**
 * @Titre :        LossDifference
 * @Description :  Implementation of public class LossDifference for the MultiLayer Perceptron
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package losses;


import exceptions.AiExceptionLoss;
import exceptions.ErrorLevel;

/**
 * Public class LossDifference that implements ILoss.
 * The main purpose of this class is to calculate the loss based on the difference of the true value and the y
 * prediction.
 */
public class LossDifference implements ILoss {


    /**
     * @param y     double[] that is the true prediction.
     * @param ypred double[] that is the prediction made by the MultiLayer Perceptron.
     * @return diff double[] that is the difference of each values y[i] & ypred[i].
     * @throws Exception throw when y or ypred is null or when y.length is different thant ypred.length.
     */
    @Override
    public double[] loss(double[] y, double[] ypred) throws Exception {
        if (y == null) throw new AiExceptionLoss("y array is null in ", ErrorLevel.LOSS);
        if (ypred == null) throw new AiExceptionLoss("y array is null in ", ErrorLevel.LOSS);
        if (y.length != ypred.length)
            throw new AiExceptionLoss("y.length and ypred.length don't have the same length in ", ErrorLevel.LOSS);

        double[] diff = new double[y.length];
        for (int i = 0; i < y.length; i++) {
            diff[i] = y[i] - ypred[i];
        }

        return diff;
    }
}
