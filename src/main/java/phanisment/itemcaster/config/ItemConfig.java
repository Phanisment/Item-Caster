package phanisment.itemcaster.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;

import dev.lone.itemsadder.api.CustomStack;

import phanisment.itemcaster.Main;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemConfig {
	private static final Map<String, ItemStack> items = new HashMap<>();
	private final Main plugin;

	public ItemConfig(Main plugin) {
		this.plugin = plugin;
		loadItems();
	}

	public void loadItems() {
		items.clear();
		File itemFolder = new File(plugin.getDataFolder(), "items");
		if (!itemFolder.exists()) {
			itemFolder.mkdirs();
			plugin.saveResource("items/example.yml", false);
		}
		File[] files = itemFolder.listFiles((dir, name) -> name.endsWith(".yml") && !name.contains(" "));
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
		String fileName = file.getName().replace(".yml", "").toLowerCase();
		if (config.contains("items")) {
			for (String id : config.getConfigurationSection("items").getKeys(false)) {
				String type = config.getString("items." + id + ".type", "STONE");
				String displayName = config.getString("items." + id + ".displayname");
				List<String> lore = config.getStringList("items." + id + ".lore");
				List<String> enchants = config.getStringList("items." + id + ".enchantments");
				List<String> attributes = config.getStringList("items." + id + ".attributes");
				List<String> hideFlags = config.getStringList("items." + id + ".hideflags");
				Map<String, Object> options = parseOptions(config, id);
				String nbtString = config.getString("items." + id + ".nbt", "{}");
				List<Map<String, Object>> abilities = (List<Map<String, Object>>)config.get("items." + id + ".abilities");
				ItemStack item = createItem(type, nbtString, displayName, lore, options, enchants, abilities, attributes, hideFlags);
				if (item != null) {
					items.put(fileName + ":" + id.toLowerCase(), item);
				} else {
					plugin.getLogger().warning("Can not load item [" + id + "] form file [" + file.getName() + "].");
				}
			}
		} else {
			plugin.getLogger().warning("Can not load file [" + file.getName() + "] because the structure of config is wrong!");
		}
	}

	private ItemStack createItem(String type, String nbtString, String displayName, List<String> lore, Map<String, Object> options, List<String> enchants, List<Map<String, Object>> abilities, List<String> attributes, List<String> hideFlags) {
		try {
			ItemStack item = new ItemStack(Material.STONE);
			
			if (type.contains(":")) {
				String[] parts = type.split(":");
				String plugin = parts[0];
				String name = parts[1];
				switch(plugin.toLowerCase()) {
					case "mythicmobs":
						Optional<MythicItem> mythicItem = MythicBukkit.inst().getItemManager().getItem(name);
						if (mythicItem.isPresent()) {
							MythicItem mi = mythicItem.get();
							item = BukkitAdapter.adapt(mi.generateItemStack(1));
						} else {
							this.plugin.getLogger().warning("MythicMobs item not found: " + name);
						}
						break;
					case "itemsadder":
						if (Main.hasItemsAdder) {
							CustomStack stack = CustomStack.getInstance(name + ":" + parts[2]);
							if (stack != null) item = stack.getItemStack();
						}
						break;
					default:
						this.plugin.getLogger().warning("Unknown external type: " + plugin);
						break;
				}
			} else {
				Material material = Material.valueOf(type.toUpperCase());
				item = new ItemStack(material);
			}
			
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				
				// Display Name
				if (displayName != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r" + displayName));
				
				// Lore
				if (!lore.isEmpty()) {
					List<String> lores = new ArrayList<>();
					for (String line : lore) {
						lores.add(ChatColor.translateAlternateColorCodes('&', "&r" + line));
					}
					meta.setLore(lores);
				}
				
				// Enchantment
				if (enchants != null && !enchants.isEmpty()) {
					for (String enchant : enchants) {
						String[] parts = enchant.split(" ");
						if (parts.length == 2) {
							String enchantName = parts[0].toUpperCase();
							int level;
							try {
								level = Integer.parseInt(parts[1]);
								Enchantment enchantment = Enchantment.getByName(enchantName);
								if (enchantment != null) {
									meta.addEnchant(enchantment, level, true);
								} else {
									plugin.getLogger().warning("Enchant [" + enchantName + " ] is invalid for item" + type);
								}
							} catch (NumberFormatException e) {
								plugin.getLogger().warning("Level enchantment [" + enchant + "] is invalid!");
							}
						} else {
							plugin.getLogger().warning("Format enchantment [" + enchant + "] is wrong!");
						}
					}
				}
				
				// Attributes
				if (attributes != null) {
					for (String attributeEntry : attributes) {
						String[] parts = attributeEntry.split(" ");
						if (parts.length == 3) {
							try {
								String attributeName = parts[0].toUpperCase();
								double value = Double.parseDouble(parts[1]);
								String slotName = parts[2].toUpperCase();
								Attribute attribute = Attribute.valueOf(attributeName);
								EquipmentSlot slot = EquipmentSlot.valueOf(slotName);
								AttributeModifier modifier = new AttributeModifier(
									UUID.randomUUID(),
									attributeName + "_" + slotName,
									value,
									AttributeModifier.Operation.ADD_NUMBER,
									slot
								);
								meta.addAttributeModifier(attribute, modifier);
							} catch (IllegalArgumentException | NullPointerException e) {
								plugin.getLogger().warning("Invalid attribute entry: " + attributeEntry + " (" + e.getMessage() + ")");
							}
						} else {
							plugin.getLogger().warning("Invalid attribute format: " + attributeEntry);
						}
					}
				}
				
				// HideFlags
				if (hideFlags != null) {
					for (String flag : hideFlags) {
						try {
							ItemFlag itemFlag = ItemFlag.valueOf(flag.toUpperCase());
							meta.addItemFlags(itemFlag);
						} catch (IllegalArgumentException e) {
							plugin.getLogger().warning("HideFlag [" + flag + "] is invalid.");
						}
					}
				}
				
				// Options
				if (options != null) {
					for (Map.Entry<String, Object> entry : options.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						switch (key) {
							case "unbreakable":
								if (value instanceof Boolean) {
									meta.setUnbreakable((Boolean)value);
								}
								break;
							case "modeldata":
								if (value instanceof Integer) {
									meta.setCustomModelData((Integer)value);
								}
								break;
						}
					}
				}
				item.setItemMeta(meta);
			}
			
			NBTItem nbtItem = new NBTItem(item);
			// Nbt Parsing
			if (!nbtString.isEmpty()) {
				try {
					ReadWriteNBT nbtData = NBT.parseNBT(nbtString);
					nbtItem.mergeCompound(nbtData);
				} catch (Exception e) {
					plugin.getLogger().warning("Format NBT invalid: " + e.getMessage());
					return null;
				}
			}
			
			// Abilities
			if (abilities != null) {
				NBTCompoundList artifactList = nbtItem.getCompoundList("Artifact");
				for (Map<String, Object> ability : abilities) {
					if (!ability.containsKey("skill") || !ability.containsKey("activator")) {
						plugin.getLogger().warning("Ability missing required keys: " + ability);
						continue;
					}
					NBTCompound skillCompound = artifactList.addCompound();
					skillCompound.setString("skill", (String)ability.get("skill"));
					skillCompound.setString("activator", (String)ability.get("activator"));
					if (ability.containsKey("cooldown")) {
						skillCompound.setInteger("cooldown", (Integer)ability.get("cooldown"));
					}
					if (ability.containsKey("power")) {
						skillCompound.setFloat("power", (Float)ability.get("power"));
					}
				}
			}
			return nbtItem.getItem();
		} catch (IllegalArgumentException e) {
			plugin.getLogger().warning("Material " + type + " is invalid!");
			return null;
		}
	}

	private Map<String, Object> parseOptions(YamlConfiguration config, String id) {
		Map<String, Object> options = new HashMap<>();
		if (config.contains("items." + id + ".options")) {
			for (String key : config.getConfigurationSection("items." + id + ".options").getKeys(false)) {
				Object value = config.get("items." + id + ".options." + key);
				options.put(key.toLowerCase(), value);
			}
		}
		return options;
	}

	public List<String> getListFile() {
		File itemFolder = new File(plugin.getDataFolder(), "items");
		List<String> fileList = new ArrayList<>();
		if (itemFolder.exists() && itemFolder.isDirectory()) {
			File[] files = itemFolder.listFiles((dir, name) -> name.endsWith(".yml") && !name.contains(" "));;
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						fileList.add(file.getName());
					}
				}
			}
		}
		return fileList;
	}

	public ItemStack getItem(String id) {
		return items.get(id.toLowerCase());
	}
	
	public static Map<String, ItemStack> getItemList() {
		return items;
	}
}