package io.phanisment.itemcaster;

import org.bukkit.plugin.java.JavaPlugin;

import io.phanisment.itemcaster.util.PassiveSkill;

public class ItemCaster extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		
		new PassiveSkill(this);
	}
}
