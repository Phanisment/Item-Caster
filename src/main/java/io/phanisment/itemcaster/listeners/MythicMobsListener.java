package io.phanisment.itemcaster.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.bukkit.events.MythicPlayerSignalEvent;
import io.lumine.mythic.bukkit.BukkitAdapter;

import io.phanisment.itemcaster.skills.SkillActivator;
import io.phanisment.itemcaster.skills.conditions.AttackCooldownCondition;
import io.phanisment.itemcaster.skills.mechanics.SetItemModelMechanic;
import io.phanisment.itemcaster.skills.mechanics.SetItemTypeMechanic;
import io.phanisment.itemcaster.skills.mechanics.AddEnchantmentMechanic;

public class MythicMobsListener implements Listener {
	@EventHandler
	public void onConditionLoad(MythicConditionLoadEvent event) {
		if(event.getConditionName().equalsIgnoreCase("attackcooldown")) {
			event.register(new AttackCooldownCondition());
		}
	}
	
	@EventHandler
	public void onMechanicLoad(MythicMechanicLoadEvent event) {
		if (event.getMechanicName().equalsIgnoreCase("setitemmodel")) {
			event.register(new SetItemModelMechanic(event.getConfig()));
		}
		if (event.getMechanicName().equalsIgnoreCase("setitemtype")) {
			event.register(new SetItemTypeMechanic(event.getConfig()));
		}
		if (event.getMechanicName().equalsIgnoreCase("addenchantment")) {
			event.register(new AddEnchantmentMechanic(event.getConfig()));
		}
	}
	
	@EventHandler
	public void onSignal(MythicPlayerSignalEvent event) {
		Player player = BukkitAdapter.adapt(event.getProfile().getEntity().asPlayer());
		runSkill(player, SkillActivator.Activator.SIGNAL, event.getSignal());
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
	}
}