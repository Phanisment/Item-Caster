package io.phanisment.itemcaster.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;

import io.phanisment.itemcaster.MythicMobs;

public class SkillRunnable extends BukkitRunnable {
	private final Player player;
	private final String SKILL;
	
	public SkillRunnable(Player player, String skill) {
		this.playe = player;
		this.SKILL = skill
	}
	
	@Override
	public void run() {
		ItemStack item = this.player.getInventory().getItemInMainHand();
		if (item != null) {
			MythicMobs.runSkill(this.SKILL, this.player);
		}
	}
}