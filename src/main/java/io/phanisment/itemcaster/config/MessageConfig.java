package io.phanisment.itemcaster.config;

import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig extends AbstractConfig {
	public MessageConfig(JavaPlugin plugin) {
		super(plugin, "message.yml");
	}
	
	@Override
	public void addDefaults() {
		addDefault("prefix", "<gradient:#62A5DF:#375DB2>ItemCaster</gradient> | ");
	}
}