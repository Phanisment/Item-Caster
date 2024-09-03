package io.phanisment.itemcaster;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.phanisment.itemcaster.AbilityManager;

public class ItemCaster extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent event) {
		switch (event.getAction()) {
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK:
				Player player = event.getPlayer();
				ItemStack item = player.getInventory().getItemInMainHand();
				AbilityManager abilityManager = new AbilityManager(item);
				String skill = abilityManager.getSkill();
				if (skill != null) {
					player.sendMessage("You have activated skill: " + skill);
				} else {
					player.sendMessage("This item has no abilities.");
				}
				break;
			default:
				break;
		}
	}
}