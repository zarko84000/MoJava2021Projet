/**
 * @Titre :        TransfertFunctionLeakyRelu
 * @Description :  TransfertFunctionLeakyRelu
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package transfertFunctions;

/**
 * LeakyRelu activation function.
 *
 * References : https://www.machinecurve.com/index.php/2019/10/15/leaky-relu-improving-traditional-relu/
 * https://stats.stackexchange.com/questions/275521/what-is-the-derivative-of-leaky-relu
 */
public class TransfertFunctionLeakyRelu implements ITransfertFunction{
    private double c;

    public TransfertFunctionLeakyRelu(double c) {
        this.c = c;
    }

    @Override
    public double ft(double v) {
        if (v<0) return c*v;
        return v;
    }

    @Override
    public double dft(double v) throws Exception {
        if (v > 0) return 1;
        return c;
    }
}
