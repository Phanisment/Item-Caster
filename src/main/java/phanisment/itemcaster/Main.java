package phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import phanisment.itemcaster.listeners.MythicMobsSkills;
import phanisment.itemcaster.listeners.PlayerListener;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		Bukkit.getPluginManager().registerEvents(new MythicMobsSkills(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}
}