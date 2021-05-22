/**
 * @Titre :        IModel
 * @Description :  Implementation of interface IModel for the MultiLayer Perceptron
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 *
 * @author : Adel Moumen
 * @version : 1.0
 */
package models;

import exceptions.AiException;

/**
 * Interface IModel that define the main structure of our model (and future one).
 */
public interface IModel {
    double[] forward(double[] input) throws AiException;
    double backward(double[] output, double[] predicted) throws Exception;
    void learn(double []ytrue) throws Exception;
}
