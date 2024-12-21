package phanisment.itemcaster.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfig {
	private final Map<String, ItemStack> items = new HashMap<>();
	private final JavaPlugin plugin;

	public ItemConfig(JavaPlugin plugin) {
		this.plugin = plugin;
		loadItems();
	}

	public void loadItems() {
		items.clear();
		File itemFolder = new File(plugin.getDataFolder(), "items");
		if (!itemFolder.exists()) {
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
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (config.contains("items")) {
			for (String id : config.getConfigurationSection("items").getKeys(false)) {
				String type = config.getString("items." + id + ".type", "STONE");
				String nbtString = config.getString("items." + id + ".nbt", "{}");
				ItemStack item = createItem(type, nbtString);
				if (item != null) {
					items.put(id.toLowerCase(), item);
				} else {
					plugin.getLogger().warning("Can not load item [" + id + "] form file [" + file.getName() + "].");
				}
			}
		} else {
			plugin.getLogger().warning("Can not load file [" + file.getName() + "] because the structure of config is wrong!");
		}
	}

	private ItemStack createItem(String type, String nbtString) {
		try {
			Material material = Material.valueOf(type.toUpperCase());
			ItemStack item = new ItemStack(material);
			try {
				NBTItem nbtItem = new NBTItem(item);
				ReadWriteNBT nbtData = NBT.parseNBT(nbtString);
				nbtItem.mergeCompound(nbtData);
				item = nbtItem.getItem();
			} catch (Exception e) {
				plugin.getLogger().warning("Format NBT invalid: " + e.getMessage());
				return null;
			}
			return item;
		} catch (IllegalArgumentException e) {
			plugin.getLogger().warning("Material " + type + " is invalid!");
			return null;
		}
	}

	public ItemStack getItem(String id) {
		return items.get(id.toLowerCase());
	}
	
	public Map<String, ItemStack> getItemList() {
		return items;
	}
}