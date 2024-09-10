package io.phanisment.itemcaster;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import io.phanisment.itemcaster.util.SkillManager;

public class SkillExecutor implements Listener {
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null) {
			Player player = event.getPlayer();
			SkillManager skill = new SkillManager(player, "right_click");
			skill.runSkill().activeSkill();
		} else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK && event.getItem() != null) {
			Player player = event.getPlayer();
			SkillManager skill = new SkillManager(player, "left_click");
			skill.runSkill().activeSkill();
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			SkillManager skill = new SkillManager(player, "damaged");
			skill.runSkill().activeSkill();
		}
	}
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			SkillManager skill = new SkillManager(player, "attack");
			skill.runSkill().activeSkill();
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.isOnGround() && event.getTo().getY() > event.getFrom().getY()) {
			SkillManager skill = new SkillManager(player, "jump");
			skill.runSkill().activeSkill();
		}
		
	}
	
	@EventHandler
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		SkillManager skill = new SkillManager(player, "sneak");
		skill.runSkill().activeSkill();
	}
}