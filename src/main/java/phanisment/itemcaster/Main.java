package phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import phanisment.itemcaster.command.ItemCasterCommand;
import phanisment.itemcaster.listeners.MythicMobsListener;
import phanisment.itemcaster.listeners.ActivatorListener;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		ActivatorListener.runTick(this);
		Bukkit.getPluginManager().registerEvents(new MythicMobsListener(), this);
		Bukkit.getPluginManager().registerEvents(new ActivatorListener(), this);
		getCommand("itemcaster").setExecutor(new ItemCasterCommand());
		if (Bukkit.getPluginManager().getPlugin("ItemsAdder").isEnabled()) {
			
		}
	}
}