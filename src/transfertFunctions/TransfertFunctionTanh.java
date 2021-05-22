/**
 * @Titre :        TransfertFunctionTanh
 * @Description :  Implementation of Tanh Activation Function.
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package transfertFunctions;

/**
 * Implementation of main function of tanh function.
 */
public class TransfertFunctionTanh implements ITransfertFunction {

    @Override
    public double ft(double v) {
        double e = Math.exp(v);
        double me = Math.exp(-v);
        return ((e - me) / (e + me));
    }

    @Override
    public double dft(double v) {
        return (1 - v * v);
    }
}
