package io.phanisment.itemcaster.config.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.ConfigurationSection;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.config.item.CasterItem;
import io.phanisment.itemcaster.util.Message;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.gui.AbilityMenu.AbilityType;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ItemAbilityInterface implements ItemCasterItems {
	public ItemStack item;
	public AbilityType type;
	private String id;
	
	public String icon;
	public String display_name;
	public int model_data;
	public List<String> lore;
	
	public ItemAbilityInterface(Map<String, Object> config) {
		this.type = AbilityType.valueOf(AbilityType.class, ((String)config.get("type")).toUpperCase());
		try {
			this.icon = (String)config.getOrDefault("icon", "STONE");
			this.display_name = (String)config.getOrDefault("display_name", "");
			this.model_data = (Integer)config.getOrDefault("model_data", 0);
			this.lore = (List<String>)config.getOrDefault("lore", new ArrayList<>());
		} catch (Exception e) {
			getPl().getLogger().warning("Error creating edit interface [" + type + "]: " + e.getMessage());
		}
	}
	
	private ItemCaster getPl() {
		return ItemCaster.getInst();
	}
	
	public ItemAbilityInterface load() {
		try {
			this.item = CasterItem.itemType(icon);
			ItemMeta meta = item.getItemMeta();
			
			// Display Name
			if (!display_name.isEmpty()) meta.setDisplayName(Legacy.serializer("<white>" + display_name));
			
			// Model Data
			if (model_data >= 1) meta.setCustomModelData(model_data);
			
			// Lore
			if (lore != null) meta.setLore(Legacy.serializer(lore));
			item.setItemMeta(meta);
		} catch (IllegalArgumentException e) {
			getPl().getLogger().warning("Material " + type + " is invalid!");
		}
		return this;
	}
	
	@Override
	public ItemStack getItemStack() {
		return item;
	}
	
	public ItemAbilityInterface setDisplayName(String name) {
		this.display_name = name;
		return this;
	}
	
	public ItemAbilityInterface setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}
}