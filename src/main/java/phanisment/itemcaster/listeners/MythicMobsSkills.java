package phanisment.itemcaster.listeners;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import phanisment.itemcaster.skills.conditions.AttackCooldownCondition;

public class MythicMobsSkills implements Listener {
	@EventHandler
	public void onConditionLoadEvent(MythicConditionLoadEvent event) {
		if(event.getConditionName().equalsIgnoreCase("attackcooldown")) {
			event.register(new AttackCooldownCondition(event.getConfig()));
		}
	}
}