package com.UniverseSim.Creatures;

import java.util.Comparator;

public class CreatureHealthComparator implements Comparator<Creature>{

	@Override
	public int compare(Creature creature1, Creature creature2) {
		if(creature2.getHealth() > creature1.getHealth()) {
			return 1;
		}else if(creature2.getHealth() < creature1.getHealth()) {
			return -1;
		}else {
			return 0;
		}
	}

}
