/*
 * @Titre :        Test class
 * @Description :  Test class for the train/validation on different operators.
 * @Copyright :    Copyright (c) 2021
 * @CLasse : CMI L2 Informatique, Avignon Université
 * @author : Adel Moumen
 * @version : 1.0
 */
package test;

import layers.LayerLinear;
import losses.LossDifference;

import models.MLP;
import transfertFunctions.ITransfertFunction;
import transfertFunctions.TransfertFunctionSigmoid;
import transfertFunctions.TransfertFunctionTanh;
import utils.InitialiseBiasNormal;
import utils.InitialiseWeightsNormal;

import java.util.concurrent.ThreadLocalRandom;

public class Test {
    /**
     * Compute train & test on the 3 different operators on the MLP.
     * <p>
     * IMPORTANT NOTE FOR THE USER :
     * If you train/validate your model on OneOperator you need to put the inputSize of your model to 2.
     * If you train/validate your model on All Operator you need to put the inputSize of your model to 3.
     * <p>
     * If you do the Addition :
     * typeOfOperation : 0
     * outputSize : 19
     * <p>
     * If you do the Multiplication :
     * typeOfOperation : 1
     * outputSize : 82
     * <p>
     * If you do the Subtraction:
     * typeOfOperation : 2 or +
     * outputSize : 19
     * <p>
     * If you do the All Operators :
     * outputSize : 91
     *
     */
    public static void main(String[] args) throws Exception {

        /*
        MLP modelAddition = initModel(2, 128, 0.01, new ITransfertFunction[]
                {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 19, 2);
        trainOneOperator(modelAddition, 10_000, 0);
        validationOneOperator(modelAddition, 1_000, 0);


        MLP modelMultiplication = initModel(2, 128, 0.01, new ITransfertFunction[]
                {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 82, 2);
        trainOneOperator(modelMultiplication, 10_000, 1);
        validationOneOperator(modelMultiplication, 1_000, 1);


        MLP modelSubtraction = initModel(2, 128, 0.01, new ITransfertFunction[]
                {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 19, 2);
        trainOneOperator(modelSubtraction, 10_000, 2);
        validationOneOperator(modelSubtraction, 1_000, 2);
        */

        MLP modelAll = initModel(2, 128, 0.01, new ITransfertFunction[]
                {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 91, 3);

        trainAllOperators(modelAll, 1_000_000);
        validationAllOperators(modelAll, 10_000);

    }

    /**
     * @param nbHiddenLayers numbers of layers
     * @param hiddenSize    numbers of neurons in layers
     * @param lr    learning rate
     * @param tfArray   array of TransfertFunction
     * @param outputSize    size of the output
     * @param inputSize     size of the input
     * @return MLP model
     */
    public static MLP initModel(int nbHiddenLayers, int hiddenSize, double lr, ITransfertFunction[] tfArray, int outputSize, int inputSize) throws Exception {
        if (nbHiddenLayers <= 0) throw new Exception("nbHiddenLayers is <= 0 in initModel function from Test file");
        if (hiddenSize <= 0) throw new Exception("hiddenSize is <= 0 in initModel function from Test file");
        if (lr < 0) throw new Exception("lr is < 0 in initModel function from Test file");
        if (tfArray == null) throw new Exception("tfArray is null in initModel function from Test file");
        if (tfArray.length != nbHiddenLayers)
            throw new Exception("tfArray.length != nbHiddenLayers in initModel function from Test file");
        if (outputSize <= 0) throw new Exception("outputSize <= 0 in initModel function from Test file");
        if (inputSize <= 0) throw new Exception("inputSize <= 0 in initModel function from Test file");

        LayerLinear[] layers = new LayerLinear[nbHiddenLayers];


        layers[0] = new LayerLinear(
                inputSize, hiddenSize, lr,
                new InitialiseWeightsNormal(), new InitialiseBiasNormal(), tfArray[0]
        );

        for (int i = 1; i < nbHiddenLayers - 1; i++) {
            layers[i] = new LayerLinear(
                    hiddenSize, hiddenSize, lr,
                    new InitialiseWeightsNormal(), new InitialiseBiasNormal(), tfArray[i]
            );
        }

        layers[nbHiddenLayers - 1] = new LayerLinear(
                hiddenSize, outputSize, lr,
                new InitialiseWeightsNormal(), new InitialiseBiasNormal(), tfArray[nbHiddenLayers - 1]
        );

        System.out.println(layers.length);

        return new MLP(layers, new LossDifference());
    }

    /**
     * @param model           MLP model that need to be init manually or with initModel(...) function.
     * @param n_iter          Numbers of examples for training our model.
     * @param typeOfOperation 0 == Addition, 1 == Multiplication and 2 and + is for subtraction.
     * @throws Exception throw when n_iter is negative, MLP model is null or when typeOfOperation is bellow 0.
     */
    public static void trainOneOperator(MLP model, int n_iter, int typeOfOperation) throws Exception {
        if (n_iter < 0) throw new Exception("n_iter is negative in tranOneOperation from Test file");
        if (model == null) throw new Exception("MLP model is null in tranOneOperation from Test file");
        if (typeOfOperation < 0)
            throw new Exception("typeOfOperation can't be negative in tranOneOperation from Test file");

        for (int i = 0; i < n_iter; i++) {
            int x1 = ThreadLocalRandom.current().nextInt(0, 10);
            int x2 = ThreadLocalRandom.current().nextInt(0, 10);

            int result;
            double[] ytrue;

            if (typeOfOperation == 0) {
                result = x1 + x2;
                ytrue = new double[19];
            } else if (typeOfOperation == 1) {
                result = x1 * x2;
                ytrue = new double[82];
            } else {
                result = x1 - x2;
                ytrue = new double[19];
            }

            ytrue[Math.abs(result)] = 1;

            model.input = new double[]{x1, x2};
            model.learn(ytrue);

            if (typeOfOperation == 0) {
                System.out.println(model.input[0] + " + " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);

            } else if (typeOfOperation == 1) {
                System.out.println(model.input[0] + " * " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);

            } else {
                if (model.input[0] - model.input[1] < 0) {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (-MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (-MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
                } else {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);

                }
            }
        }

    }

    /**
     * @param model           MLP model that need to be init manually or with initModel(...) function.
     * @param n_iter          Numbers of examples for training our model.
     * @param typeOfOperation 0 == Addition, 1 == Multiplication and 2 and + is for subtraction.
     * @throws Exception throw when n_iter is negative, MLP model is null or when typeOfOperation is bellow 0.
     */
    public static void validationOneOperator(MLP model, int n_iter, int typeOfOperation) throws Exception {
        if (n_iter < 0) throw new Exception("n_iter is negative in tranOneOperation from Test file");
        if (model == null) throw new Exception("MLP model is null in tranOneOperation from Test file");
        if (typeOfOperation < 0)
            throw new Exception("typeOfOperation can't be negative in tranOneOperation from Test file");

        model.goodAnswers = 0;

        for (int i = 0; i < n_iter; i++) {

            int x1 = ThreadLocalRandom.current().nextInt(0, 10);
            int x2 = ThreadLocalRandom.current().nextInt(0, 10);

            int result;
            double[] ytrue;

            if (typeOfOperation == 0) {
                result = x1 + x2;
                ytrue = new double[19];
            } else if (typeOfOperation == 1) {
                result = x1 * x2;
                ytrue = new double[82];
            } else {
                result = x1 - x2;
                ytrue = new double[19];
            }

            ytrue[Math.abs(result)] = 1;

            model.input = new double[]{x1, x2};
            model.predicted = model.forward(model.input);

            if (typeOfOperation == 0) {
                System.out.println(model.input[0] + " + " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);

                if (MLP.getMaxIndice(ytrue) == MLP.getMaxIndice(model.predicted)) {
                    model.goodAnswers += 1;
                }
            } else if (typeOfOperation == 1) {
                System.out.println(model.input[0] + " * " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
                if (MLP.getMaxIndice(ytrue) == MLP.getMaxIndice(model.predicted)) {
                    model.goodAnswers += 1;
                }
            } else {
                if (model.input[0] - model.input[1] < 0) {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (-MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (-MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
                    if (MLP.getMaxIndice(ytrue) == MLP.getMaxIndice(model.predicted)) {
                        model.goodAnswers += 1;
                    }
                } else {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
                    if (MLP.getMaxIndice(ytrue) == MLP.getMaxIndice(model.predicted)) {
                        model.goodAnswers += 1;
                    }

                }
            }

        }

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Validation Set : ");
        System.out.println("Accuracy : " + (double) model.goodAnswers / n_iter);
        System.out.println("-------------------------------------------------------------------------");
    }

    /**
     * Train method for training our model on all operators.
     * This will generate the training set and will call the method learn.
     *
     * @param n_iter is the number of iterations that our model will train on data.
     * @param model  MLP model that need to be init manually or with initModel(...) function.
     * @throws Exception is throw when n_iter is negative or when model is null.
     */
    public static void trainAllOperators(MLP model, int n_iter) throws Exception {
        if (n_iter < 0) throw new Exception("n_iter is negative in trainAllOperators(...) from Test file");
        if (model == null) throw new Exception("MLP model is null in trainAllOperators(...) from Test file");

        for (int i = 0; i < n_iter; i++) {

            int x1 = ThreadLocalRandom.current().nextInt(0, 10);
            int x2 = ThreadLocalRandom.current().nextInt(0, 10);
            int x3 = ThreadLocalRandom.current().nextInt(0, 3);

            int result;
            double[] ytrue = new double[91]; // because there is 91 possibility
            // with 3 operators and 9 different numbers
            if (x3 == 0) {
                result = x1 + x2;
                ytrue[result + 9] = 1;

            } else if (x3 == 1) {
                result = x1 * x2;
                ytrue[result + 9] = 1;

            } else {
                result = x1 - x2;
                ytrue[Math.abs(result)] = 1;

            }

            model.input = new double[]{x1, x3, x2};
            model.learn(ytrue);

            if (model.input[1] == 0) {
                System.out.println(model.input[0] + " + " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);

            } else if (model.input[1] == 1) {
                System.out.println(model.input[0] + " * " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);

            } else {
                if (model.input[0] - model.input[1] < 0) {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (-MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (-MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
                } else {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);

                }
            }


        }

    }


    /**
     * validation method for our model on all operators.
     * This will generate the validation set and will call the method forward.
     * This method will keep track on the number of good predictions by
     * our model and will print at the end the accuracy metric that is the number of good predictions / n_iter.
     *
     * @param n_iter is the number of iterations that our model will train on data.
     * @param model  MLP model that need to be init manually or with initModel(...) function.
     * @throws Exception is throw when n_iter is negative or when model in null.
     */
    public static void validationAllOperators(MLP model, int n_iter) throws Exception {
        if (n_iter < 0) throw new Exception("n_iter is negative");
        if (model == null) throw new Exception("MLP model is null in validationAllOperators(...) from Test file");

        model.goodAnswers = 0;

        for (int i = 0; i < n_iter; i++) {


            int x1 = ThreadLocalRandom.current().nextInt(0, 10);
            int x2 = ThreadLocalRandom.current().nextInt(0, 10);
            int x3 = ThreadLocalRandom.current().nextInt(0, 3);

            int result;
            double[] ytrue = new double[91]; // because there is 91 possibility
            // with 3 operators and 9 different numbers
            if (x3 == 0) {
                result = x1 + x2;
                ytrue[result + 9] = 1;

            } else if (x3 == 1) {
                result = x1 * x2;
                ytrue[result + 9] = 1;

            } else {
                result = x1 - x2;
                ytrue[Math.abs(result)] = 1;

            }

            model.input = new double[]{x1, x3, x2};


            if (model.input[1] == 0) {
                System.out.println(model.input[0] + " + " + model.input[2] + " = " + (MLP.getMaxIndice(ytrue) - 9));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
            } else if (model.input[1] == 1) {
                System.out.println(model.input[0] + " * " + model.input[2] + " = " + (MLP.getMaxIndice(ytrue) - 9));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
            } else {
                if (model.input[0] - model.input[2] < 0) {
                    System.out.println(model.input[0] + " - " + model.input[2] + " = " + (-MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (-MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
                } else {
                    System.out.println(model.input[0] + " - " + model.input[2] + " = " + (MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss);
                }
            }


            model.predicted = model.forward(model.input);
            if (MLP.getMaxIndice(ytrue) == MLP.getMaxIndice(model.predicted)) {
                model.goodAnswers += 1;
            }

        }

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Validation Set All Operation: ");
        System.out.println("Accuracy : " + (double) model.goodAnswers / n_iter);
        System.out.println("-------------------------------------------------------------------------");
    }

}


