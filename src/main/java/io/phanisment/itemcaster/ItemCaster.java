package io.phanisment.itemcaster;

import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.changeme.nbtapi.NBT;
import io.phanisment.itemcaster.util.PassiveSkill;

public class ItemCaster extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		if (!NBT.preloadApi()) {
			getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
			getPluginLoader().disablePlugin(this);
			return;
		}
		new PassiveSkill(this);
	}
}
