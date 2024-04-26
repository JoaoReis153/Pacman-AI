package pacman;


public class Main {

    public static void main(String[] args) {

        int seed = 3113;
        PacmanGeneticAlgorithm ga = new PacmanGeneticAlgorithm(seed);
        PacmanNeuralNetwork nn = ga.getChampion();
        new Pacman(nn, true, seed);
        new Pacman(nn, true, seed+10);


    }
}
