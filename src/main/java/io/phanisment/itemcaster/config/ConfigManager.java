package io.phanisment.itemcaster.config;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;

public class ConfigManager extends AbstractConfig {
	public ConfigManager(JavaPlugin plugin) {
		super(plugin, "config.yml");
	}
	
	@Override
	public void addDefaults() {
		addDefault("item_test", Map.of(
			"item", "STICK",
			"displayname", "Test Item",
			"modeldata", 1
		));
	}
}