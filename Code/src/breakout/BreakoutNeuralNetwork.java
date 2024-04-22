package breakout;

import utils.Commons;
import utils.GameController;

public class BreakoutNeuralNetwork implements GameController, Comparable<BreakoutNeuralNetwork> {

	private int inputDim = Commons.BREAKOUT_STATE_SIZE;
    private int hiddenDim = Commons.BREAKOUT_HIDDEN_LAYER;
    private int outputDim = Commons.BREAKOUT_NUM_ACTIONS;
    private double[][] hiddenWeights;
    private double[] hiddenBiases;
    private double[][] outputWeights;
    private double[] outputBiases;

    private double fitness = 0.0;

    private int seed = Commons.SEED;
    
    public BreakoutNeuralNetwork(int seed) {
    	this.seed = seed;
        initializeParameters();
    }
    
    public BreakoutNeuralNetwork(double[] values, int seed) {
    	this.seed = seed;
        int maxSize = Commons.BREAKOUT_NETWORK_SIZE; // Adjust this value as per new network size
        if (values.length == maxSize) {
            initializeNetwork(values);
        } else {
            throw new IllegalArgumentException("Incorrect size of input values array");
        }
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

    private void initializeParameters() {
        hiddenWeights = new double[inputDim][hiddenDim];
        hiddenBiases = new double[hiddenDim];
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                hiddenWeights[i][j] = ((Math.random() * 2) - 1);
            }
        }
        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = ((Math.random() * 2) - 1);
        }
        
        outputWeights = new double[hiddenDim][outputDim];
        outputBiases = new double[outputDim];
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                outputWeights[i][j] = ((Math.random() * 2) - 1);
            }
        }
        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = ((Math.random() * 2) - 1);
        }
    }

	public int getSeed() {
		return seed;
	}


	public double[] forward(int[] inputValues) {
		//double[] inputValues = normalize(inputValues);

		// First hidden layer
		double[] hiddenLayer = new double[hiddenDim];
		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < inputDim; j++) {
				hiddenLayer[i] += hiddenWeights[j][i] * inputValues[j];
			}
			hiddenLayer[i] = sigmoid(hiddenLayer[i] + hiddenBiases[i]);
		}

		// Output layer (now directly follows the first hidden layer)
		double[] output = new double[outputDim];
		for (int i = 0; i < outputDim; i++) {
			for (int j = 0; j < hiddenDim; j++) {
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

    private double sigmoid(double x) {
    	return 1/(1+Math.exp(-x));
    }

    public double getFitness() {
        return fitness;
    }

    public void calculateFitness() {
    	BreakoutBoard bb = new BreakoutBoard(this, false, seed);
    	bb.runSimulation();
    	this.fitness = bb.getFitness();
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
            
            acc /= Commons.BREAKOUT_NETWORK_SIZE;
            
            return getFitness() + " | (" + acc + ") | (" + max + ") | (" + min + ")";
    }

	@Override
	public int compareTo(BreakoutNeuralNetwork o) {
		return Double.compare(o.getFitness(), getFitness());
	}
}
