package io.phanisment.itemcaster;

import org.bukkit.Bukkit;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.plugin.LuminePlugin;

import io.phanisment.itemcaster.config.Configuration;
import io.phanisment.itemcaster.listener.ActivatorListener;
import io.phanisment.itemcaster.listener.RecipeListener;
import io.phanisment.itemcaster.listener.CasterRunnable;
import java.util.logging.Level;

public class ItemCaster extends LuminePlugin {
	private static ItemCaster inst;
	private static MythicBukkit mythic_inst;
	
	public static Configuration CONFIGURATION;
	
	public ItemCaster() {
		inst = this;
	}
	
	@Override
	public void enable() {
		if (!hasDependency("NBTAPI")) {
			getLogger().log(Level.SEVERE, "Plugin NBTAPI not found, this plugin required NBTAPI to work!");
			getPluginLoader().disablePlugin(this);
		}
		
		mythic_inst = MythicBukkit.inst();
		CONFIGURATION = new Configuration(this);
		getServer().getPluginManager().registerEvents(new ActivatorListener(), this);
		getServer().getPluginManager().registerEvents(new RecipeListener(), this);
		new CasterRunnable();
		
	}
	
	private static boolean hasDependency(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin) != null;
	}
	
	public static ItemCaster getInst() {
		return inst;
	}
	
	public static MythicBukkit getMythicInst() {
		return mythic_inst;
	}
}