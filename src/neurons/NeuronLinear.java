/**
 * @Titre :        INeuron
 * @Description :  Implementation of INeuron
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package neurons;

import exceptions.*;
import transfertFunctions.ITransfertFunction;
import utils.IInitialiseBias;
import utils.IInitialiseWeights;


/**
 * Public Class NeuronLinear that extends INeuron.
 * It's a linear neuron.
 * The forward computation is :
 * z = activationFunction(W*X + b)
 */
public class NeuronLinear extends INeuron {

    private double[] w; //weights of our neuron
    private double b; // bias
    private double lr; //learning rate
    private double[] xt;
    private double yt;
    private ITransfertFunction tf;


    /**
     * NeuronLinear Constructor.
     *
     * @param lr        is the learning rate of our neuron.
     * @param inputSize numbers of Weights for our neuron (One w for one input).
     * @param initW     initW randomly.
     * @param initB     initB randomly.
     * @param tf        activation function.
     * @throws AiException is throw when the lr is under 0,
     *                     when the inputSize is <= 0 (we don't compute the forward pass for 0 input),
     *                     initB, initW, tf is null.
     */
    public NeuronLinear(double lr, int inputSize, IInitialiseWeights initW,
                        IInitialiseBias initB, ITransfertFunction tf) throws Exception {
        if (lr < 0) throw new AiException("Learning Rate lr is bellow 0 ", ErrorLevel.NEURON, ErrorMethode.CONSTRUCTOR);
        if (inputSize <= 0)
            throw new AiException("inputSize is equal or under 0 in", ErrorLevel.NEURON, ErrorMethode.CONSTRUCTOR);
        if (initB == null)
            throw new AiException("IInitialiseBias initB is null in", ErrorLevel.NEURON, ErrorMethode.CONSTRUCTOR);
        if (initW == null)
            throw new AiException("IInitialiseWeights initW is null in", ErrorLevel.NEURON, ErrorMethode.CONSTRUCTOR);
        if (tf == null)
            throw new AiException("ITransfertFunction tf is null in", ErrorLevel.NEURON, ErrorMethode.CONSTRUCTOR);

        w = initW.initWeights(inputSize);
        b = initB.initBias();
        this.lr = lr;
        this.tf = tf;
    }

    /**
     * Forward pass of our neuron.
     * Compute : tf(W*X + b)
     *
     * @param input double[]
     * @return yt
     */
    @Override
    public double forward(double[] input) throws AiException {
        if (input == null) throw new AiExceptionForward("double[] input is null in ", ErrorLevel.NEURON);
        if (input.length == 0)
            throw new AiExceptionForward("double[] input is empty in ", ErrorLevel.NEURON);

        xt = input;
        double output = 0.0;

        for (int i = 0; i < input.length; i++) {
            output += w[i] * input[i];

        }


        yt = tf.ft(output + b);

        return yt;
    }

    /**
     * Compute backward for the neuron.
     *
     * @param dy  double
     * @param dxt double[]
     * @return dxt double[]
     * @throws AiExceptionBackward when dxt is null or when dxt have a length diffrent than w.
     */
    @Override
    public double[] backward(double dy, double[] dxt) throws Exception {
        if (dxt == null) throw new AiExceptionBackward("double[] dxt is null in ", ErrorLevel.NEURON);
        if (dxt.length != w.length)
            throw new AiExceptionBackward("double[] dxt have a length different than w in ", ErrorLevel.NEURON);

        double dxty = this.tf.dft(this.yt) * dy;
        for (int i = 0; i < w.length; i++) {
            dxt[i] += this.w[i] * dxty;
            this.w[i] += this.xt[i] * dxty * this.lr;
        }

        this.b += dy * this.lr;
        return dxt;
    }

    /**
     * @return w.length
     */
    @Override
    public int getWSize() {
        return w.length;
    }


}

