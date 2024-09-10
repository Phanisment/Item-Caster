package io.phanisment.itemcaster.util;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;

import io.phanisment.itemcaster.MythicMobs;

public class SkillRunnable extends BukkitRunnable {
	private final Player player;
	private final String SKILL;
	
	public SkillRunnable(Player player, String skill) {
		this.player = player;
		this.SKILL = skill;
	}
	
	@Override
	public void run() {
		this.player.sendMessage("Skill:" + this.SKILL + ", Timer:" + this.TIMER);
		MythicMobs.runSkill(this.SKILL, this.player);
	}
}
