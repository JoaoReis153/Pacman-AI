package pacman;


import general.Score;
import utils.Commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int seed = Commons.SEED;
/*
        PacmanGeneticAlgorithm ga = new PacmanGeneticAlgorithm(seed);
        PacmanNeuralNetwork nn = ga.getChampion();
        nn.calculateFitness(seed);
        System.out.println("Best fitness: " + nn.getFitness());
        new Pacman(nn, true, seed);
       */
        int n = 1993319814;
        ArrayList<Score> scoreList = new ArrayList<>();
        for(int i = n; i < n + 200; i++) {
            System.out.println();
            System.out.println();
            System.out.println("-----Seed: " + i + " --------");
            PacmanGeneticAlgorithm ga = new PacmanGeneticAlgorithm(i);
            PacmanNeuralNetwork nn = ga.getChampion();
            scoreList.add(new Score(i, nn.getFitness()));

            Collections.sort(scoreList);
            System.out.println("Best: ");
            System.out.println(scoreList.getFirst());
            if(i - n > 10) {
                System.out.println("Top 10: ");
                for(int k = 0; k < scoreList.size(); k++) {
                    System.out.println(scoreList.get(k+1));
                }
            }

        }

        for(int i = 0; i < scoreList.size(); i++) {
            System.out.println(scoreList.get(i));
        }



    }
}

