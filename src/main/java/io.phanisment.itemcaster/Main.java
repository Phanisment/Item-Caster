package io.phanisment.itemcaster;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Main instances;
	
	public Main() {
		instances = this;
	}
	
	@Override
	public void onEnable() {
	}
	
	public static Main getInst() {
		return instances;
	}
}