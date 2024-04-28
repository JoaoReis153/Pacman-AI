package breakout;

import java.util.*;

public class Main {

	public static void main(String[] args) {

		int seed = 123;

		BreakoutGeneticAlgorithm ga = new BreakoutGeneticAlgorithm(seed);
		BreakoutNeuralNetwork nn = ga.getChampion();
		new Breakout(nn, seed);
		/*

		int n = 1000;
		ArrayList<Score> scoreList = new ArrayList<>();

		for(int i = 0; i < n; i++) {

			BreakoutGeneticAlgorithm ga = new BreakoutGeneticAlgorithm(i);
			BreakoutNeuralNetwork nn = ga.getChampion();
			scoreList.add(new Score(i, nn.getFitness()));

			Collections.sort(scoreList);
			System.out.println("Best: ");
			System.out.println(scoreList.getFirst());

		}

		for(int i = scoreList.size(); i > 0; i--) {
			System.out.println(scoreList.get(i));
		}
		s
		 */



	}
}

