package io.phanisment.itemcaster.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.bukkit.events.MythicPlayerSignalEvent;
import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.bukkit.events.MythicDropLoadEvent;
import io.lumine.mythic.bukkit.BukkitAdapter;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;
import io.phanisment.itemcaster.skills.SkillActivator;
import io.phanisment.itemcaster.skills.conditions.AttackCooldownCondition;
import io.phanisment.itemcaster.skills.drops.ItemCasterDrop;
import io.phanisment.itemcaster.skills.mechanics.SetItemModelMechanic;
import io.phanisment.itemcaster.skills.mechanics.SetItemTypeMechanic;
import io.phanisment.itemcaster.skills.mechanics.AddEnchantmentMechanic;

public class MythicMobsListener implements Listener {
	private ItemCaster plugin;
	
	public MythicMobsListener(ItemCaster plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onReloaded(MythicReloadedEvent event) {
		if (plugin.getConfig().getBoolean("auto_reload")) {
			Message.send("Reloading...");
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				try {
					plugin.reloadConfigs();
					Message.send("Reload " + plugin.itemConfig.allItems + " Items!");
				} catch (Exception e) {
					Message.send("An error occurred while reloading the configuration.");
				}
			});
		}
	}
	
	@EventHandler
	public void onConditionLoad(MythicConditionLoadEvent event) {
		switch(event.getConditionName().toLowerCase()) {
			case "attackcooldown":
				event.register(new AttackCooldownCondition());
				break;
		}
	}
	
	@EventHandler
	public void onMechanicLoad(MythicMechanicLoadEvent event) {
		switch(event.getMechanicName().toLowerCase()) {
			case "setitemmodel":
				event.register(new SetItemModelMechanic(event.getConfig()));
				break;
			case "setitemtype":
				event.register(new SetItemTypeMechanic(event.getConfig()));
				break;
			case "addenchantment":
				event.register(new AddEnchantmentMechanic(event.getConfig()));
				break;
		}
	}
	
	@EventHandler
	public void onDropLoad(MythicDropLoadEvent event) {
		if (event.getDropName().equalsIgnoreCase("itemcaster")) {
			event.register(new ItemCasterDrop(event.getConfig(), event.getArgument()));
		}
	}
	
	@EventHandler
	public void onSignal(MythicPlayerSignalEvent event) {
		Player player = BukkitAdapter.adapt(event.getProfile().getEntity().asPlayer());
		runSkill(player, SkillActivator.Activator.SIGNAL, event.getSignal());
	}
	
	private static void runSkill(Player player, SkillActivator.Activator type, String signal) {
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		if (mainHand != null && mainHand.getType() != Material.AIR) {
			new SkillActivator(player, mainHand, type, signal);
		}
		
		ItemStack offHand = player.getInventory().getItemInOffHand();
		if (offHand != null && offHand.getType() != Material.AIR) {
			new SkillActivator(player, offHand, type, signal);
		}
		
		ItemStack helmet = player.getInventory().getHelmet();
		if (helmet != null && helmet.getType() != Material.AIR) {
			new SkillActivator(player, helmet, type, signal);
		}
		
		ItemStack chestplate = player.getInventory().getChestplate();
		if (chestplate != null && chestplate.getType() != Material.AIR) {
			new SkillActivator(player, chestplate, type, signal);
		}
		
		ItemStack leggings = player.getInventory().getLeggings();
		if (leggings != null && leggings.getType() != Material.AIR) {
			new SkillActivator(player, leggings, type, signal);
		}
		
		ItemStack boots = player.getInventory().getBoots();
		if (boots != null && boots.getType() != Material.AIR) {
			new SkillActivator(player, boots, type, signal);
		}
	}
}