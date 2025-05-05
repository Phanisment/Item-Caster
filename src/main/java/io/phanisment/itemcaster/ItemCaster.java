package io.phanisment.itemcaster;

import org.bstats.bukkit.Metrics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


import de.tr7zw.changeme.nbtapi.NBT;

import fr.mrmicky.fastinv.FastInvManager;

import io.phanisment.itemcaster.command.BaseCommand;
import io.phanisment.itemcaster.listeners.*;
import io.phanisment.itemcaster.config.*;
import io.phanisment.itemcaster.util.Message;

public class ItemCaster extends JavaPlugin {
	public static final int plugin_id = 25172;
	public static ItemCaster plugin;
	public static Metrics metrics;
	public static boolean hasItemsAdder;
	public static boolean hasPapi;
	public ItemConfig itemConfig;
	public ItemEditConfig itemEditConfig;
	
	public ItemCaster() {
		plugin = this;
	}
	
	@Override
	public void onLoad() {
		this.itemConfig = new ItemConfig();
		this.itemEditConfig = new ItemEditConfig();
		saveDefaultConfig();
	}
	
	@Override
	public void onEnable() {
		this.metrics = new Metrics(this, plugin_id);
		FastInvManager.register(this);
		if (!NBT.preloadApi()) {
			Message.send("<color:#d61c38>NBT-API wasn't initialized properly, disabling the plugin");
			getPluginLoader().disablePlugin(this);
			return;
		}
		Message.send("The plugin is loaded!");
		new BukkitRunnable() {
			@Override
			public void run() {
				if (Bukkit.getOnlinePlayers().isEmpty()) return;
				for (Player player : Bukkit.getOnlinePlayers()) {
					ActivatorListener.tick(player);
					GuiListener.tick(player);
				}
			}
		}.runTaskTimer(this, 0L, 1L);
		getServer().getPluginManager().registerEvents(new MythicMobsListener(this), this);
		getServer().getPluginManager().registerEvents(new ActivatorListener(this), this);
		getServer().getPluginManager().registerEvents(new GuiListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getCommand("itemcaster").setExecutor(new BaseCommand(this));
		getCommand("itemcaster").setTabCompleter(new BaseCommand(this));
		if (getServer().getPluginManager().getPlugin("ItemsAdder") != null) {
			Message.send("<color:#49eb6e>ItemsAdder detected, activate ItemsAdder feature");
			this.hasItemsAdder = true;
		}
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			Message.send("<color:#49eb6e>PlaceholderAPI detected, activate PlaceholderAPI feature");
			this.hasPapi = true;
		}
	}
	
	public static ItemCaster getInst() {
		return plugin;
	}
	
	public ItemConfig getItemConfig() {
		return itemConfig;
	}
	
	public ItemEditConfig getItemEditConfig() {
		return itemEditConfig;
	}
	
	public static boolean hasItemsAdder() {
		return hasItemsAdder;
	}
	
	public static boolean hasPlaceholderAPI() {
		return hasPapi;
	}
	
	public void reloadConfigs() {
		plugin.reloadConfig();
		plugin.getItemConfig().loadItems();
		plugin.getItemEditConfig().load();
	}
}