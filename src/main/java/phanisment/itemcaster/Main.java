package phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import phanisment.itemcaster.command.ArtifactCommand;
import phanisment.itemcaster.listeners.MythicMobsSkills;
import phanisment.itemcaster.listeners.ActivatorListener;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		ActivatorListener.runTick(this);
		Bukkit.getPluginManager().registerEvents(new MythicMobsSkills(), this);
		Bukkit.getPluginManager().registerEvents(new ActivatorListener(this), this);
		getCommand("itemcaster").setExecutor(new ArtifactCommand());
	}
}