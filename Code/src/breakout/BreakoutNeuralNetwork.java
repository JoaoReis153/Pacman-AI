package breakout;

import general.NeuralNetwork;
import utils.Commons;
import utils.GameController;

public class BreakoutNeuralNetwork extends NeuralNetwork {

    private double[][] hiddenWeights;
    private double[] hiddenBiases;
    private double[][] outputWeights;
    private double[] outputBiases;

    public BreakoutNeuralNetwork() {
        super(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_LAYER, Commons.BREAKOUT_NUM_ACTIONS);
    }


    public BreakoutNeuralNetwork(double[] values) {
        super(values, Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_LAYER, Commons.BREAKOUT_NUM_ACTIONS);
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
			hiddenLayer[i] = hiddenLayer[i] + hiddenBiases[i];
		}

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
	public int nextMove(int[] inputValues) {
        double[] output = forward(inputValues);
		if(output[0] > output[1]) 
			return 1;
		return 2;
	}

    public void calculateFitness(int seed) {
		BreakoutBoard bb = new BreakoutBoard(this, false, seed*2);
		bb.runSimulation();
		this.setFitness(bb.getFitness());
    }

    @Override
    public int getNeuralNetworkSize() {
        return Commons.BREAKOUT_NETWORK_SIZE;
    }
}
