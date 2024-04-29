package breakout;

import java.util.Arrays;
import java.util.Random;

import utils.Commons;

public class BreakoutGeneticAlgorithm {

		private Random random = new Random((int) (Math.random() * 100000));

		private int seed;

		private static final int POPULATION_SIZE = 100;
		private static final int NUM_GENERATIONS = 100;
		//private static final double INITIAL_MUTATION_PERCENTAGE = 0.05;
		private double MUTATION_PERCENTAGE = 0.05;
		private static final double MUTATION_RATE = 0.3;
		private static final double SELECTION_PERCENTAGE = 0.3;
		private static final int K_TOURNAMENT = 4;

		private int noEvolutionInterval = 0;

		private BreakoutNeuralNetwork[] population = new BreakoutNeuralNetwork[POPULATION_SIZE];

		private BreakoutNeuralNetwork champion;

	    BreakoutGeneticAlgorithm(int seed){
			this.seed = seed;
			System.out.println("--Seed : " + seed + " --");
	        generatePopulation();
	        champion = search();
	        System.out.println(champion);
	    }

	    public BreakoutNeuralNetwork getChampion() {
			return champion;
	    }


	    private void generatePopulation() {
			//start = Math.max(Math.min(start, POPULATION_SIZE), 0);
	        for(int i = 0; i < POPULATION_SIZE; i++){
	        	population[i] = new BreakoutNeuralNetwork();
	        }
	    }

	    private void updateChamp(BreakoutNeuralNetwork nn) {
			if (champion == null || nn.getFitness() > champion.getFitness()) {
				noEvolutionInterval = 0;
				champion = nn;
				System.out.println(champion);
			} else {
				noEvolutionInterval++;
			}
		}

		private void loadPopulationFitness() {
			for(BreakoutNeuralNetwork nn : population) nn.calculateFitness(seed);
		}


	    private BreakoutNeuralNetwork search() {

			int eliteCount = (int) (POPULATION_SIZE * SELECTION_PERCENTAGE);
			System.out.println("Elite count: " + eliteCount);

            for (int i = 0; i < NUM_GENERATIONS; i++) {

				loadPopulationFitness();

				Arrays.sort(population);

				BreakoutNeuralNetwork[] newGeneration = new BreakoutNeuralNetwork[POPULATION_SIZE];

				System.out.println("Gen: " + i);

				if(i % 10 == 0) {
					System.out.println("(" + population[0].getFitness() + ") - (" + population[population.length - 1].getFitness() + ")");

				}

				updateChamp(population[0]);

				for (int j = 0; j < POPULATION_SIZE - 1; j += 2) {

					if(j < eliteCount) {
						newGeneration[j] = population[j];
						newGeneration[j+1] = population[j+1];
					} else {
						//Select Parents
						BreakoutNeuralNetwork parent1 = selectParent();
						BreakoutNeuralNetwork parent2 = selectParent();
						BreakoutNeuralNetwork[] children;
						//Crossover
						if(noEvolutionInterval > 15) {
							children = crossover(parent1, parent2, 0);
						} else {
							children = crossover(parent1, parent2, j);
						}


						//Mutation
						newGeneration[j] = mutate(children[0]);
						newGeneration[j + 1]  = mutate(children[1]);
					}

				}

				population = newGeneration;

			}
			champion.calculateFitness(seed);
			return champion;
		}


	    private BreakoutNeuralNetwork mutate(BreakoutNeuralNetwork individual) {
	        double[] genes = individual.getNeuralNetwork();
	        if (Math.random() < MUTATION_RATE) {
				int size = (int) (Commons.BREAKOUT_NETWORK_SIZE * MUTATION_PERCENTAGE);
				for(int i = 0; i < size; i++) {
					int index = (int) (random.nextDouble() * Commons.BREAKOUT_NETWORK_SIZE);
					genes[index] = ((random.nextDouble() * 2) - 1);
				}

	        }
	        individual.initializeNetwork(genes);
	        return individual;
	    }


		private BreakoutNeuralNetwork selectParent() {
			BreakoutNeuralNetwork best = population[(int) (random.nextDouble() * POPULATION_SIZE)];

			for (int i = 1; i < K_TOURNAMENT; i++) {
				BreakoutNeuralNetwork c = population[(int) (random.nextDouble() * POPULATION_SIZE)];

				if (c.getFitness() > best.getFitness())
					best = c;

			}
			return best;
		}

		private BreakoutNeuralNetwork[] crossover(BreakoutNeuralNetwork parent1, BreakoutNeuralNetwork parent2, int option) {
				double[] genes1 = parent1.getNeuralNetwork();
				double[] genes2 = parent2.getNeuralNetwork();
				double[] child1 = new double[genes1.length];
				double[] child2 = new double[genes2.length];

				int crossoverPoint = (int) (random.nextDouble() * genes1.length);

				for (int i = 0; i < genes1.length; i++) {

					if(option == 0) {
						boolean r = (random.nextDouble() < .5);
						child1[i] = r ? genes1[i] : genes2[i];
						child2[i] = r ? genes2[i] : genes1[i];
					} else if(option % 2 == 0) {
						child1[i] = (i < crossoverPoint) ? genes1[i] : genes2[i];
						child2[i] = (i < crossoverPoint) ? genes2[i] : genes1[i];
					} else {
						double r = (genes1[i] + genes2[i]) / 2;
						child1[i] = r;
						child2[i] = r;
					}



				}

				BreakoutNeuralNetwork offspring1 = new BreakoutNeuralNetwork(child1);
				BreakoutNeuralNetwork offspring2 = new BreakoutNeuralNetwork(child2);
				return new BreakoutNeuralNetwork[]{offspring1, offspring2};
			}




}