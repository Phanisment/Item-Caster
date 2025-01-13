package io.phanisment.itemcaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import io.phanisment.itemcaster.command.BaseCommand;
import io.phanisment.itemcaster.listeners.MythicMobsListener;
import io.phanisment.itemcaster.listeners.ActivatorListener;
import io.phanisment.itemcaster.config.*;
import io.phanisment.itemcaster.util.Message;

import java.util.HashMap;
import java.util.Map;

public class ItemCaster extends JavaPlugin {
	private final Map<String, AbstractConfig> configManagers = new HashMap<>();
	private static boolean hasItemsAdder;
	private static ItemConfig itemConfig;
	private static ItemCaster plugin;
	
	public ItemCaster() {
		plugin = this;
	}
	
	@Override
	public void onLoad() {
		registerConfigManager("message", new MessageConfig(this));
		registerConfigManager("config", new ConfigManager(this));
		this.itemConfig = new ItemConfig(this);
	}
	
	@Override
	public void onEnable() {
		Message.sendConsole("The plugin is loaded!");
		ActivatorListener.runTick(this);
		getServer().getPluginManager().registerEvents(new MythicMobsListener(), this);
		getServer().getPluginManager().registerEvents(new ActivatorListener(this), this);
		getCommand("itemcaster").setExecutor(new BaseCommand(this));
		getCommand("itemcaster").setTabCompleter(new BaseCommand(this));
		if (getServer().getPluginManager().getPlugin("ItemsAdder") != null) {
			Message.sendConsole("ItemsAdder detected, activate ItemsAdder feature");
			this.hasItemsAdder = true;
		}
	}
	
	@Override
	public void onDisable() {
		Message.sendConsole("Disabled the plugin.");
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
	
	public void registerConfigManager(String key, AbstractConfig manager) {
		manager.checkDefaults();
		configManagers.put(key, manager);
	}

	public AbstractConfig getConfigManager(String key) {
		return configManagers.get(key);
	}

	public Map<String, AbstractConfig> getConfigManagers() {
		return configManagers;
	}

	public FileConfiguration getConfig(String key) {
		return (getConfigManager(key) != null) ? getConfigManager(key).getConfig() : null;
	}
}