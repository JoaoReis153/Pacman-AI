package pacman;


public class Main {

    public static void main(String[] args) {

        int seed = 547;

        PacmanGeneticAlgorithm ga = new PacmanGeneticAlgorithm(seed);
        PacmanNeuralNetwork nn = ga.getChampion();
        new Pacman(nn, true, seed);
        /*
        int n = 500;
        ArrayList<Score> scoreList = new ArrayList<>();
        for(int i = n; i < n * 2; i++) {

            PacmanGeneticAlgorithm ga = new PacmanGeneticAlgorithm(i);
            PacmanNeuralNetwork nn = ga.getChampion();
            scoreList.add(new Score(i, nn.getFitness()));

            Collections.sort(scoreList);
            System.out.println("Best: ");
            System.out.println(scoreList.getFirst());

        }

        for(int i = 0; i < scoreList.size(); i++) {
            System.out.println(scoreList.get(i));
        }
        */


    }
}

