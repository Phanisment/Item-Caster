package phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import phanisment.itemcaster.command.BaseCommand;
import phanisment.itemcaster.listeners.MythicMobsListener;
import phanisment.itemcaster.listeners.ActivatorListener;
import phanisment.itemcaster.config.ItemConfig;

public class Main extends JavaPlugin {
	public static boolean hasPapi = false;
	public static boolean hasNexo = false;
	public static boolean hasOraxen = false;
	public static boolean hasItemsAdder = false;
	public static ItemConfig itemConfig;
	
	@Override
	public void onEnable() {
		this.itemConfig = new ItemConfig(this);
		ActivatorListener.runTick(this);
		getServer().getPluginManager().registerEvents(new MythicMobsListener(), this);
		getServer().getPluginManager().registerEvents(new ActivatorListener(), this);
		getCommand("itemcaster").setExecutor(new BaseCommand(this));
		getCommand("itemcaster").setTabCompleter(new BaseCommand(this));
		
		boolean hasIa = getServer().getPluginManager().getPlugin("ItemsAdder") != null ? true : false;
		if (hasIa) {
			this.hasItemsAdder = true;
			getLogger().info("ItemsAdder detected, activate ItemsAdder feature");
		}
	}

	@Override
	public void onLoad() {
		saveDefaultConfig();
	}
}