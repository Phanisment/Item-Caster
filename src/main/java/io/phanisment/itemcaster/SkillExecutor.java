package io.phanisment.itemcaster;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import io.phanisment.itemcaster.util.SkillManager;

public class SkillExecutor implements Listener {
	private SkillManager skill = new SkillManager();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		// Right Clixck Events
		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			skill.runSkill(player, "right_click_air");
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			skill.runSkill(player, "right_click_block");
		}
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			skill.runSkill(player, "right_click");
		}
		
		// Left Click Events
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			skill.runSkill(player, "left_click_air");
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			skill.runSkill(player, "left_click_block");
		}
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			skill.runSkill(player, "left_click");
		}
	}
	
	// When player sneaking
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (event.isSneaking()) {
			skill.runSkill(player, "sneak");
		} else {
			skill.runSkill(player, "unsneak");
		}
	}
	
	// When player Eat/Drink item
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		skill.runSkill(event.getPlayer(), "consume");
	}
}