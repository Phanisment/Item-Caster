package io.phanisment.itemcaster.config.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;

import dev.lone.itemsadder.api.CustomStack;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.util.Message;

import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CasterItem {
	public String[] item_id;
	public ItemStack item;
	//private ItemStack item_menu;
	
	public String type;
	public String nbtString;
	public String display_name;
	public int model_data;
	public boolean unbreakable;
	public String color;
	public Integer damage;
	
	public List<String> lore;
	public List<String> enchants;
	public List<String> hide_flags;
	public List<String> attributes;
	
	public List<Map<String, Object>> abilities;
	
	private ItemCaster getPl() {
		return ItemCaster.getInst();
	}
	
	public CasterItem(CasterItem cloneItem) {
		if (cloneItem == null || cloneItem.item == null) {
			this.item = new ItemStack(Material.STONE);
			return;
		}
		this.item = cloneItem.getItemStack().clone();
	}
	
	public CasterItem(MemorySection config, String[] item_id) {
		this.item_id = item_id;
		try {
			this.type = config.getString("type", "STONE");
			this.nbtString = config.getString("nbt", "{}");
			this.display_name = config.getString("display_name");
			this.model_data = config.getInt("model_data");
			this.unbreakable = config.getBoolean("unbreakable", false);
			this.color = config.getString("color");
			this.damage = config.getInt("damage", 0);
			
			this.lore = (List<String>)config.getList("lore", new ArrayList<>());
			this.enchants = (List<String>)config.getList("enchants", new ArrayList<>());
			this.hide_flags = (List<String>) config.getList("hide_flags", new ArrayList<>());
			this.attributes = (List<String>) config.getList("attributes", new ArrayList<>());
			
			this.abilities = (List<Map<String, Object>>)config.get("abilities");
		} catch (Exception e) {
			getPl().getLogger().warning("Error creating item [" + item_id[0] + "] in file [" + item_id[1] + "]: " + e.getMessage());
		}
	}
	
	public CasterItem load() {
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
							this.item = BukkitAdapter.adapt(mi.generateItemStack(1));
						} else {
							getPl().getLogger().warning("MythicMobs item not found: " + name);
						}
						break;
					case "itemsadder":
						if (getPl().hasItemsAdder()) {
							CustomStack stack = CustomStack.getInstance(name + ":" + parts[2]);
							if (stack != null) {
								this.item = stack.getItemStack();
							} else {
								getPl().getLogger().warning("ItemsAdder item not found: " + name);
							}
						}
						break;
					default:
						Material material = Material.valueOf(type.toUpperCase());
						this.item = new ItemStack(material);
						getPl().getLogger().warning("Unknown external type: " + plugin);
						break;
				}
			} else {
				Material material = Material.valueOf(type.toUpperCase());
				this.item = new ItemStack(material);
			}
			
			ItemMeta meta = item.getItemMeta();
			
			// Display Name
			if (display_name != null) meta.setDisplayName(Legacy.serializer("<reset>" + display_name));
			
			// Model Data
			if (model_data >= 1) meta.setCustomModelData(model_data);
			
			// Unbreakable
			meta.setUnbreakable(unbreakable);
			
			// Leather  Color
			if (meta instanceof LeatherArmorMeta) {
				String[] parts = color.split(",");
				if (parts.length == 3) {
					try {
						int r = Integer.parseInt(parts[0].trim());
						int g = Integer.parseInt(parts[1].trim());
						int b = Integer.parseInt(parts[2].trim());
						((LeatherArmorMeta)meta).setColor(Color.fromRGB(r, g, b));
					} catch (NumberFormatException e) {
						getPl().getLogger().warning("The Color is must be Number: " + e);
					}
				} else {
					getPl().getLogger().warning("Color format must be like this: R, G, B");
				}
			}
			
			// Item Damage
			if (meta instanceof Damageable) {
				if (damage >= 1) ((Damageable)meta).setDamage(damage);
			}
			
			
			
			// Lore
			List<String> lores = new ArrayList<>();
			if (!lore.isEmpty()) {
				for (String line : lore) {
					lores.add(Legacy.serializer("<white>" + line));
				}
			}
				
			// Abilities Lore
			List<String> abilitiesLoreFormat = getPl().getConfig().getStringList("abilities.lore");
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
								lores.add(Legacy.serializer("<white>" + formattedLine));
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
								getPl().getLogger().warning("Enchant [" + enchantName + " ] is invalid for item" + type);
							}
						} catch (NumberFormatException e) {
							getPl().getLogger().warning("Level enchantment [" + enchant + "] is invalid!");
						}
					} else {
						getPl().getLogger().warning("Format enchantment [" + enchant + "] is wrong!");
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
							getPl().getLogger().warning("Invalid attribute entry: " + attributeEntry + " (" + e.getMessage() + ")");
						}
					} else {
						getPl().getLogger().warning("Invalid attribute format: " + attributeEntry);
					}
				}
			} else {
				getPl().getLogger().warning("Attributes list is null or ItemMeta is null.");
			}
			
			// HideFlags
			if (hide_flags != null) {
				for (String flag : hide_flags) {
					try {
						ItemFlag itemFlag = ItemFlag.valueOf(flag.toUpperCase());
						meta.addItemFlags(itemFlag);
					} catch (IllegalArgumentException e) {
						getPl().getLogger().warning("HideFlag [" + flag + "] is invalid.");
					}
				}
			}
			
			item.setItemMeta(meta);
			
			NBTItem nbtItem = new NBTItem(item);
			NBTCompound nbt = nbtItem.getOrCreateCompound("ItemCaster");
			
			if (!nbtString.isEmpty()) {
				try {
					ReadWriteNBT nbtData = NBT.parseNBT(nbtString);
					nbtItem.mergeCompound(nbtData);
				} catch (Exception e) {
					getPl().getLogger().warning("Format NBT invalid: " + e.getMessage());
				}
			}
			
			// Abilities
			if (abilities != null) {
				NBTCompoundList abilitiesList = nbt.getCompoundList("Abilities");
				for (Map<String, Object> ability : abilities) {
					if (!ability.containsKey("skill") || !ability.containsKey("activator")) {
						getPl().getLogger().warning("Ability missing required keys: " + ability);
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
			this.item = nbtItem.getItem();
		} catch (IllegalArgumentException e) {
			getPl().getLogger().warning("Material " + type + " is invalid!");
		}
		return this;
	}
	
	// Getter
	public ItemStack getItemStack() {
		return item;
	}
	
	public String getType() {
		return type;
	}
	
	public String getNbt() {
		return nbtString;
	}
	
	// Setter
}