package phanisment.artifact.listeners;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import phanisment.artifact.Main;
import phanisment.artifact.skills.conditions.IsAttackOnCooldownCondition;

public class MythicMobsSkills implements Listener {
	private Main pl;
	
	public MythicMobsSkills(Main pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onConditionLoadEvent(MythicConditionLoadEvent event) {
		if(event.getConditionName().equalsIgnoreCase("attackCooldown")) {
			event.register(new IsAttackOnCooldownCondition(event.getConfig()));
		}
	}
}