package phanisment.itemcaster.configuration.item;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfig {
	private final Map<String, ItemStack> items = new HashMap<>();
	private final JavaPlugin plugin;
	
	public ItemConfig(JavaPlugin plugin) {
		this.plugin = plugin;
		
		File itemFolder = new File(plugin.getDataFolder(), "items");
		if(!itemFolder.exists()) {
			itemFolder.mkdirs();
		}
		
		File[] files = itemFolder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					loadFileConfig(file);
				}
			}
		}
	}
	
	private void loadFileConfig(File file) {
		items.clear();
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (config.contains("items")) {
			for (String id : config.getConfigurationSection("items").getKeys(false)) {
				String material = config.getString("items." + id + ".material", "STONE");
				String name = config.getString("items." + id + ".displayname", id);
				String modelData = config.getInteger("items." + id + ".modeldata", 0);
				boolean unbreakable = config.getBoolean("items." + id + ".unbreakable", false);
				List<Map<String, Object>> ability = getAbility(config, id);
			}
		} else {
			plugin.getLogger().warning("Can not load file [" + file.getName() + "] because the structure of config is wrong!");
		}
	}
	
	private List<Map<String, Object>> getAbility(YamlConfiguration config, String id) {
		List<Map<String, Object>> abilities = new ArrayList<>();
		if (config.contains("items." + id + ".abilities")) {
			List<Map<?, ?>> abilityList = config.getMapList("items." + id + ".abilities");
			for (Map<?, ?> ability : abilityList) {
				Map<String, Object> abilityData = new HashMap<>();
				abilityData.put("skill", ability.get("skill"));
				abilityData.put("activator", ability.get("activator"));
				if (ability.containsKey("cooldown")) {
					abilityData.put("cooldown", ability.get("cooldown"));
				}
				if (ability.containsKey("power")) {
					abilityData.put("power", ability.get("power"));
				}
				abilities.add(abilityData);
			}
		}
		return abilities;
	}
	
	public ItemStack createItem(String material, String name, int modelData, boolean unbreakable) {
		try {
			ItemStack item = new ItemStack(Material.valueOf(material.toUpperCase()));
			ItemMeta meta = item.getItemMeta();
			if (meta != null ) {
				meta.setDisplayName(name);
				meta.setUnbreakable(unbreakable);
				if (!model == 0) {
					meta.setCustomModelData(modelData);
				}
				item.setItemMeta(meta);
			}
			return item;
		} catch (IllegalArgumentException e) {
			plugin.getLogger().warning("Can not create item [" + name + "] because something is wrong!");
			return null;
		}
	}
	
	public ItemStack getItem(String id) {
		return items.get(id);
	}
}