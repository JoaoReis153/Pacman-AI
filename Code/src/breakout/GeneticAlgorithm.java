package breakout;

import java.util.Arrays;

import utils.Commons;

public class GeneticAlgorithm {

		private int seed = 1;
		private static final int POPULATION_SIZE = 100;
		private static final int NUM_GENERATIONS = 100;

		private double MUTATION_RATE = .5;
		private double SELECTION_PERCENTAGE = .15;

		private int K_TOURNAMENT = 5;


		private BreakoutNeuralNetwork[] population = new BreakoutNeuralNetwork[POPULATION_SIZE];
		
		private BreakoutNeuralNetwork champion;
		

	    GeneticAlgorithm(){
	        generatePopulation();
	        champion = search();
	        System.out.println(champion);
	    }

	    public BreakoutNeuralNetwork getChampion() {
			return champion;
	    }


	    private void generatePopulation() {
			System.out.println("\n\n---\n+ Seed: " + seed + "\n-");
	        for(int i = 0; i < POPULATION_SIZE; i++){
	        	population[i] = new BreakoutNeuralNetwork(seed);
	        }
	    }
	    
	    private void getBest(BreakoutNeuralNetwork nn) {
    		if(nn.getFitness() > champion.getFitness()) {
				champion = nn;
				System.out.println(champion);
			}
		}


	    private BreakoutNeuralNetwork search() {
	    	champion = population[0];
            for (int i = 0; i < NUM_GENERATIONS; i++) {
				BreakoutNeuralNetwork[] newGeneration = new BreakoutNeuralNetwork[POPULATION_SIZE];
				Arrays.sort(population);
				
				if(i % 10 == 0)
					System.out.println("Gen: " + i);
		
				getBest(population[0]);

				int start = Math.max(2, (int) (POPULATION_SIZE * SELECTION_PERCENTAGE));

				for (int j = 0; j < POPULATION_SIZE - 1; j += 2) {
					
					if(j < start) {
						newGeneration[j] = population[j];
						newGeneration[j+1] = population[j+1];
					} else {
						//Select Parents
						BreakoutNeuralNetwork parent1 = selectParent();
						BreakoutNeuralNetwork parent2 = selectParent();
						BreakoutNeuralNetwork[] children;
						
						//Crossover
						children = crossover(parent1, parent2); 

						//Mutation
						newGeneration[j] = mutate(children[0]);
						newGeneration[j + 1]  = mutate(children[1]);
					}
					
				}
				
				population = newGeneration;

			}
			return champion;
		}
	    
	
	    private BreakoutNeuralNetwork mutate(BreakoutNeuralNetwork individual) {
	        double[] genes = individual.getNeuralNetwork();
	        if (Math.random() < MUTATION_RATE) {
				int index = (int) (Math.random() * Commons.BREAKOUT_NETWORK_SIZE);
				genes[index] = ((Math.random() * 2) - 1);
	        }
	        individual.initializeNetwork(genes);
	        return individual;
	    }


		private BreakoutNeuralNetwork selectParent() {
			BreakoutNeuralNetwork best = population[(int) (Math.random() * POPULATION_SIZE)];

			for (int i = 1; i < K_TOURNAMENT; i++) {
				BreakoutNeuralNetwork c = population[(int) (Math.random() * POPULATION_SIZE)];

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
		    
		    int crossoverPoint = (int) (Math.random() * genes1.length);
		    
		    for (int i = 0; i < genes1.length; i++) {
		    	child1[i] = (i < crossoverPoint) ? genes1[i] : genes2[i];
		        child2[i] = (i < crossoverPoint) ? genes2[i] : genes1[i];
		    }
		    
		    BreakoutNeuralNetwork offspring1 = new BreakoutNeuralNetwork(child1, seed);
		    BreakoutNeuralNetwork offspring2 = new BreakoutNeuralNetwork(child2, seed);
		    return new BreakoutNeuralNetwork[]{offspring1, offspring2};
		}




}