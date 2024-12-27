package phanisment.itemcaster.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicPlayerSignalEvent;

import phanisment.itemcaster.skills.SkillActivator;
import phanisment.itemcaster.skills.conditions.AttackCooldownCondition;

public class MythicMobsListener implements Listener {
	@EventHandler
	public void onConditionLoadEvent(MythicConditionLoadEvent event) {
		if(event.getConditionName().equalsIgnoreCase("attackcooldown")) {
			event.register(new AttackCooldownCondition(event.getConfig()));
		}
	}
	/*
	@EventHandler
	public void onSignal(MythicPlayerSignalEvent event) {
		Entity entity = event.getProfile().getEntity().getBukkitEntity();
		if (entity instanceof Player) {
			Player player = (Player)entity;
			runSkill(player, SkillActivator.Activator.SIGNAL, event.getSignal());
		}
	}
	private static void runSkill(Player player, SkillActivator.Activator type, String signal) {
		ItemStack hand = player.getInventory().getItemInMainHand();
		if (hand != null || hand.getType() != Material.AIR) {
			new SkillActivator(player, hand, type, signal);
		}
		ItemStack offHand = player.getInventory().getItemInOffHand();
		if (offHand != null || offHand.getType() != Material.AIR) {
			new SkillActivator(player, offHand, type, signal);
		}
	}*/
}