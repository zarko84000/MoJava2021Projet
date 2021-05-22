/**
 * @Titre :        LayerLinear
 * @Description :  Implementation of class LayerLinear for the MultiLayer Perceptron
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */

package layers;

import exceptions.AiException;
import exceptions.AiExceptionBackward;
import exceptions.AiExceptionForward;
import exceptions.ErrorLevel;
import transfertFunctions.ITransfertFunction;
import utils.IInitialiseBias;
import utils.IInitialiseWeights;
import neurons.NeuronLinear;

import java.util.*;


/**
 * Public class LayerLinear that is used in the MultiLayer Perceptron for stocking LinearNeurons and computes backward &
 * forward on each neurons.
 *
 */
public class LayerLinear extends ILayer<NeuronLinear> {
    private List<NeuronLinear> arrayNeurons;
    private int outputSize;


    /**
     * Constructor of the class LayerLinear.
     * Initialize arrayNeurons with a LinkedList<>() but this can be change with whatever you want that is a List<>().
     * Fill arrayNeurons with n = outputSize neurons that are define with (lr, inputSize, initWeights, initBias, tf).
     *
     * @param inputSize   Input size of neurons
     * @param outputSize  Numbers of neurons in one layer
     * @param lr          Learning Rate of neurons
     * @param initWeights Weights of neurons
     * @param initBias    Bias of neurons
     * @param tf          Activate Function of neurons
     * @throws Exception throw when outputSize <= 0, inputSize <= 0, tf/initBias/initWeights are null.
     */
    public LayerLinear
    (int inputSize,
     int outputSize,
     double lr,
     IInitialiseWeights initWeights,
     IInitialiseBias initBias,
     ITransfertFunction tf)
            throws Exception {

        if (outputSize <= 0) throw new Exception("outputSize is equal or under 0 in constructor of LayerLinear");
        if (inputSize <= 0) throw new Exception("inputSize is equal or under 0 in constructor of LayerLinear");
        if (tf == null) throw new Exception("Activation Function is null in LayerLinear");
        if (initBias == null) throw new Exception("Bias is null in LayerLinear");
        if (initWeights == null) throw new Exception("initialiseWeights is null in LayerLinear");

        this.outputSize = outputSize;
        arrayNeurons = new LinkedList<>(); /* ArrayList(), LinkedList()... */

        for (int i = 0; i < outputSize; i++) {
            arrayNeurons.add(
                    new NeuronLinear(
                            lr,
                            inputSize,
                            initWeights,
                            initBias,
                            tf
                    )
            );
        }
    }

    /**
     * Forward pass on each neurons in the layer.
     *
     * @param input
     * @return out double[]
     * @throws AiExceptionForward throw when input is null or input is empty.
     */
    @Override
    public double[] forward(double[] input) throws AiException {
        if (input == null) throw new AiExceptionForward("Input is null in ", ErrorLevel.LAYER);
        if (input.length <= 0) throw new AiExceptionForward("Input length is equal or under 0 in", ErrorLevel.LAYER);

        double[] out = new double[arrayNeurons.size()];
        int pos = 0;

        for (NeuronLinear neuron : this) {
            out[pos++] = neuron.forward(input);
        }
        return out;
    }

    /**
     * Backward pass on each neurons in the layer.
     *
     * @param dy double[]
     * @return double[]
     * @throws AiExceptionBackward throw when dy is null or empty.
     */
    @Override
    public double[] backward(double[] dy) throws AiExceptionBackward {
        if (dy == null) throw new AiExceptionBackward("dy is null in ", ErrorLevel.LAYER);
        if (dy.length <= 0) throw new AiExceptionBackward("dy length is equal or under 0 in ", ErrorLevel.LAYER);

        int pos = 0;
        double[] dxt = new double[arrayNeurons.get(0).getWSize()];
        for (NeuronLinear neuron : this) {
            dxt = neuron.backward(dy[pos++], dxt);
        }
        return dxt;
    }


    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     *
     * Reference : https://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/java/util/ArrayList.java
     */
    @Override
    public Iterator<NeuronLinear> iterator() {

        class Itr implements Iterator<NeuronLinear> {
            int cursor;       // index of next element to return
            int lastRet = -1; // index of last element returned; -1 if no such


            @Override
            public boolean hasNext() {
                return cursor != outputSize;
            }


            @Override
            public NeuronLinear next() {
                int i = cursor;
                cursor = i + 1;

                return arrayNeurons.get(lastRet = i);
            }
        }
        return new Itr();
    }
}