package general;

import utils.Commons;
import utils.GameController;

import java.util.Random;

public abstract class NeuralNetwork implements GameController, Comparable<NeuralNetwork> {

    private final int inputDim;
    private final int hiddenDim;
    private final int outputDim;
    private double[][] hiddenWeights;
    private double[] hiddenBiases;
    private double[][] outputWeights;
    private double[] outputBiases;

    private Random random = new Random(Commons.SEED);
    private double fitness = 0.0;

    public NeuralNetwork(int inputDim, int hiddenDim, int outputDim) {
        this.inputDim = inputDim;
        this.hiddenDim = hiddenDim;
        this.outputDim = outputDim;
        initializeParameters();
    }

    public NeuralNetwork(double[] values, int inputDim, int hiddenDim, int outputDim) {
        this.inputDim = inputDim;
        this.hiddenDim = hiddenDim;
        this.outputDim = outputDim;
        int maxSize = getNeuralNetworkSize();
        if (values.length == maxSize) {
            initializeNetwork(values);
        } else {
            throw new IllegalArgumentException("Incorrect size of input values array");
        }
    }


    @Override
    public abstract int nextMove(int[] currentState);

    public abstract void calculateFitness(int seed);

    public abstract int getNeuralNetworkSize();

    public abstract double[] forward(int[] inputValues);


    public double[][] getHiddenWeights() {return hiddenWeights;}
    public double[] getHiddenBiases() {return hiddenBiases;}
    public double[][] getOutputWeights() {return outputWeights;}
    public double[] getOutputBiases() {return outputBiases;}
    public int getInputDim() {return inputDim;}
    public int getHiddenDim() {return hiddenDim;}
    public int getOutputDim() {return outputDim;}

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void initializeNetwork(double[] values) {
        hiddenWeights = new double[inputDim][hiddenDim];
        hiddenBiases = new double[hiddenDim];
        outputWeights = new double[hiddenDim][outputDim];
        outputBiases = new double[outputDim];

        int index = 0;
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                hiddenWeights[i][j] = values[index++];
            }
        }
        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = values[index++];
        }
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                outputWeights[i][j] = values[index++];
            }
        }
        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = values[index++];
        }
    }

    public void initializeParameters() {
        hiddenWeights = new double[inputDim][hiddenDim];
        hiddenBiases = new double[hiddenDim];
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                hiddenWeights[i][j] = ((random.nextDouble() * 2) - 1);
            }
        }
        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = ((random.nextDouble() * 2) - 1);
        }

        outputWeights = new double[hiddenDim][outputDim];
        outputBiases = new double[outputDim];
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                outputWeights[i][j] = ((random.nextDouble() * 2) - 1);
            }
        }
        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = ((random.nextDouble() * 2) - 1);
        }
    }

    public double sigmoid(double x) {
        return 1/(1+Math.exp(-x));
    }

    public double getFitness() {
        return fitness;
    }

    public double[] getNeuralNetwork() {
        int size = (inputDim * hiddenDim) + hiddenDim + // Weights and biases for the hidden layer
                (hiddenDim * outputDim) + outputDim; // Weights and biases for the output layer
        double[] networkParams = new double[size];

        int index = 0;
        // Hidden layer weights
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                networkParams[index++] = hiddenWeights[i][j];
            }
        }
        // Hidden layer biases
        for (int i = 0; i < hiddenDim; i++) {
            networkParams[index++] = hiddenBiases[i];
        }

        // Output layer weights
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                networkParams[index++] = outputWeights[i][j];
            }
        }
        // Output layer biases
        for (int i = 0; i < outputDim; i++) {
            networkParams[index++] = outputBiases[i];
        }

        return networkParams;
    }

    @Override
    public String toString() {

        double acc = 0;
        double max = -10000;
        double min =  10000;

        for (int input = 0; input < inputDim; input++) {
            for (int i = 0; i < hiddenDim; i++) {
                max = Math.max(hiddenWeights[input][i], max);
                min = Math.min(hiddenWeights[input][i], min);
                acc += hiddenWeights[input][i];
            }
        }

        for (int i = 0; i < hiddenDim; i++) {
            acc += hiddenBiases[i];
            max = Math.max(hiddenBiases[i], max);
            min = Math.min(hiddenBiases[i], min);
        }

        for (int hiddenw = 0; hiddenw < hiddenDim; hiddenw++) {
            for (int i = 0; i < outputDim; i++) {
                acc += outputWeights[hiddenw][i];
                max = Math.max(outputWeights[hiddenw][i], max);
                min = Math.min(outputWeights[hiddenw][i], min);
            }
        }

        for (int i = 0; i < outputDim; i++) {
            acc += outputBiases[i];
            max = Math.max(outputBiases[i], max);
            min = Math.min(outputBiases[i], min);
        }

        acc /= Commons.PACMAN_NETWORK_SIZE;

        return getFitness() + " | (" + acc + ") | (" + max + ") | (" + min + ")";
    }

    @Override
    public int compareTo(NeuralNetwork o) {
        return Double.compare(o.getFitness(), getFitness());
    }
}
