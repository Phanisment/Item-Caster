package io.phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import io.phanisment.itemcaster.command.BaseCommand;
import io.phanisment.itemcaster.listeners.MythicMobsListener;
import io.phanisment.itemcaster.listeners.ActivatorListener;
import io.phanisment.itemcaster.config.*;
import io.phanisment.itemcaster.util.Message;

public class ItemCaster extends JavaPlugin {
	private static boolean hasItemsAdder;
	private ItemConfig itemConfig;
	private static ItemCaster plugin;
	
	public ItemCaster() {
		plugin = this;
	}
	
	@Override
	public void onLoad() {
		this.itemConfig = new ItemConfig(this);
		saveDefaultConfig();
	}
	
	@Override
	public void onEnable() {
		Message.send("The plugin is loaded!");
		ActivatorListener.runTick(this);
		getServer().getPluginManager().registerEvents(new MythicMobsListener(this), this);
		getServer().getPluginManager().registerEvents(new ActivatorListener(this), this);
		getCommand("itemcaster").setExecutor(new BaseCommand(this));
		getCommand("itemcaster").setTabCompleter(new BaseCommand(this));
		if (getServer().getPluginManager().getPlugin("ItemsAdder") != null) {
			Message.send("ItemsAdder detected, activate ItemsAdder feature");
			this.hasItemsAdder = true;
		}
	}
	
	public static ItemCaster getInst() {
		return plugin;
	}
	
	public ItemConfig getItemConfig() {
		return itemConfig;
	}
	
	public static boolean hasItemsAdder() {
		return hasItemsAdder;
	}
	
	public void reloadConfigs() {
		plugin.reloadConfig();
		plugin.getItemConfig().loadItems();
	}
}