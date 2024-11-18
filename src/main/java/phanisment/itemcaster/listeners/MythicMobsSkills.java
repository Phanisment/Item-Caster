package phanisment.itemcaster.listener;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import phanisment.itemcaster.Main;
import phanisment.skills.conditions.IsAttackOnCooldownCondition;

public class MythicMobsSkills implements Listener {
	private Main pl;
	
	public MythicMobsSkills(Main pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onConditionLoadEvent(MythicConditionLoadEvent event) {
		if(event.getConditionName().equalsIgnoreCase("isAttackCooldown")) {
			event.register(new IsAttackOnCooldownCondition(event.getConfig()));
			pl.getLogger().info("-- Registered attack cooldown condition!");
		}
	}
}