package io.phanisment.itemcaster.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.config.item.ItemEditInterface;
import io.phanisment.itemcaster.config.item.ItemAbilityInterface;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemEditConfig {
	public static List<ItemEditInterface> items = new ArrayList<>();
	public static Map<String, ItemEditInterface> icon = new HashMap<>();
	public static List<ItemAbilityInterface> ability_icons = new ArrayList<>();
	
	public ItemEditConfig() {
		load();
	}
	
	public void load() {
		items.clear();
		icon.clear();
		ability_icons.clear();
		
		List<Map<String, Object>> list = (List<Map<String, Object>>)config().getList("gui.edit.interface", new ArrayList<>());
		for (Map<String, Object> config : list) {
			items.add(new ItemEditInterface(config).load());
		}
		
		List<Map<String, Object>> abilities_list = (List<Map<String, Object>>)config().getList("gui.ability.interface", new ArrayList<>());
		for (Map<String, Object> config : abilities_list) {
			ability_icons.add(new ItemAbilityInterface(config).load());
		}
		
		for (String name : config().getConfigurationSection("gui.icon").getKeys(false)) {
			icon.put(name, new ItemEditInterface(config().getConfigurationSection("gui.icon." + name)).load());
		}
	}
	
	private FileConfiguration config() {
		return ItemCaster.getInst().getConfig();
	}
	
	public static List<ItemEditInterface> getList() {
		return items;
	}
}