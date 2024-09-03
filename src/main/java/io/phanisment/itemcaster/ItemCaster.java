package io.phanisment.itemcaster;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
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
		if(event.getAction() == Action.RIGHT_CLICK || event.getItem() != null) {
			Player player = event.getPlayer();
			AbilityManager nbt = AbilityManager(player);
			String skill = nbt.getSkill();
			String event = nbt.getEvent();
			int timer = nbt.getTimer();
			
			player.sendMessage(skill);
			player.sendMessage(event);
			player.sendMessage(timer);
		}
	}
}