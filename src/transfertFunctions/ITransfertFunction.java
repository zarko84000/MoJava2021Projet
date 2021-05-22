/**
 * @Titre :        INeuron
 * @Description :  Implementation of INeuron
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package transfertFunctions;

/**
 * Implementation of abstract class ITransfertFunction that define the main methods.
 */
public interface ITransfertFunction {
    double ft(double v);

    double dft(double v);
}
