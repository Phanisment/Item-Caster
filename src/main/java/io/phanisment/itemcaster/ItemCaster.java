package io.phanisment.itemcaster;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import io.phanisment.itemcaster.SkillExecutor;
import io.phanisment.itemcaster.util.SkillManager;

public class ItemCaster extends JavaPlugin {
	private SkillManager skill;
	
	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		getServer().getPluginManager().registerEvents(new SkillExecutor(), this);
	}
}