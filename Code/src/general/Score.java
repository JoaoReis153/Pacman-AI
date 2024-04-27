package general;

public class Score implements Comparable<Score> {
	private final double fitness;
	private final int seed;

	public Score(int seed, double fitness) {
		this.seed = seed;
		this.fitness = fitness;
	}

	public double getFitness() {
		return fitness;
	}

	public int getSeed() {
		return seed;
	}

	@Override
	public int compareTo(Score o) {
		return Double.compare(o.getFitness(), getFitness());
	}

	@Override
	public String toString() {
		return "( " + seed + " )" +  " - " + getFitness();
	}
}
