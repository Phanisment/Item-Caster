package io.phanisment.itemcaster.config;

import org.bukkit.configuration.file.YamlConfiguration;

import io.phanisment.itemcaster.ItemCaster;

import java.util.Map;
import java.util.HashMap;

public class ConfigManager {
	private static Map<String, AbstractConfig> configs = new HashMap<>();
	private ItemCaster plugin;
	
	public ConfigManager(ItemCaster plugin) {
		this.plugin = plugin;
		load();
	}
	
	public void load() {
		configs.keySet().forEach(key -> {
			AbstractConfig config = configs.get(key);
			if (config != null) {
				config.checkDefaults(); 
				configs.put(key, config);
			}
		});
	}
	
	public static void registerConfigs(String id, AbstractConfig config) {
		config.checkDefaults();
		configs.put(id, config);
	}
	
	public static AbstractConfig getConfig(String id) {
		return configs.get(id);
	}
}