package io.phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.Material;
import org .bukkit.entity.Player;

import io.phanisment.itemcaster.AbilityManager;

public class ItemCaster extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			AbilityManager nbt = AbilityManager(event.getItem());
			String skill = nbt.getSkill();
			String event = nbt.getEvent();
			int timer = nbt.getTimer().toString();
			Bukkit.broadcastMessage("Skill: " + skill + ", Event: " + event + ", Timer: " + timer);
		}
	}
}