package io.phanisment.itemcaster.config.item;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import io.phanisment.itemcaster.config.item.CasterItem;

import java.util.List;
import java.io.File;

public class Pack {
	protected final File file;
	protected final YamlConfiguration config;
	
	public Pack(File file, YamlConfiguration config) {
		this.file = file;
		this.config = config;
	}
}