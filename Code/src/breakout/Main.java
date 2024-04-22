package breakout;

import utils.Commons;

public class Main {

	public static void main(String[] args) {

		GeneticAlgorithm ga = new GeneticAlgorithm();
		BreakoutNeuralNetwork nn = ga.getChampion();
		new Breakout(nn, nn.getSeed());

		
	}
}
