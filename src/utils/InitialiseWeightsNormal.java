/**
 * @Titre :        InitialiseWeightsNormal
 * @Description :  Implementation of InitialiseWeightsNormal.
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package utils;

import java.lang.Math;

/**
 * Public class InitialiseWeightsNormal that implements IInitialiseWeights.
 * Each w[i] is randomly choose.
 */
public class InitialiseWeightsNormal implements IInitialiseWeights {

    /**
     * @param size of the weights
     * @return weights that are randomly init
     * @throws Exception when size is 0 or under
     */
    @Override
    public double[] initWeights(int size) throws Exception {
        if (size <= 0) throw new Exception("int size in initWeights is 0 or under.");
        double[] w = new double[size];

        for (int i = 0; i < w.length; i++) {
            w[i] = Math.random();
        }

        return w;
    }
}
