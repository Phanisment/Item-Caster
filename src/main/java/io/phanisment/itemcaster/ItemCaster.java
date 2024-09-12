package io.phanisment.itemcaster;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import io.phanisment.itemcaster.SkillExecutor;
import io.phanisment.itemcaster.util.SkillManager;

public class ItemCaster extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		getServer().getPluginManager().registerEvents(new SkillExecutor(), this);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					SkillManager.runSkill(player, "timer");
				}
			}
		}.runTaskTimer(this, 0L, 1L);
	}
}