package com.game.flappyBird;

import java.util.Comparator;

public class PipeComparator implements Comparator<Pipe>{

	@Override
	public int compare(Pipe pipe1, Pipe pipe2) {
		return (int) (pipe1.posX + 75 - pipe2.posX );
	}
	
}
