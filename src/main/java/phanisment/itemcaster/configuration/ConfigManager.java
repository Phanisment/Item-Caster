package phanisment.itemcaster.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigManager {
	private final JavaPlugin plugin;
	private File configFile;
	private FileConfiguration config;

	public ConfigManager(JavaPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.configFile = new File(plugin.getDataFolder(), fileName);
		if (!configFile.exists()) {
			plugin.saveResource(fileName, false);
		}
		this.config = YamlConfiguration.loadConfiguration(configFile);
	}

	public void set(String path, Object value) {
		config.set(path, value);
		save();
	}

	public void save() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Can not save the Configuration!", e);
		}
	}

	public void reload() {
		this.config = YamlConfiguration.loadConfiguration(configFile);
	}

	public FileConfiguration getConfig() {
		return config;
	}
}
