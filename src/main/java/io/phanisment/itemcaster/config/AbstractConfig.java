package io.phanisment.itemcaster.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfig {
	private final JavaPlugin plugin;
	private static YamlConfiguration config;
	private final String fileName;
	private File configFile;

	public AbstractConfig(JavaPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		saveDefaultConfig();
	}

	public void saveDefaultConfig() {
		if (configFile == null) configFile = new File(plugin.getDataFolder(), fileName);
		if (!configFile.exists()) plugin.saveResource(fileName, false);
	}

	public void reloadConfig() {
		if (configFile == null) configFile = new File(plugin.getDataFolder(), fileName);
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public YamlConfiguration getConfig() {
		if (config == null) reloadConfig();
		return config;
	}

	public void saveConfig() {
		if (config == null || configFile == null) return;
		try {
			getConfig().save(configFile);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
		}
	}

	public void addDefault(String path, Object value) {
		if (value instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) value;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				addDefault(path + "." + entry.getKey(), entry.getValue());
			}
		} else if (!getConfig().contains(path)) {
			getConfig().set(path, value);
		}
	}

	public void checkDefaults() {
		if (configFile.exists()) {
			reloadConfig();
			addDefaults();
			saveConfig();
		} else {
			saveDefaultConfig();
			reloadConfig();
		}
	}

	public abstract void addDefaults();
}