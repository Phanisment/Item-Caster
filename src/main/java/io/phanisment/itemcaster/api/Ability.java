package io.phanisment.itemcaster.api;

import io.phanisment.itemcaster.AbilityManager;

public class Ability {
	private final AbilityManager skill;
	private final AbilityManager event;
	private final AbilityManager timer;
	
	public String getSkill() {
		return skill;
	}
	
	public String getEvent() {
		return event;
	}
	
	public int getTimer() {
		return timer;
	}
}
