/**
 * @Titre :        MLP
 * @Description :  Implementation of MultiLayer Perceptron
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package models;

import exceptions.*;
import layers.LayerLinear;
import losses.ILoss;


/**
 * Public Class MLP for MultiLayer Perceptron that implements IModel.
 * <p>
 * A MultiLayer Perceptron is a class of feedforward artificial neural network (ANN).
 * In our case it's a "vanilla" version with a composition of multiple layers of perceptrons.
 * <p>
 * Our MLP consists of : an input layer, at least one hidden layer and an output layer.
 * Except for the input nodes, each node is a neuron that uses a nonlinear activation function (like Sigmoid, Tanh...).
 * With that we can distinguish data that is not linearly separable.
 * <p>
 * References :  Rosenblatt, Frank. x. Principles of Neurodynamics: Perceptrons and the Theory of Brain Mechanisms.
 * Spartan Books, Washington DC, 1961
 */
public class MLP implements IModel {

    private LayerLinear[] layers;
    private ILoss lossFunction;
    public double loss;
    public double[] input;
    private double[] output;
    public double[] predicted;
    public int goodAnswers = 0;


    /**
     * Constructor of the Multilayer Perceptron.
     *
     * @param layers       array of LayerLinear elements.
     * @param lossFunction type of lossFunction in our case LossDifference will be use.
     * @throws AiException throw if null or length is 0.
     */
    public MLP(LayerLinear[] layers, ILoss lossFunction) throws AiException {
        if (layers == null)
            throw new AiException("LayerLinear[] layers is null in ", ErrorLevel.MODEL, ErrorMethode.CONSTRUCTOR);
        if (lossFunction == null)
            throw new AiException("ILoss lossFunction is null in ", ErrorLevel.MODEL, ErrorMethode.CONSTRUCTOR);
        if (layers.length == 0)
            throw new AiException("LayerLinear[] layers is empty in ", ErrorLevel.MODEL, ErrorMethode.CONSTRUCTOR);

        this.layers = layers;
        this.lossFunction = lossFunction;
    }


    /**
     * @param input array of double[] that will take the input of the model. The input is size 3.
     *              <p>
     *              Example : [operand1, operator, operand2]
     *              operator is a double that take only 3 values : 0, 1 or 2
     *              If operator == 0 then it's an addition
     *              operator == 1 then it's an multiplication
     *              and if it's 2 then it's an subtraction.
     *              operand1 & operand2 are double.
     * @return input that is the prediction made by the model.
     * @throws AiExceptionForward throw when input is null or empty.
     */
    @Override
    public double[] forward(double[] input) throws AiException {
        if (input == null) throw new AiExceptionForward("double[] input is null in ", ErrorLevel.MODEL);
        if (input.length == 0)
            throw new AiExceptionForward("double[] input is empty in ", ErrorLevel.MODEL);

        for (int i = 0; i < layers.length; i++) {
            input = layers[i].forward(input);
        }
        return input;
    }

    /**
     * Compute the backpropagation of our model. The main purpose is to send the value of the loss in all
     * layers of our model for changing the parameters W & B of each neurons for learning.
     * The loss function is the error of our model on an prediction.
     *
     * @param output    real value of the input.
     * @param predicted value predicted by our model.
     * @return value of the loss.
     * @throws Exception throw when output/predicted are null or if output/predicted are empty.
     */
    @Override
    public double backward(double[] output, double[] predicted) throws Exception {
        if (output == null) throw new AiExceptionBackward("double[] output is null in ", ErrorLevel.MODEL);
        if (predicted == null) throw new AiExceptionBackward("double[] predicted is null in ", ErrorLevel.MODEL);
        if (output.length == 0) throw new AiExceptionBackward("double[] output is empty in ", ErrorLevel.MODEL);
        if (predicted.length == 0) throw new AiExceptionBackward("double[] predicted is empty in ", ErrorLevel.MODEL);

        double[] loss = this.lossFunction.loss(output, predicted);
        double[] dy = loss;
        for (int i = layers.length - 1; i >= 0; i--) {
            dy = layers[i].backward(dy);
        }
        return getLoss(loss);
    }

    /**
     * Calculate loss for a vector dy.
     *
     * @param dy
     * @return loss/=dy.length
     * @throws AiExceptionLoss throw when dy is null or if dy is empty.
     */
    public double getLoss(double[] dy) throws AiExceptionLoss {
        if (dy == null) throw new AiExceptionLoss("double[] dy is null in ", ErrorLevel.MODEL);
        if (dy.length == 0) throw new AiExceptionLoss("double[] dy is empty in ", ErrorLevel.MODEL);

        loss = 0.0;
        for (int i = 0; i < dy.length; i++) {
            loss += dy[i];
        }
        return loss /= dy.length;
    }

    /**
     * Function that will find the index of the max value in an array of double.
     *
     * @param t array of double[].
     * @return max : index of the max value in t.
     * @throws Exception when t is null or t is empty.
     */
    public static int getMaxIndice(double[] t) throws Exception {
        if (t == null) throw new Exception("double[] t is null in getMaxIndice(doubel[] t) in MLP.java");
        if (t.length == 0) throw new Exception("double[] t is empty in getMaxIndice(doubel[] t) in MLP.java");

        int max = 0;
        double maxValue = -Double.MIN_VALUE;
        for (int i = 0; i < t.length; i++) {
            if (t[i] > maxValue) {
                maxValue = t[i];
                max = i;
            }
        }

        return max;
    }

    /**
     * Function that will train our MLP model.
     * This function just compute the forward pass and the backward pass.
     * This function also print the results of our model and compute the accuracy metrics.
     *
     * @param ytrue double[]
     * @throws Exception is throw when ytrue is null or is empty.
     */
    @Override
    public void learn(double[] ytrue) throws Exception {
        if (ytrue == null) throw new Exception("double[] ytrue is null in MODEL");
        if (ytrue.length == 0) throw new Exception("double[] ytrue is empty in MODEL");

        predicted = forward(input);
        output = predicted;
        loss = backward(ytrue, predicted);

    }


}
