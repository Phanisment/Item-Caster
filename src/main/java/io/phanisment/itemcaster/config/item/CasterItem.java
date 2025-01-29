package io.phanisment.itemcaster.config.item;
/*
import org.bukkit.Material;
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

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Legacy;

import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CasterItem {
	public final String[] ITEM_ID;
	public ItemStack ITEM;
	public ItemMeta META;
	
	private String type;
	private String display_name;
	private String nbt;
	private boolean unbreakable;
	private int model_data;
	private short durability;
	private List<String> lore;
	private List<String> attributes;
	private List<String> enchantments;
	private List<String> hide_flags;
	private List<String, Map<String, Object>> abilities;
	
	public CasterItem(ItemStack convertItem) {
		
	}
	
	public CasterItem(CasterItem cloneItem) {
		
	}
	
	public CasterItem(Map<String, Object> config, String[] id) {
		this.ITEM_ID = id;
		try {
			this.type = (String)config.getOrDefault("type", "STONE");
			this.nbt = (String)config.getOrDefault("nbt", "{}");
			this.display_name = (String)config.get("display_name");
			this.unbreakable = (Boolean)config.getOrDefault("unbreakable", false);
			this.model_data = (Integer)config.getOrDefault("model_data", 0);
			this.durability = (Short)config.get("durability");
			this.lore = (List<String>)config.getOrDefault("lore", new ArrayList<>());
			this.attributes = (List<String>)config.getOrDefault("attributes", new ArrayList<>());
			this.enchantments = (List<String>)config.getOrDefault("enchantments", new ArrayList<>());
			this.hide_flags = (List<String>)config.getOrDefault("hide_flags", new ArrayList<>());
			this.abilities = (List<String, Map<String, Object>>)config.getOrDefault("abilities", new HashMap<>());
		} catch (Exception e) {
			Message.send("Error creating item [" + id[0] + "] in file [" + id[1] + "]: " + e.getMessage());
		}
	}
	
	public void generate() {
		try {
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
						if (this.plugin.hasItemsAdder()) {
							CustomStack stack = CustomStack.getInstance(name + ":" + parts[2]);
							if (stack != null) item = stack.getItemStack();
						}
						break;
					default:
						Material material = Material.valueOf(type.toUpperCase());
						item = new ItemStack(material);
						this.plugin.getLogger().warning("Unknown external type: " + plugin);
						break;
				}
			} else {
				Material material = Material.valueOf(type.toUpperCase());
				item = new ItemStack(material);
			}
			if (durability != null) item
			meta = item.getItemMeta();
			if (meta != null) {
				
				// Display Name
				if (display_name != null) meta.setDisplayName(Legacy.serializer("<reset>" + display_name));
				if (model_data >= 1) meta.setCustomModelData(model_data);
				meta.setUnbreakable(unbreakable);
				
				// Lore
				List<String> lores = new ArrayList<>();
				if (!lore.isEmpty()) {
					for (String line : lore) {
						lores.add(Legacy.serializer("<reset>" + line));
					}
				}
				
				// Abilities Lore
				List<String> abilitiesLoreFormat = plugin.getConfig().getStringList("abilities.lore");
				if (abilities != null && !abilitiesLoreFormat.isEmpty()) {
					for (Map<String, Object> ability : abilities) {
						if (ability.containsKey("skill") && ability.containsKey("activator")) {
							String skill = (String)ability.get("skill");
							String activator = (String)ability.get("activator");
							boolean showInLore = (Boolean)ability.getOrDefault("show_in_lore", false);
							if (showInLore) {
								String name = (String)ability.getOrDefault("name", "");
								Integer cooldown = (Integer)ability.getOrDefault("cooldown", 0);
								Integer power = (Integer)ability.getOrDefault("power", 0);
								Map<String, Object> variable = (Map<String, Object>) ability.get("variable");
								
								for (String format : abilitiesLoreFormat) {
									String formattedLine = format
										.replace("{name}", name)
										.replace("{skill}", skill.replace("_", " "))
										.replace("{activator}", activator.replace("_", " "))
										.replace("{cooldown}", String.valueOf(cooldown))
										.replace("{power}", String.valueOf(power));
										if (variable != null) {
											Pattern pattern = Pattern.compile("\\{var\\.(.+?)\\}");
											Matcher matcher = pattern.matcher(formattedLine);
											StringBuffer sb = new StringBuffer();
											while (matcher.find()) {
												String varName = matcher.group(1);
												String varValue = variable.containsKey(varName) ? variable.get(varName).toString() : "null";
												matcher.appendReplacement(sb, varValue);
											}
											matcher.appendTail(sb);
											formattedLine = sb.toString();
										}
									lores.add(Legacy.serializer("<reset>" + formattedLine));
								}
							}
						}
					}
				}
				meta.setLore(lores);
				
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
				if (attributes != null && meta != null) {
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
				} else {
					plugin.getLogger().warning("Attributes list is null or ItemMeta is null.");
				}
				
				// HideFlags
				if (hide_flags != null) {
					for (String flag : hide_flags) {
						try {
							ItemFlag itemFlag = ItemFlag.valueOf(flag.toUpperCase());
							meta.addItemFlags(itemFlag);
						} catch (IllegalArgumentException e) {
							plugin.getLogger().warning("HideFlag [" + flag + "] is invalid.");
						}
					}
				}
				item.setItemMeta(meta);
			}
			
			NBTItem nbtItem = new NBTItem(item);
			NBTCompound nbt = nbtItem.getOrCreateCompound("ItemCaster");
			
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
				NBTCompoundList abilitiesList = nbt.getCompoundList("Abilities");
				for (Map<String, Object> ability : abilities) {
					if (!ability.containsKey("skill") || !ability.containsKey("activator")) {
						plugin.getLogger().warning("Ability missing required keys: " + ability);
						continue;
					}
					NBTCompound skillCompound = abilitiesList.addCompound();
					skillCompound.setString("skill", (String)ability.get("skill"));
					skillCompound.setString("activator", (String)ability.get("activator"));
					if (ability.containsKey("cooldown")) skillCompound.setInteger("cooldown", (Integer)ability.get("cooldown"));
					if (ability.containsKey("power")) skillCompound.setFloat("power", (Float)ability.get("power"));
					if (ability.containsKey("sneak")) skillCompound.setBoolean("sneak", (Boolean)ability.get("sneak"));
					if (ability.containsKey("show_cooldown")) skillCompound.setBoolean("show_cooldown", (Boolean)ability.get("show_cooldown"));
					if (ability.containsKey("variable")) {
						Map<String, Object> variable = (Map<String, Object>)ability.get("variable");
						NBTCompound variableCompound = skillCompound.getOrCreateCompound("variable");
						for (Map.Entry<String, Object> entry : variable.entrySet()) {
							String key = entry.getKey();
							Object value = entry.getValue();
							if (value instanceof String) {
								variableCompound.setString(key, (String)value);
							} else if (value instanceof Float) {
								variableCompound.setFloat(key, (Float)value);
							} else if (value instanceof Integer) {
								variableCompound.setInteger(key, (Integer)value);
							} else {
								variableCompound.setFloat(key, (Float)value);
							}
						}
					}
				}
			}
			return nbtItem.getItem();
		} catch (IllegalArgumentException e) {
			plugin.getLogger().warning("Material " + type + " is invalid!");
			return null;
		}
	}
	
	// Getter
	public ItemStack getItemStack() {
		return ITEM;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDisplayName() {
		return display_name;
	}
	
	public String getNbt() {
		return nbt;
	}
	
	public boolean getUnbreakable() {
		return unbreakable;
	}
	
	public int getModelData
	
	// Haster
	
	// Setter
}
*/