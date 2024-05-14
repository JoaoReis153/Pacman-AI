package utils;

import pacman.PacmanBoard;

public interface Commons {

	public static final int PACMAN_STATE_SIZE = PacmanBoard.N_BLOCKS * PacmanBoard.N_BLOCKS * 2 + 2 + PacmanBoard.MAX_GHOSTS * 2;
	public static final int PACMAN_NUM_ACTIONS = 5;
	public static final int PACMAN_HIDDEN_LAYER = 12;
	public static final int PACMAN_NETWORK_SIZE = (PACMAN_STATE_SIZE * PACMAN_HIDDEN_LAYER) + PACMAN_HIDDEN_LAYER +
			(PACMAN_HIDDEN_LAYER * PACMAN_NUM_ACTIONS) + PACMAN_NUM_ACTIONS;
    public static final int SEED = 1;
}
