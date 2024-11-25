package phanisment.artifact;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import phanisment.artifact.command.ArtifactCommand;
import phanisment.artifact.listeners.MythicMobsSkills;
import phanisment.artifact.listeners.PlayerListener;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		Bukkit.getPluginManager().registerEvents(new MythicMobsSkills(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		getCommand("magicartifact").setExecutor(new ArtifactCommand());
	}
}