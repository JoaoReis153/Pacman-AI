package pacman;

import java.util.Arrays;

import breakout.BreakoutNeuralNetwork;
import utils.Commons;

public class PacmanGeneticAlgorithm {

    private final int POPULATION_SIZE = 100;
    private final int NUM_GENERATIONS = 400;
    private static final double INITIAL_MUTATION_PERCENTAGE = 0.05;
    private double MUTATION_PERCENTAGE = 0.0;
    //private double MUTATION_PERCENTAGE = .8;
    private static final double MUTATION_RATE = 0.2;
    private double SELECTION_PERCENTAGE = .2;
    private int k_tournament = 5;
    private PacmanNeuralNetwork champion;
    private int seed ;

    private int noEvolutionInterval = 0;

    private PacmanNeuralNetwork[] population = new PacmanNeuralNetwork[POPULATION_SIZE];

    // Construtor para testar CADA parâmetro durante o TREINO

    public PacmanGeneticAlgorithm(int seed) {
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

        generatePopulation();

        int start = Math.max(2, (int) (POPULATION_SIZE * SELECTION_PERCENTAGE));

        for (int i = 0; i < NUM_GENERATIONS; i++) {

            loadPopulationFitness();

            Arrays.sort(population);

            MUTATION_PERCENTAGE = Math.min(INITIAL_MUTATION_PERCENTAGE * noEvolutionInterval, 0.7);
            if(MUTATION_PERCENTAGE == 0.68) System.out.println("Mutation percentage at it's maximum");


            PacmanNeuralNetwork[] newGeneration = new PacmanNeuralNetwork[POPULATION_SIZE];

            if(i % 10 == 0)
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
                int index = (int) (Math.random() * Commons.PACMAN_NETWORK_SIZE);
                genes[index] = (Math.random() * 2 - 1);
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

        int crossoverPoint = (int) (Math.random() * genes1.length);

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
        PacmanNeuralNetwork best = population[(int) (Math.random() * POPULATION_SIZE)];

        for (int i = 1; i < k_tournament; i++) {
            PacmanNeuralNetwork c = population[(int) (Math.random() * POPULATION_SIZE)];

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

