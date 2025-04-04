package io.phanisment.itemcaster.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.config.item.CasterItem;
import io.phanisment.itemcaster.config.item.Pack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemConfig {
	public static Map<String, CasterItem> items = new HashMap<>();
	public static int all_item = 0;

	public ItemConfig() {
		loadItems();
	}

	public static void loadItems() {
		items.clear();
		all_item = 0;
		File folder = new File(ItemCaster.getInst().getDataFolder(), "items");
		if (!folder.exists()) {
			folder.mkdirs();
			ItemCaster.getInst().saveResource("items/example.yml", false);
		}
		
		File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml") && !name.contains(" "));
		if (files == null) return;
		for (File file : files) {
			if (file.isFile()) {
				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				String fileName = file.getName().replace(".yml", "").toLowerCase();
				if (config.contains("items")) config.getConfigurationSection("items").getKeys(false).forEach((id) -> {
					CasterItem ci = new CasterItem(new Pack(file, config), (ConfigurationSection)config.get("items." + id), new String[]{fileName, id}).load();
					if (ci.item != null) {
						items.put(fileName.toLowerCase() + ":" + id.toLowerCase(), ci);
						all_item++;
					} else {
						ItemCaster.getInst().getLogger().warning("Can not load item [" + id + "] form file [" + file.getName() + "].");
					}
				});
			}
		}
	}

	public static CasterItem getItem(String id) {
		return items.get(id.toLowerCase());
	}
	
	public static CasterItem getItem(String namespace, String id) {
		return getItem(namespace + ":" + id.toLowerCase());
	}
	
	public static CasterItem getItem(String[] id) {
		return getItem(id[0], id[1]);
	}
	
	public static Map<String, CasterItem> getItemList() {
		return items;
	}
}