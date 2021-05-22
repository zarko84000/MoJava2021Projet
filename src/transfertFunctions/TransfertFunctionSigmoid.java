/**
 * @Titre :        TransfertFunctionSigmoid
 * @Description :  Implementation of Sigmoid Activation Function.
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package transfertFunctions;

import java.lang.Math;

/**
 * Implementation of main function of sigmoid function.
 * Sigmoid function take a double in input and map this input to a probability.
 */
public class TransfertFunctionSigmoid implements ITransfertFunction {

    @Override
    public double ft(double v) {
        return (1 / (1 + Math.exp(-v)));
    }

    @Override
    public double dft(double v) {
        return (v * (1 - v));
    }
}
