package phanisment.itemcaster;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import phanisment.itemcaster.listener.MythicMobsSkills;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		Bukkit.getPluginManager().registerEvents(new MythicMobsSkills(this), this);
	}
}