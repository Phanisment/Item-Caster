package io.phanisment.itemcaster.api;

import io.phanisment.itemcaster.AbilityManager;

public class Ability {
	private final AbilityManager skill;
	private final AbilityManager event;
	private final AbilityManager timer;
	
	public Ability(String skill, String event, int timer) {
		String this.skill = skill;
		String this.event = event;
		int this.timer = timer;
	}
}
