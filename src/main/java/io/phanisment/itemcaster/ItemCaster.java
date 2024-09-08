package io.phanisment.itemcaster;

import org.bukkit.plugin.java.JavaPlugin;

import io.phanisment.itemcaster.SkillExecutor;

public class ItemCaster extends JavaPlugin {
	@Override
	public void onEnable() {
		if(Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
			getServer().getPluginManager().disablePlugin(this);
			getLogger().info("Plugin Disabled, Need plugin MythicMobs ro enable this plugin.");
		}
		getLogger().info("Plugin Installed!");
		
		getServer().getPluginManager().registerEvents(new SkillExecutor(), this);
	}
}