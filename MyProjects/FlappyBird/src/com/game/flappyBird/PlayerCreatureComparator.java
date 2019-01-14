package com.game.flappyBird;

import java.util.Comparator;

public class PlayerCreatureComparator implements Comparator<PlayerCreature> {

		@Override
		public int compare(PlayerCreature player1, PlayerCreature player2) {
			return (int) (player2.getFitness() - player1.getFitness());
		}
}
