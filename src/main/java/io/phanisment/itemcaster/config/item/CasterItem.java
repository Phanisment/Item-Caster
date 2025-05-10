package io.phanisment.itemcaster.config.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.potion.PotionType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

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
import io.phanisment.itemcaster.config.TrimsData;
import io.phanisment.itemcaster.script.FormatScript;
import io.phanisment.itemcaster.script.FormatScript.ScriptData;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CasterItem implements Cloneable, ItemCasterItems {
	private Pack pack;
	private ConfigurationSection config;
	private final String[] item_id;
	public ItemStack item;
	//private ItemStack item_menu;
	
	public String type;
	public String nbt_string;
	public String display_name;
	public int model_data;
	public boolean unbreakable;
	public boolean hide_all_flags;
	public String lore_format;
	public String color;
	public int damage;
	public int repair_cost;
	public String axolotl_variant;
	public String trim_material;
	public String trim_pattern;
	public String potion_type;
	public String potion_color;
	public String potion_effect;
	public float potion_duration;
	public List<Map<String, Object>> potion_effects;
	
	public List<String> lore;
	public List<String> enchants;
	public List<String> hide_flags;
	public List<String> attributes;
	public List<String> crossbow_projectiles;
	public List<String> bundle_items;
	public List<Map<String, Object>> suspicious_stew_effect;
	public List<Map<String, Object>> abilities;
	
	private ItemCaster getPl() {
		return ItemCaster.getInst();
	}
	
	public CasterItem(Pack pack, ConfigurationSection config, String[] item_id) {
		this.item_id = item_id;
		if (config != null) this.config = config;
		this.pack = pack;
		try {
			this.type = config.getString("type", "STONE");
			this.nbt_string = config.getString("nbt", "{}");
			this.display_name = config.getString("display_name", "");
			this.model_data = config.getInt("model_data", 0);
			this.unbreakable = config.getBoolean("unbreakable", false);
			this.hide_all_flags = config.getBoolean("hide_all_flags", false);
			this.lore_format = config.getString("lore_format", "");
			this.color = config.getString("color");
			this.damage = config.getInt("damage", 0);
			this.repair_cost = config.getInt("repair_cost", 0);
			this.axolotl_variant = config.getString("axolotl_variant", "");
			this.trim_material = config.getString("trim.material", "");
			this.trim_pattern = config.getString("trim.pattern", "");
			this.potion_type = config.getString("potion.type", "");
			this.potion_effect = config.getString("potion.effect", "");
			this.potion_color = config.getString("potion.color", "");
			this.potion_effects = (List<Map<String, Object>>)config.getList("potion.effects", new ArrayList<>());
			this.lore = (List<String>)config.getList("lore", new ArrayList<>());
			this.enchants = (List<String>)config.getList("enchants", new ArrayList<>());
			this.hide_flags = (List<String>)config.getList("hide_flags", new ArrayList<>());
			this.attributes = (List<String>)config.getList("attributes", new ArrayList<>());
			this.crossbow_projectiles = (List<String>)config.getList("crossbow_projectiles", new ArrayList<>());
			this.bundle_items = (List<String>)config.getList("bundle_items", new ArrayList<>());
			this.suspicious_stew_effect = (List<Map<String, Object>>)config.getList("suspicious_stew_effect", new ArrayList<>());
			this.abilities = (List<Map<String, Object>>)config.get("abilities");
		} catch (Exception e) {
			getPl().getLogger().warning("Error creating item [" + item_id[0] + "] in file [" + item_id[1] + "]: " + e.getMessage());
		}
	}
	
	public CasterItem load() {
		try {
			this.item = itemType(type);
			ItemMeta meta = item.getItemMeta();
			
			// Display Name
			if (!display_name.isEmpty()) meta.setDisplayName(Legacy.serializer("<white>" + display_name));
			
			// Model Data
			if (model_data >= 1) meta.setCustomModelData(model_data);
			
			// Unbreakable
			meta.setUnbreakable(unbreakable);
			
			// Leather Color
			if (meta instanceof LeatherArmorMeta && !color.isEmpty()) {
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
			
			// Crossbow Arrow
			if (meta instanceof CrossbowMeta) {
				List<ItemStack> item_list = new ArrayList<>();
				if (crossbow_projectiles != null) crossbow_projectiles.forEach((type) -> {
					item_list.add(itemType(type));
				});
				((CrossbowMeta)meta).setChargedProjectiles(item_list);
			}
			
			// Item Repair Cost
			if (meta instanceof Repairable) {
				if (repair_cost >= 1) ((Repairable)meta).setRepairCost(repair_cost);
			}
			
			// Budle Item
			if (meta instanceof BundleMeta) {
				List<ItemStack> item_list = new ArrayList<>();
				if (bundle_items != null) bundle_items.forEach((type) -> {
					item_list.add(itemType(type));
				});
				((BundleMeta)meta).setItems(item_list);
			}
			
			// Axolotl Variant
			if (meta instanceof AxolotlBucketMeta) {
				if (!axolotl_variant.isEmpty()) ((AxolotlBucketMeta)meta).setVariant(Axolotl.Variant.valueOf(axolotl_variant));
			}
			
			// Trims
			if (meta instanceof ArmorMeta) {
				TrimsData trims = new TrimsData();
				if (!trim_material.isEmpty() && !trim_pattern.isEmpty()) ((ArmorMeta)meta).setTrim(new ArmorTrim(trims.getMaterial(trim_material), trims.getPattern(trim_pattern)));
			}
			
			// Suspicion Stew Effect
			if (meta instanceof SuspiciousStewMeta) {
				SuspiciousStewMeta stew_meta = (SuspiciousStewMeta)meta;
				if (suspicious_stew_effect != null) {
					for (Map<String, Object> effect : suspicious_stew_effect) {
						try {
							String potionType = (String)effect.get("potion");
							int duration = (Integer)effect.get("duration");
							int level = (Integer)effect.getOrDefault("level", 1);
							boolean hide_ambient = (Boolean)effect.getOrDefault("hide_ambient", false);
							boolean hide_particle = (Boolean)effect.getOrDefault("hide_particle", false);
							boolean hide_icon = (Boolean)effect.getOrDefault("hide_icon", false);
							PotionEffectType effectType = PotionEffectType.getByName(potionType.toUpperCase());
							if (effectType != null) {
								PotionEffect potionEffect = new PotionEffect(effectType, duration, level - 1, hide_ambient, !hide_particle, !hide_icon);
								stew_meta.addCustomEffect(potionEffect, true);
							} else {
								getPl().getLogger().warning("Invalid potion effect type: " + potionType);
							}
						} catch (Exception e) {
							getPl().getLogger().warning("Error processing suspicious stew effect: " + e.getMessage());
						}
					}
				}
			}
			
			// Potion
			if (meta instanceof PotionMeta) {
				PotionMeta potion_meta = (PotionMeta) meta;
				try {
					if (!potion_type.isEmpty()) {
						PotionType potionType = PotionType.valueOf(potion_type.toUpperCase());
						potion_meta.setBasePotionData(new PotionData(potionType));
					}
					if (!potion_effect.isEmpty()) potion_meta.setMainEffect(PotionEffectType.getByName(potion_effect.toUpperCase()));
					if (potion_effects != null) {
						for (Map<String, Object> effect : potion_effects) {
							String potionType = (String) effect.get("potion");
							int duration = (Integer)effect.get("duration");
							int level = (Integer) effect.getOrDefault("level", 1);
							boolean hide_ambient = (Boolean) effect.getOrDefault("hide_ambient", false);
							boolean hide_particle = (Boolean) effect.getOrDefault("hide_particle", false);
							boolean hide_icon = (Boolean) effect.getOrDefault("hide_icon", false);
							PotionEffectType effectType = PotionEffectType.getByName(potionType.toUpperCase());
							if (effectType != null) {
								PotionEffect potionEffect = new PotionEffect(effectType, duration, level - 1, hide_ambient, !hide_particle, !hide_icon);
								potion_meta.addCustomEffect(potionEffect, true);
							} else {
								getPl().getLogger().warning("Invalid potion effect type: " + potionType);
							}
						}
					}
					if (!potion_color.isEmpty()) {
						String[] parts = potion_color.split(",");
						if (parts.length == 3) {
							try {
								int r = Integer.parseInt(parts[0].trim());
								int g = Integer.parseInt(parts[1].trim());
								int b = Integer.parseInt(parts[2].trim());
								potion_meta.setColor(Color.fromRGB(r, g, b));
							} catch (NumberFormatException e) {
								getPl().getLogger().warning("The Color is must be Number: " + e);
							}
						} else {
							getPl().getLogger().warning("Color format must be like this: R, G, B");
						}
					}
				} catch (Exception e) {
					getPl().getLogger().warning("Error processing potion_meta: " + e.getMessage());
				}
			}
			
			// Lore
			List<String> lores = new ArrayList<>();
			if (lore_format.isEmpty()) {
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
								Integer power = (Integer)ability.getOrDefault("power", 0);
								Map<String, Object> variable = (Map<String, Object>) ability.get("variable");
								
								for (String format : abilitiesLoreFormat) {
									String formattedLine = format
										.replace("{name}", name)
										.replace("{skill}", skill.replace("_", " "))
										.replace("{activator}", activator.replace("_", " "))
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
			} else {
				List<String> fmt = FormatScript.run(lore_format,
					new ScriptData("id", getId()),
					new ScriptData("nbt", nbt_string),
					new ScriptData("model_data", model_data),
					new ScriptData("color", color),
					new ScriptData("damage", damage),
					new ScriptData("repair_cost", repair_cost),
					new ScriptData("axolotl_variant", axolotl_variant),
					new ScriptData("trim_pattern", trim_pattern),
					new ScriptData("trim_material", trim_material),
					new ScriptData("potion_type", potion_type),
					new ScriptData("potion_color", potion_color),
					new ScriptData("potion_effect", potion_effect),
					new ScriptData("potion_duration", potion_duration),
					new ScriptData("potion_effects", potion_effects),
					new ScriptData("lore", lore),
					new ScriptData("enchantments", enchants),
					new ScriptData("hide_flags", hide_flags),
					new ScriptData("attributes", attributes),
					new ScriptData("crossbow_projectiles", crossbow_projectiles),
					new ScriptData("bundle_items", bundle_items),
					new ScriptData("suspicious_stew_effect", suspicious_stew_effect),
					new ScriptData("potion_effects", potion_effects),
					new ScriptData("abilities", abilities)
				);
				
				fmt.forEach(line -> {
					lores.add(Legacy.serializer("<white>" + line));
				});
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
								if (meta instanceof EnchantmentStorageMeta) {
									((EnchantmentStorageMeta)meta).addStoredEnchant(enchantment, level, true);
								} else {
									meta.addEnchant(enchantment, level, true);
								}
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
			if (!hide_all_flags) {
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
			} else {
				meta.addItemFlags(ItemFlag.values());
			}
			item.setItemMeta(meta);
			
			NBTItem nbtItem = new NBTItem(item);
			NBTCompound nbt = nbtItem.getOrCreateCompound("ItemCaster");
			nbt.setString("id", item_id[0] + ":" + item_id[1]);
			
			if (!nbt_string.isEmpty()) {
				try {
					ReadWriteNBT nbtData = NBT.parseNBT(nbt_string);
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
					if (ability.containsKey("power")) skillCompound.setFloat("power", (Float)ability.get("power"));
					if (ability.containsKey("sneak")) skillCompound.setBoolean("sneak", (Boolean)ability.get("sneak"));
					if (ability.containsKey("cancel_event")) skillCompound.setBoolean("cancel_event", (Boolean)ability.get("cancel_event"));
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
	
	public static ItemStack itemType(String type) {
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
						ItemCaster.getInst().getLogger().warning("MythicMobs item not found: " + name);
					}
					break;
				case "itemsadder":
					if (ItemCaster.getInst().hasItemsAdder()) {
						CustomStack stack = CustomStack.getInstance(name + ":" + parts[2]);
						if (stack != null) {
							item = stack.getItemStack();
						} else {
							ItemCaster.getInst().getLogger().warning("ItemsAdder item not found: " + name);
						}
					}
					break;
				case "itemcaster":
					CasterItem ci = ItemCaster.getInst().getItemConfig().getItem(name, parts[2]);
					if (ci != null) {
						item = ci.getItemStack();
					} else {
						ItemCaster.getInst().getLogger().warning("The item is not registered: " + name);
					}
					break;
				default:
					Material material = Material.valueOf(type.toUpperCase());
					ItemCaster.getInst().getLogger().warning("Unknown external type: " + plugin);
					item = new ItemStack(material);
					break;
				}
		} else {
			Material material = Material.valueOf(type.toUpperCase());
			item = new ItemStack(material);
		}
		return item;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public CasterItem save() {
		CasterItem ic = this.load();
		getPl().getItemConfig().getItemList().put(getId(), ic);
		return ic;
	}
	
	public void remove() {
		getPl().getItemConfig().getItemList().remove(getId());
		this.pack.config.set("items." + item_id[1], null);
		saveConfig();
	}
	
	public boolean equals(ItemStack item) {
		NBTCompound nbt = new NBTItem(item).getCompound("ItemCaster");
		if (nbt != null) return false;
		if (nbt.getString("id").isEmpty()) return false;
		return this.getId().equalsIgnoreCase(nbt.getString("id")) ;
	}
	
	public boolean equals(CasterItem item) {
		return item.getId().equals(this.getId());
	}
	
	public ConfigurationSection getConfig() {
		return config;
	}
	
	private void saveConfig() {
		try {
			pack.config.save(pack.file);
		} catch (Exception e) {
			getPl().getLogger().warning("Failed to save config for item [" + getId() + "]: " + e.getMessage());
		}
	}
	
	public String getId() {
		return item_id[0] + ":" + item_id[1];
	}
	
	@Override
	public ItemStack getItemStack() {
		return item;
	}
	
	public ItemMeta getItemMeta() {
		return item.getItemMeta();
	}
	
	// Setter
	public CasterItem setType(String type) {
		this.config.set("type", type);
		this.type = type;
		saveConfig();
		return this;
	}
	
	public CasterItem setNbt(String nbt) {
		this.config.set("nbt", nbt);
		this.nbt_string = nbt;
		saveConfig();
		return this;
	}
	
	public CasterItem setDisplayName(String display_name) {
		this.config.set("display_name", display_name);
		this.display_name = display_name;
		saveConfig();
		return this;
	}
	
	public CasterItem setModelData(int model_data) {
		this.config.set("model_data", model_data);
		this.model_data = model_data;
		saveConfig();
		return this;
	}
	
	public CasterItem setUnbreakable(boolean unbreakable) {
		this.config.set("unbreakable", unbreakable);
		this.unbreakable = unbreakable;
		saveConfig();
		return this;
	}
	
	public CasterItem setColor(String color) {
		this.config.set("color", color);
		this.color = color;
		saveConfig();
		return this;
	}
	
	public CasterItem setDamage(int damage) {
		this.config.set("damage", damage);
		this.damage = damage;
		saveConfig();
		return this;
	}
	
	public CasterItem setRepairCost(int cost) {
		this.config.set("repair_cost", cost);
		this.repair_cost = cost;
		saveConfig();
		return this;
	}
	
	public CasterItem setAxolotlVariant(String variant) {
		this.config.set("axolotl_variant", variant);
		this.axolotl_variant = variant;
		saveConfig();
		return this;
	}
	
	public CasterItem setLore(List<String> lore) {
		this.config.set("lore", lore);
		this.lore = lore;
		saveConfig();
		return this;
	}
	
	public CasterItem setEnchants(List<String> enchants) {
		this.config.set("enchants", enchants);
		this.enchants = enchants;
		saveConfig();
		return this;
	}
	
	public CasterItem setFlags(List<String> flags) {
		this.config.set("hide_flags", flags);
		this.hide_flags = flags;
		saveConfig();
		return this;
	}
	
	public CasterItem setAttributes(List<String> attributes) {
		this.config.set("attributes", attributes);
		this.attributes = attributes;
		saveConfig();
		return this;
	}
	
	public CasterItem setCrossbowProjectiles(List<String> items) {
		this.config.set("crossbow_projectiles", items);
		this.crossbow_projectiles = items;
		saveConfig();
		return this;
	}
	
	public CasterItem setBundleItems(List<String> items) {
		this.config.set("bundle_items", items);
		this.bundle_items = items;
		saveConfig();
		return this;
	}
	
	public CasterItem setAbilities(List<Map<String, Object>> abilities) {
		this.config.set("abilities", abilities);
		this.abilities = abilities;
		saveConfig();
		return this;
	}
	
	// adder
	public CasterItem addLore(String line) {
		this.lore.add(line);
		this.config.set("lore", lore);
		saveConfig();
		return this;
	}
	
	public CasterItem addEnchants(String line) {
		this.lore.add(line);
		this.config.set("enchants", lore);
		saveConfig();
		return this;
	}
	
	public CasterItem addFlags(String line) {
		this.lore.add(line);
		this.config.set("hide_flags", lore);
		saveConfig();
		return this;
	}
	
	public CasterItem addAttribute(String line) {
		this.lore.add(line);
		this.config.set("attributes", lore);
		saveConfig();
		return this;
	}
	
	public CasterItem addCrossbowProjectiles(String line) {
		this.lore.add(line);
		this.config.set("crossbow_projectiles", lore);
		saveConfig();
		return this;
	}
	
	public CasterItem addBundleItems(String line) {
		this.lore.add(line);
		this.config.set("bundle_items", lore);
		saveConfig();
		return this;
	}
	
	// resetter
	public CasterItem resetNbt() {
		this.config.set("nbt", null);
		this.nbt_string = "{}";
		saveConfig();
		return this;
	}
	
	public CasterItem resetDisplayName() {
		this.config.set("display_name", null);
		this.display_name = "";
		saveConfig();
		return this;
	}
	
	public CasterItem resetModelData() {
		this.config.set("model_data", null);
		this.model_data = 0;
		saveConfig();
		return this;
	}
	
	public CasterItem resetUnbreakable() {
		this.config.set("unbreakable", null);
		this.unbreakable = false;
		saveConfig();
		return this;
	}
	
	public CasterItem resetColor() {
		this.config.set("color", null);
		this.color = null;
		saveConfig();
		return this;
	}
	
	public CasterItem resetDamage() {
		this.config.set("damage", null);
		this.damage = 0;
		saveConfig();
		return this;
	}
	
	public CasterItem resetRepairCost() {
		this.config.set("repair_cost", 0);
		this.repair_cost = 0;
		saveConfig();
		return this;
	}
	
	public CasterItem resetAxolotlVariant() {
		this.config.set("axolotl_variant", null);
		this.axolotl_variant = "";
		saveConfig();
		return this;
	}
	
	public CasterItem resetLores() {
		this.config.set("lore", null);
		this.lore = new ArrayList<>();
		saveConfig();
		return this;
	}
	
	public CasterItem resetEnchants() {
		this.config.set("enchants", null);
		this.enchants = new ArrayList<>();
		saveConfig();
		return this;
	}
	
	public CasterItem resetFlags() {
		this.config.set("hide_flags", null);
		this.hide_flags = new ArrayList<>();
		saveConfig();
		return this;
	}
	
	public CasterItem resetAttributes() {
		this.config.set("attributes", null);
		this.attributes = new ArrayList<>();
		saveConfig();
		return this;
	}
	
	public CasterItem resetCrossbowProjectiles() {
		this.config.set("crossbow_projectiles", null);
		this.crossbow_projectiles = new ArrayList<>();
		saveConfig();
		return this;
	}
	
	public CasterItem resetBundleItems() {
		this.config.set("bundle_items", null);
		this.bundle_items = new ArrayList<>();
		saveConfig();
		return this;
	}
	
	public CasterItem resetAbilities() {
		this.config.set("abilities", null);
		this.abilities = new ArrayList<>();
		saveConfig();
		return this;
	}
	
	@Override
	public String toString() {
		return "CasterItem{id= " + this.getId() + "; type= " + this.type + "; model= " + this.model_data + "}";
	}
}