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
import transfertFunctions.TransfertFunctionLeakyRelu;

import transfertFunctions.TransfertFunctionSigmoid;
import transfertFunctions.TransfertFunctionTanh;
import utils.InitialiseBiasNormal;
import utils.InitialiseWeightsNormal;

import java.util.Random;
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
     * outputSize : 21
     * <p>
     * If you do the Multiplication :
     * typeOfOperation : 1
     * outputSize : 101
     * <p>
     * If you do the Subtraction:
     * typeOfOperation : 2 or +
     * outputSize : 21
     * <p>
     * If you do the All Operators :
     * outputSize : 111
     *
     * @param args nothing.
     * @throws Exception when one Exception is throw in one the method.
     */
    public static void main(String[] args) throws Exception {

        /*
                // ACCURACY WITH THIS CONFIG 81.1%
        MLP modelAddition = initModel(1, new int[]{256}, 0.01, new ITransfertFunction[]
                {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 21, 2);
        trainOneOperator(modelAddition, 2_000_000, 0);
        validationOneOperator(modelAddition, 1_000, 0);
        */

        /*
         // ACCURACY WITH THIS CONFIG 81.7%
        MLP modelMultiplication = initModel(1, new int[] {256}, 0.01, new ITransfertFunction[]
                {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 101, 2);
        trainOneOperator(modelMultiplication, 2_000_000, 1);
        validationOneOperator(modelMultiplication, 1_000, 1);
        */
        /* // ACCURACY WITH THIS CONFIG 74.5%
        MLP modelSubtraction = initModel(1, new int[] {256}, 0.1, new ITransfertFunction[]
                {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 21, 2);
        trainOneOperator(modelSubtraction, 2_000_000, 2);
        validationOneOperator(modelSubtraction, 1_000, 2);
        */
        /*
        // ACCURACY WITH THIS CONFIG 79.2%
        MLP modelAll = initModel(1, new int[]{256}, 0.01, new ITransfertFunction[]
                {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 111, 3);

        trainAllOperators(modelAll, 2_000_000);
        validationAllOperators(modelAll, 1_000);
        */
    }

    /**
     * The initModel is an elegant way to initialize an MLP model.
     * The structure of an Neural network model in our case is :
     * input layer - hidden layers - output layer
     * <p>
     * Example :
     * MLP modelAll = initModel(1, new int[]{256}, 0.01, new ITransfertFunction[]
     * {new TransfertFunctionTanh(), new TransfertFunctionSigmoid()}, 111, 3);
     * <p>
     * In this case we say that we have 1 hidden layer with 256 neurons. For all the model we have a learning rate of 0.01.
     * We need to specify the activation function for all the hidden layers / output layer. So, ITransfertFunction[] need
     * to be size : nbHiddenLayers + 1
     * outputSize is the number of elements that you want to predict, please refer to the javadoc of the main method.
     * inputSize same as outputSize.
     *
     * @param nbHiddenLayers numbers of layers
     * @param hiddenSize     numbers of neurons in layers
     * @param lr             learning rate
     * @param tfArray        array of TransfertFunction
     * @param outputSize     size of the output
     * @param inputSize      size of the input
     * @return MLP model
     */
    public static MLP initModel(int nbHiddenLayers, int[] hiddenSize, double lr, ITransfertFunction[] tfArray, int outputSize, int inputSize) throws Exception {
        if (nbHiddenLayers <= 0) throw new Exception("nbHiddenLayers is <= 0 in initModel function from Test file");
        if (nbHiddenLayers + 1 != tfArray.length)
            throw new Exception("nbHiddenLayers+1 != tfArray.length in initModel function from Test file");
        if (hiddenSize == null) throw new Exception("hiddenSize is null in initModel function from Test file");
        if (lr < 0) throw new Exception("lr is < 0 in initModel function from Test file");
        if (tfArray == null) throw new Exception("tfArray is null in initModel function from Test file");

        if (outputSize <= 0) throw new Exception("outputSize <= 0 in initModel function from Test file");
        if (inputSize <= 0) throw new Exception("inputSize <= 0 in initModel function from Test file");

        for (ITransfertFunction tf : tfArray) {
            if (tf == null) throw new Exception("null object in tfArray from Test.initModel");
        }

        LayerLinear[] layers = new LayerLinear[nbHiddenLayers + 1];


        layers[0] = new LayerLinear(
                inputSize, hiddenSize[0], lr,
                new InitialiseWeightsNormal(), new InitialiseBiasNormal(), tfArray[0]
        );

        for (int i = 1; i < nbHiddenLayers; i++) {
            layers[i] = new LayerLinear(
                    hiddenSize[i - 1], hiddenSize[i], lr,
                    new InitialiseWeightsNormal(), new InitialiseBiasNormal(), tfArray[i]
            );
        }

        layers[nbHiddenLayers] = new LayerLinear(
                hiddenSize[nbHiddenLayers - 1], outputSize, lr,
                new InitialiseWeightsNormal(), new InitialiseBiasNormal(), tfArray[nbHiddenLayers]
        );


        return new MLP(layers, new LossDifference());
    }

    /**
     * Please refer to the javadoc of the main method for typeOfOperation.
     * Numbers of epochs is the number of elements that you will feed into your model.
     * Example :
     * epochs = 1_000_000 is 1_000_000 of samples that will be used for train your model.
     * <p>
     * This method train and display information on terminal.
     *
     * @param model           MLP model that need to be init manually or with initModel(...) function.
     * @param epochs          Numbers of examples for training our model.
     * @param typeOfOperation 0 == Addition, 1 == Multiplication and 2 and + is for subtraction.
     * @throws Exception throw when n_iter is negative, MLP model is null or when typeOfOperation is bellow 0.
     */
    public static void trainOneOperator(MLP model, int epochs, int typeOfOperation) throws Exception {
        if (epochs < 0) throw new Exception("n_iter is negative in tranOneOperation from Test file");
        if (model == null) throw new Exception("MLP model is null in tranOneOperation from Test file");
        if (typeOfOperation < 0)
            throw new Exception("typeOfOperation can't be negative in tranOneOperation from Test file");
        long start = System.currentTimeMillis();

        for (int i = 0; i < epochs; i++) {
            int operand1 = ThreadLocalRandom.current().nextInt(0, 11);
            int operand2 = ThreadLocalRandom.current().nextInt(0, 11);

            int resultOperation;
            double[] ytrue;

            if (typeOfOperation == 0) {
                resultOperation = operand1 + operand2;
                ytrue = new double[21];
                ytrue[resultOperation] = 1;

            } else if (typeOfOperation == 1) {
                resultOperation = operand1 * operand2;
                ytrue = new double[101];
                ytrue[resultOperation] = 1;


            } else {
                resultOperation = operand1 - operand2;
                ytrue = new double[21];
                ytrue[Math.abs(Math.abs(resultOperation)) + 10] = 1;


            }


            model.input = new double[]{operand1, operand2};
            model.learn(ytrue);

            if (typeOfOperation == 0) {
                System.out.println(model.input[0] + " + " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss + " \t\t epoch: " + (i / (double) epochs) + "%" + "\t\t" + "train set");

            } else if (typeOfOperation == 1) {
                System.out.println(model.input[0] + " * " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss + " \t\t epoch: " + (i / (double) epochs) + "%" + "\t\t" + "train set");

            } else {
                if (model.input[0] - model.input[1] < 0) {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (-MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (-MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss + " \t\t epoch: " + (i / (double) epochs) + "%" + "\t\t" + "train set");
                } else {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (MLP.getMaxIndice(ytrue)));
                    System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss + " \t\t epoch: " + (i / (double) epochs) + "%" + "\t\t" + "train set");

                }
            }
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Training Set: ");
        System.out.println("Time : " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("-------------------------------------------------------------------------");
    }

    /**
     * This method will help you to see if your model is doing good on new data or not.
     *
     * Please refer to the javadoc of the main method for typeOfOperation.
     * Numbers of epochs is the number of elements that you will feed into your model.
     *
     * Example :
     * epochs = 1_000_000 is 1_000_000 of samples that will be used for train your model.
     * <p>
     *
     * This method validation display information on terminal about the accuracy of our model that is the number of good
     * answers/numbers of epochs.
     *
     * @param model           MLP model that need to be init manually or with initModel(...) function.
     * @param epochs          Numbers of examples for training our model.
     * @param typeOfOperation 0 == Addition, 1 == Multiplication and 2 and + is for subtraction.
     * @throws Exception throw when n_iter is negative, MLP model is null or when typeOfOperation is bellow 0.
     */
    public static void validationOneOperator(MLP model, int epochs, int typeOfOperation) throws Exception {
        if (epochs < 0) throw new Exception("n_iter is negative in tranOneOperation from Test file");
        if (model == null) throw new Exception("MLP model is null in tranOneOperation from Test file");
        if (typeOfOperation < 0)
            throw new Exception("typeOfOperation can't be negative in tranOneOperation from Test file");

        model.goodAnswers = 0;
        long start = System.currentTimeMillis();

        for (int i = 0; i < epochs; i++) {

            int operand1 = ThreadLocalRandom.current().nextInt(0, 11);
            int operand2 = ThreadLocalRandom.current().nextInt(0, 11);

            int resultOperation;
            double[] ytrue;

            if (typeOfOperation == 0) {
                resultOperation = operand1 + operand2;
                ytrue = new double[21];
                ytrue[resultOperation] = 1;

            } else if (typeOfOperation == 1) {
                resultOperation = operand1 * operand2;
                ytrue = new double[101];
                ytrue[resultOperation] = 1;


            } else {
                resultOperation = operand1 - operand2;
                ytrue = new double[21];
                ytrue[Math.abs(Math.abs(resultOperation)) + 10] = 1;


            }


            model.input = new double[]{operand1, operand2};
            model.predicted = model.forward(model.input);

            if (typeOfOperation == 0) {
                System.out.println(model.input[0] + " + " + model.input[1] + " = " + (resultOperation));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted) - 10) + " , loss=" + model.loss + " \t\t epoch: " + (i / (double) epochs) + "%" + "\t\t" + "validation set");

            } else if (typeOfOperation == 1) {
                System.out.println(model.input[0] + " * " + model.input[1] + " = " + (resultOperation));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted) - 10) + " , loss=" + model.loss + " \t\t epoch: " + (i / (double) epochs) + "%" + "\t\t" + "validation set");

            } else {
                if ((model.input[0] - model.input[1] < 0)) {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (resultOperation));
                    System.out.println("ypred = " + (-MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss + " \t\t epoch: " + (i / (double) epochs) + "%" + "\t\t" + "validation set");
                } else {
                    System.out.println(model.input[0] + " - " + model.input[1] + " = " + (resultOperation));
                    System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss + " \t\t epoch: " + (i / (double) epochs) + "%" + "\t\t" + "validation set");

                }
            }

            if (MLP.getMaxIndice(ytrue) == MLP.getMaxIndice(model.predicted)) {
                model.goodAnswers += 1;
            }

        }

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Validation Set : ");
        System.out.println("Accuracy : " + (double) model.goodAnswers / epochs);
        System.out.println("Time : " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("-------------------------------------------------------------------------");
    }

    /**
     * Train method for training our model on all operators.
     * This will generate the training set and will call the method learn and display.
     *
     * @param epochs is the number of iterations that our model will train on data.
     * @param model  MLP model that need to be init manually or with initModel(...) function.
     * @throws Exception is throw when epochs is negative or when model is null.
     */
    public static void trainAllOperators(MLP model, int epochs) throws Exception {
        if (epochs < 0) throw new Exception("epochs is negative in trainAllOperators(...) from Test file");
        if (model == null) throw new Exception("MLP model is null in trainAllOperators(...) from Test file");
        long start = System.currentTimeMillis();
        for (int i = 0; i < epochs; i++) {

            int operand1 = new Random().nextInt(11);
            int operand2 = new Random().nextInt(11);
            int operator = new Random().nextInt(3);

            int resultOperation;
            double[] ytrue = new double[111]; // because there is 111 possibility
            // with 3 operators and 11 different numbers

            if (operator == 0) {
                resultOperation = operand1 + operand2;
                ytrue[resultOperation + 10] = 1;

            } else if (operator == 1) {
                resultOperation = operand1 * operand2;
                ytrue[resultOperation + 10] = 1;


            } else {
                resultOperation = operand1 - operand2;
                ytrue[Math.abs(Math.abs(resultOperation)) + 10] = 1;


            }

            model.input = new double[]{operand1, operator, operand2};
            model.learn(ytrue);

            display(model, epochs, i, resultOperation, "training set");
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Training Set: ");
        System.out.println("Time : " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("-------------------------------------------------------------------------");
    }


    /**
     * validation method for our model on all operators.
     * This will generate the validation set and will call the method forward.
     * This method will keep track on the number of good predictions by
     * our model and will print at the end the accuracy metric that is the number of good predictions / n_iter.
     *
     * @param epochs is the number of iterations that our model will train on data.
     * @param model  MLP model that need to be init manually or with initModel(...) function.
     * @throws Exception is throw when n_iter is negative or when model in null.
     */
    public static void validationAllOperators(MLP model, int epochs) throws Exception {
        if (epochs < 0) throw new Exception("n_iter is negative");
        if (model == null) throw new Exception("MLP model is null in validationAllOperators(...) from Test file");
        long start = System.currentTimeMillis();
        model.goodAnswers = 0;

        for (int i = 0; i < epochs; i++) {


            int operand1 = ThreadLocalRandom.current().nextInt(0, 11);
            int operand2 = ThreadLocalRandom.current().nextInt(0, 11);
            int operator = ThreadLocalRandom.current().nextInt(0, 3);

            int resultOperation;
            double[] ytrue = new double[111]; // because there is 91 possibility
            // with 3 operators and 9 different numbers
            if (operator == 0) {
                resultOperation = operand1 + operand2;
                ytrue[resultOperation + 10] = 1;

            } else if (operator == 1) {
                resultOperation = operand1 * operand2;
                ytrue[resultOperation + 10] = 1;


            } else {
                resultOperation = operand1 - operand2;
                ytrue[Math.abs(Math.abs(resultOperation)) + 10] = 1;


            }

            model.input = new double[]{operand1, operator, operand2};


            display(model, epochs, i, resultOperation, "validation set");


            model.predicted = model.forward(model.input);
            if (MLP.getMaxIndice(ytrue) == MLP.getMaxIndice(model.predicted)) {
                model.goodAnswers += 1;
            }

        }

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Validation Set All Operation: ");
        System.out.println("Accuracy : " + (double) model.goodAnswers / epochs);
        System.out.println("Time : " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("-------------------------------------------------------------------------");
    }

    /**
     * Method that is used for display information about training/validation on terminal.
     *
     * @param model           MLP model
     * @param epochs          numbers of epochs
     * @param i               iterator i
     * @param resultOperation result of the mathematical expression.
     * @throws Exception when model is null, epochs < 0 or i < 0
     */
    private static void display(MLP model, double epochs, int i, int resultOperation, String type) throws Exception {
        if (model == null) throw new Exception("model is null in display from Test");
        if (epochs < 0) throw new Exception("epochs<0 in display from Test");
        if (i < 0) throw new Exception("i<0 in display from Test");

        if (model.input[1] == 0) {
            System.out.println(model.input[0] + " + " + model.input[2] + " = " + (resultOperation));
            System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted) - 10) + " , loss=" + model.loss + " \t\t epoch: " + (i / epochs) + "%" + "\t\t" + type);

        } else if (model.input[1] == 1) {
            System.out.println(model.input[0] + " * " + model.input[2] + " = " + (resultOperation));
            System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted) - 10) + " , loss=" + model.loss + " \t\t epoch: " + (i / epochs) + "%" + "\t\t" + type);

        } else {
            if ((model.input[0] - model.input[2] < 0)) {
                System.out.println(model.input[0] + " - " + model.input[2] + " = " + (resultOperation));
                System.out.println("ypred = " + (-MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss + " \t\t epoch: " + (i / epochs) + "%" + "\t\t" + type);
            } else {
                System.out.println(model.input[0] + " - " + model.input[2] + " = " + (resultOperation));
                System.out.println("ypred = " + (MLP.getMaxIndice(model.predicted)) + " , loss=" + model.loss + " \t\t epoch: " + (i / epochs) + "%" + "\t\t" + type);

            }
        }
    }

}


