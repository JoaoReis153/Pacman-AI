package pacman;

import general.NeuralNetwork;
import utils.Commons;

public class PacmanNeuralNetwork extends NeuralNetwork {

    private double[][] hiddenWeights;
    private double[] hiddenBiases;
    private double[][] outputWeights;
    private double[] outputBiases;

    public PacmanNeuralNetwork() {
        super(Commons.PACMAN_STATE_SIZE, Commons.PACMAN_HIDDEN_LAYER, Commons.PACMAN_NUM_ACTIONS);
    }

    public PacmanNeuralNetwork(double[] values) {
        super(values, Commons.PACMAN_STATE_SIZE, Commons.PACMAN_HIDDEN_LAYER, Commons.PACMAN_NUM_ACTIONS);
    }

    public double[] forward(int[] inputValues) {
        hiddenWeights = getHiddenWeights();
        hiddenBiases = getHiddenBiases();
        outputWeights = getOutputWeights();
        outputBiases = getOutputBiases();

        // First hidden layer
        double[] hiddenLayer = new double[getHiddenDim()];
        for (int i = 0; i < getHiddenDim(); i++) {
            for (int j = 0; j < getInputDim(); j++) {
                hiddenLayer[i] += hiddenWeights[j][i] * inputValues[j];
            }
            hiddenLayer[i] = sigmoid(hiddenLayer[i] + hiddenBiases[i]);
        }

        // Output layer (now directly follows the first hidden layer)
        double[] output = new double[getOutputDim()];
        for (int i = 0; i < getOutputDim(); i++) {
            for (int j = 0; j < getHiddenDim(); j++) {
                output[i] += outputWeights[j][i] * hiddenLayer[j];
            }
            output[i] = sigmoid(output[i] + outputBiases[i]);
        }

        return output;
    }

    @Override
    public int nextMove(int[] currentState) {
        double maxValue = 0;
        int max = 0;
        double[] output = forward(currentState);
        for(int i = 0; i  < output.length; i++) {
            if( output[i] > maxValue) {
                max = i+1;
                maxValue = output[i];
            };
        }
        return maxValue == 0.25 ? 0 : max;
    }

    public void calculateFitness(int seed) {
        PacmanBoard bb = new PacmanBoard(this, false, seed);
        bb.runSimulation();
        this.setFitness(bb.getFitness());
    }

    @Override
    public int getNeuralNetworkSize() {
        return Commons.PACMAN_NETWORK_SIZE;
    }

}
