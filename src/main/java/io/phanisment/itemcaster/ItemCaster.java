package io.phanisment.itemcaster;

import org.bukkit.plugin.java.JavaPlugin;

import io.phanisment.itemcaster.SkillExecutor;

public class ItemCaster extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		
		getServer().getPluginManager().registerEvents(new SkillExecutor(), this);
	}
}