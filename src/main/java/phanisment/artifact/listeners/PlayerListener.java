package phanisment.artifact.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import phanisment.artifact.skill.ItemNbtManager;
import phanisment.artifact.skill.ItemNbtManager.Activator;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			new ItemNbtManager(event.getPlayer(), Activator.LEFT_CLICK).check();
		}
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			new ItemNbtManager(event.getPlayer(), Activator.RIGHT_CLICK).check();
		}
	}
}