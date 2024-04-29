package breakout;

public class Main {

	public static void main(String[] args) {

		int seed = 3113;

		BreakoutGeneticAlgorithm ga = new BreakoutGeneticAlgorithm(seed);
		BreakoutNeuralNetwork nn = ga.getChampion();
		new Breakout(nn, seed);

		
	}
}
