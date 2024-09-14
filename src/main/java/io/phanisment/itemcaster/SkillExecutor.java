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
			skill.runSkill(player, "right_click_air").activeSkill();
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			skill.runSkill(player, "right_click_block").activeSkill();
		}
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			skill.runSkill(player, "right_click").activeSkill();
		}
		
		// Left Click Events
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			skill.runSkill(player, "left_click_air").activeSkill();
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			skill.runSkill(player, "left_click_block").activeSkill();
		}
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			skill.runSkill(player, "left_click").activeSkill();
		}
		
		// Swap Hand
		if (event.getAction() == Action.PHYSICAL) {
			ItemStack mainHandItem = player.getInventory().getItemInMainHand();
			ItemStack offHandItem = player.getInventory().getItemInOffHand();
			if (!mainHandItem.equals(player.getInventory().getItemInMainHand()) || !offHandItem.equals(player.getInventory().getItemInOffHand())) {
				skill.runSkill(player, "swap_hand").activeSkill();
			}
		}
	}
	
	// When player sneaking
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (event.isSneaking()) {
			skill.runSkill(player, "sneak").activeSkill();
		} else {
			skill.runSkill(player, "unsneak").activeSkill();
		}
	}
	
	// When player Eat/Drink item
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		skill.runSkill(event.getPlayer(), "consume").activeSkill();
	}
	
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		if (projectile.getShooter() instanceof Player) {
			Player player = (Player) projectile.getShooter();
			skill.runSkill(player, "projectile_launch").activeSkill();
		}
	}
	
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		Player player = (Player) projectile.getShooter();
		skill.runSkill(player, "projectile_hit").activeSkill();
	}
	
	public void onEntityDamage(EntityDamageEvent event) {
		Player player = (Player) event.getEntity();
		if (event.getEntity() instanceof Player) {
			skill.runSkill(player, "damaged").activeSkill();
		}
	}
	
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			skill.runSkill(player, "attack").activeSkill();
		}
	}
	
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		skill.runSkill(player, "drop").activeSkill();
	}
}