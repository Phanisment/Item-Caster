package io.phanisment.itemcaster.common.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import io.phanisment.itemcaster.util.SkillManager;

public class SkillExecutor implements Listener {
	private SkillManager skill = new SkillManager();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		switch (event.getAction()) {
			case Action.RIGHT_CLICK_AIR:
				skill.runSkill(player, "right_click_air");
				break;
			case Action.RIGHT_CLICK_BLOCK:
				skill.runSkill(player, "right_click_block");
				break;
			case Action.LEFT_CLICK_AIR:
				skill.runSkill(player, "left_click_air");
				break;
			case Action.LEFT_CLICK_BLOCK:
				skill.runSkill(player, "left_click_block");
				break;
			default:
				break;
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			skill.runSkill(player, "right_click");
		}
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			skill.runSkill(player, "left_click");
		}
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		skill.runSkill(player, "toggle_sneak");
		if (event.isSneaking()) {
			skill.runSkill(player, "sneak");
		} else {
			skill.runSkill(player, "unsneak");
		}
	}
}