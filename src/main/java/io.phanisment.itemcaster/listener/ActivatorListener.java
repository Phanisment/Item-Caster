package io.phanisment.itemcaster.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.block.Action;

import io.phanisment.itemcaster.skill.SkillActivator;
import io.phanisment.itemcaster.skill.SkillActivator.Activator;
import io.phanisment.itemcaster.util.ItemUtil;

import java.util.HashMap;
import java.util.Map;

public class ActivatorListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemUtil.runSkill(player, Activator.RIGHT_CLICK);
		}
	}
	
	@EventHandler
	public void onPlayerSwing(PlayerAnimationEvent e) {
		Player player = e.getPlayer();
		switch (e.getAnimationType()) {
			case ARM_SWING:
				ItemStack hand = player.getInventory().getItemInMainHand();
				if (hand != null && hand.getType() != Material.AIR) {
					new SkillActivator(player, hand, Activator.LEFT_CLICK);
				}
				break;
			case OFF_ARM_SWING:
				ItemStack offHand = player.getInventory().getItemInOffHand();
				if (offHand != null && offHand.getType() != Material.AIR) {
					new SkillActivator(player, offHand, Activator.LEFT_CLICK);
				}
				break;
		}
	}
}