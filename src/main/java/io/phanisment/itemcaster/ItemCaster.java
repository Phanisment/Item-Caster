package io.phanisment.itemcaster;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.Bukkit;

import io.phanisment.itemcaster.SkillExecutor;

public class ItemCaster extends JavaPlugin {
	@Override
	public void onEnable() {
		if(Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
			new JavaPluginLoader().disablePlugin("ItemCaster");
			getLogger().info("Plugin Disabled, Need plugin MythicMobs ro enable this plugin.");
		}
		getLogger().info("Plugin Installed!");
		
		getServer().getPluginManager().registerEvents(new SkillExecutor(), this);
	}
}