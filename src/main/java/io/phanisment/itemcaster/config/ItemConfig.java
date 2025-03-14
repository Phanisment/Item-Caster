package io.phanisment.itemcaster.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.MemorySection;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.config.item.CasterItem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemConfig {
	public static Map<String, CasterItem> items = new HashMap<>();
	public static int allItems = 0;

	public ItemConfig() {
		loadItems();
	}

	public static void loadItems() {
		items.clear();
		allItems = 0;
		File itemFolder = new File(ItemCaster.getInst().getDataFolder(), "items");
		if (!itemFolder.exists()) {
			itemFolder.mkdirs();
			ItemCaster.getInst().saveResource("items/example.yml", false);
		}
		
		File[] files = itemFolder.listFiles((dir, name) -> name.endsWith(".yml") && !name.contains(" "));
		if (files == null) return;
		for (File file : files) {
			if (file.isFile()) {
				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				String fileName = file.getName().replace(".yml", "").toLowerCase();
				if (config.contains("items")) config.getConfigurationSection("items").getKeys(false).forEach((id) -> {
					CasterItem ci = new CasterItem((MemorySection)config.get("items." + id), new String[]{fileName, id}).load();
					if (ci.item != null) {
						items.put(fileName.toLowerCase() + ":" + id.toLowerCase(), ci);
						allItems++;
					} else {
						ItemCaster.getInst().getLogger().warning("Can not load item [" + id + "] form file [" + file.getName() + "].");
					}
				});
			}
		}
	}

	public CasterItem getItem(String id) {
		return items.get(id.toLowerCase());
	}
	
	public static Map<String, CasterItem> getItemList() {
		return items;
	}
}