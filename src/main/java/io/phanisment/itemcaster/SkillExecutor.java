package io.phanisment.itemcaster;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;

public class SkillExecutor implements Listener {
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			ItemStack item = player.getInventory().getItemInMainHand();
			AbilityManager abilityManager = new AbilityManager(item);
			String skill = abilityManager.getSkill();
			if (skill != null) {
				player.sendMessage("You have activated skill: " + skill);
			} else {
				player.sendMessage("This item has no abilities.");
			}
		}
	}
}