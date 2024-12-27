package phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import phanisment.itemcaster.command.BaseCommand;
import phanisment.itemcaster.listeners.MythicMobsListener;
import phanisment.itemcaster.listeners.ActivatorListener;
import phanisment.itemcaster.config.ItemConfig;

public class Main extends JavaPlugin {
	public static boolean isPaperMc = false;
	public static boolean hasOraxen = false;
	public static boolean hasItemsAdder = false;
	public static boolean hasNexo = false;
	public ItemConfig itemConfig;
	
	@Override
	public void onEnable() {
		this.itemConfig = new ItemConfig(this);
		ActivatorListener.runTick(this);
		Bukkit.getPluginManager().registerEvents(new MythicMobsListener(), this);
		Bukkit.getPluginManager().registerEvents(new ActivatorListener(), this);
		getCommand("itemcaster").setExecutor(new BaseCommand(this));
		getCommand("itemcaster").setTabCompleter(new BaseCommand(this));
		
		if (Bukkit.getPluginManager().getPlugin("ItemsAdder").isEnabled()) {
			this.hasItemsAdder = true;
			getLogger().info("ItemsAdder detected, activate ItemsAdder exclusive feature");
		}
	}

	@Override
	public void onLoad() {
		saveDefaultConfig();
	}
}