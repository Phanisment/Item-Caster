package phanisment.itemcaster.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicPlayerSignalEvent;

import phanisment.itemcaster.skills.conditions.AttackCooldownCondition;

public class MythicMobsListener implements Listener {
	@EventHandler
	public void onConditionLoadEvent(MythicConditionLoadEvent event) {
		if(event.getConditionName().equalsIgnoreCase("attackcooldown")) {
			event.register(new AttackCooldownCondition(event.getConfig()));
		}
	}
	/*
	public void onSignal(MythicPlayerSignalEvent event) {
		Player player = (Player)event.getProfile().getEntity().getBukkitEntity();
	}*/
}