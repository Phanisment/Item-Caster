package io.phanisment.itemcaster.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.phanisment.itemcaster.ItemCaster

public class PassiveSkill {
	private final ItemCaster plugin;
	
	public PassiveSkillChecker(ItemCaster plugin) {
		this.plugin = plugin;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					/* Code */
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
}