package phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import phanisment.itemcaster.listeners.MythicMobsSkills;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		Bukkit.getPluginManager().registerEvents(new MythicMobsSkills(this), this);
	}
}