/**
 * @Titre :        InitialiseBiasNormal
 * @Description :  Implementation of InitialiseBiasNormal.
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package utils;

import java.lang.Math;

/**
 * Public class InitialiseBiasNormal that implements IInitialiseBias.
 * This is just a random bias.
 */
public class InitialiseBiasNormal implements IInitialiseBias {
    @Override
    public double initBias() {
        return Math.random();
    }
}

