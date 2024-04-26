package pacman;

import java.util.Arrays;
import java.util.Random;

import breakout.BreakoutNeuralNetwork;
import utils.Commons;

public class PacmanGeneticAlgorithm {

    private final Random random = new Random((int) (Math.random() * 10000));
    private final int POPULATION_SIZE = 100;
    private final int NUM_GENERATIONS = 100;
    private static final double INITIAL_MUTATION_PERCENTAGE = 0.05;
    private double MUTATION_PERCENTAGE = 0.0;
    private static final double MUTATION_RATE = 0.40;
    private double SELECTION_PERCENTAGE = .5;
    private int k_tournament = 5;
    private PacmanNeuralNetwork champion;
    private final int seed ;

    private int noEvolutionInterval = 0;

    private PacmanNeuralNetwork[] population = new PacmanNeuralNetwork[POPULATION_SIZE];

    // Construtor para testar CADA parâmetro durante o TREINO

    public PacmanGeneticAlgorithm(int seed) {
        generatePopulation();
        this.seed = seed;
        this.champion = search();
    }


    public PacmanNeuralNetwork getChampion() {
        return champion;
    }


    private void updateChamp(PacmanNeuralNetwork nn) {
        if (champion == null || nn.getFitness() > champion.getFitness()) {
            noEvolutionInterval = 0;
            champion = nn;
            System.out.println(champion);
        } else {
            noEvolutionInterval++;
        }
    }

    // Função para gerar o fitness da população para não violar as diretrizes do compareTo() no próximo passo (sort)
    private PacmanNeuralNetwork search() {

        int start = Math.max(2, (int) (POPULATION_SIZE * SELECTION_PERCENTAGE));

        for (int i = 0; i < NUM_GENERATIONS; i++) {

            loadPopulationFitness();

            Arrays.sort(population);

            MUTATION_PERCENTAGE = Math.min(0.1 * INITIAL_MUTATION_PERCENTAGE * noEvolutionInterval, 0.7);
            if(MUTATION_PERCENTAGE == 0.68) System.out.println("Mutation percentage at it's maximum");




            PacmanNeuralNetwork[] newGeneration = new PacmanNeuralNetwork[POPULATION_SIZE];

            //if(i % 10 == 0)
            System.out.println("Gen: " + i);

            updateChamp(population[0]);

            for (int j = 0; j < POPULATION_SIZE - 1; j += 2) {

                if(j < start) {
                    newGeneration[j] = population[j];
                    newGeneration[j+1] = population[j+1];
                } else {
                    //Select Parents
                    PacmanNeuralNetwork parent1 = selectParent();
                    PacmanNeuralNetwork parent2 = selectParent();
                    PacmanNeuralNetwork[] children;

                    //Crossover
                    children = crossover(parent1, parent2);

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

    private PacmanNeuralNetwork mutate(PacmanNeuralNetwork individual) {
        double[] genes = individual.getNeuralNetwork();
        if (Math.random() < MUTATION_RATE) {
            for (int i = 0; i < MUTATION_PERCENTAGE * Commons.PACMAN_NETWORK_SIZE; i++) {
                int index = (int) (random.nextDouble() * Commons.PACMAN_NETWORK_SIZE);
                genes[index] = (random.nextDouble() * 2 - 1);
            }
        }
        individual.initializeNetwork(genes);
        return individual;
    }


    private PacmanNeuralNetwork[] crossover(PacmanNeuralNetwork parent1, PacmanNeuralNetwork parent2) {
        double[] genes1 = parent1.getNeuralNetwork();
        double[] genes2 = parent2.getNeuralNetwork();
        double[] child1 = new double[genes1.length];
        double[] child2 = new double[genes2.length];

        int crossoverPoint = (int) (random.nextDouble() * genes1.length);

        for (int i = 0; i < genes1.length; i++) {
            child1[i] = (i < crossoverPoint) ? genes1[i] : genes2[i];
            child2[i] = (i < crossoverPoint) ? genes2[i] : genes1[i];
        }

        PacmanNeuralNetwork offspring1 = new PacmanNeuralNetwork(child1);
        PacmanNeuralNetwork offspring2 = new PacmanNeuralNetwork(child2);
        return new PacmanNeuralNetwork[]{offspring1, offspring2};

    }


    // Realiza seleção por torneio
    private PacmanNeuralNetwork selectParent() {
        PacmanNeuralNetwork best = population[(int) (random.nextDouble() * POPULATION_SIZE)];

        for (int i = 1; i < k_tournament; i++) {
            PacmanNeuralNetwork c = population[(int) (random.nextDouble() * POPULATION_SIZE)];

            if (c.getFitness() > best.getFitness())
                best = c;

        }
        return best;
    }

    // load random population
    private void loadPopulationFitness() {
        for(PacmanNeuralNetwork nn : population) nn.calculateFitness(seed);
    }

    // generate random population
    private void generatePopulation() {
        for (int i = 0; i < population.length; i++) {
            population[i] = new PacmanNeuralNetwork();
        }
    }

}

