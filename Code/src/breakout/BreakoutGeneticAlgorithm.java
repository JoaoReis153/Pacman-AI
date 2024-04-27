package breakout;

import java.util.Arrays;
import java.util.Random;

import pacman.PacmanNeuralNetwork;
import utils.Commons;

import javax.swing.text.ElementIterator;

public class BreakoutGeneticAlgorithm {

		private Random random = new Random((int) (Math.random() * 100000));

		private int seed;

		private static final int POPULATION_SIZE = 1000;
		private static final int NUM_GENERATIONS = 10;
		private static final double INITIAL_MUTATION_PERCENTAGE = 0.05;
		private double MUTATION_PERCENTAGE = 0.05;
		private static final double MUTATION_RATE = 0.2;
		private static final double MUTATION_SIZE = .1;

		private static final double SELECTION_PERCENTAGE = 0.1;
		private static final int K_TOURNAMENT = 6;

		//private static final int K_POINTS = 5;

		private int noEvolutionInterval = 0;

		private BreakoutNeuralNetwork[] population = new BreakoutNeuralNetwork[POPULATION_SIZE];

		private BreakoutNeuralNetwork champion;

		public int getSeed() {return seed;}

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

				if(noEvolutionInterval > 20) {
					for (int k = POPULATION_SIZE - 1 ; k > POPULATION_SIZE - eliteCount/2 ; k--)
						population[k] = new BreakoutNeuralNetwork();
				}

				BreakoutNeuralNetwork[] newGeneration = new BreakoutNeuralNetwork[POPULATION_SIZE];

				if(i % 10 == 0) {
					System.out.println("Gen: " + i);
				}

				updateChamp(population[0]);

				for (int j = 0; j < POPULATION_SIZE; j += 2) {

					if(j < eliteCount) {
						newGeneration[j] = population[j];
						newGeneration[j+1] = population[j+1];
					} else {
						//Select Parents
						BreakoutNeuralNetwork parent1 = selectParent();
						BreakoutNeuralNetwork parent2 = selectParent();

						//Crossover
						BreakoutNeuralNetwork[] children = crossover(parent1, parent2);

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
					genes[index] += (random.nextDouble()  * MUTATION_SIZE) - MUTATION_SIZE * 2;
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

		private BreakoutNeuralNetwork[] crossover(BreakoutNeuralNetwork parent1, BreakoutNeuralNetwork parent2) {
			double[] genes1 = parent1.getNeuralNetwork();
			double[] genes2 = parent2.getNeuralNetwork();
			double[] child1 = new double[genes1.length];
			double[] child2 = new double[genes2.length];

			int crossoverPoint = (int) (random.nextDouble() * genes1.length);

			for (int i = 0; i < genes1.length; i++) {
				/*
				if(i%2== 0) {
					child1[i] = genes2[i];
					child2[i] = genes1[i];
				} else {
					child1[i] = genes1[i];
					child2[i] = genes2[i];
				}
				*/
				child1[i] = (i < crossoverPoint) ? genes2[i] : genes1[i];
				child2[i] = (i < crossoverPoint) ? genes1[i] : genes2[i];


			}

			BreakoutNeuralNetwork offspring1 = new BreakoutNeuralNetwork(child1);
			BreakoutNeuralNetwork offspring2 = new BreakoutNeuralNetwork(child2);
			return new BreakoutNeuralNetwork[]{offspring1, offspring2};
		}



}