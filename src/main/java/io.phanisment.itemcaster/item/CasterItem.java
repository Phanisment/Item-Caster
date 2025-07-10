package io.phanisment.itemcaster.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import io.lumine.mythic.api.config.MythicConfig;
import io.lumine.mythic.core.items.MythicItem;
import io.lumine.mythic.bukkit.events.MythicMobItemGenerateEvent;
import io.lumine.mythic.core.logging.MythicLogger;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;

import io.phanisment.itemcaster.ItemCaster;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class CasterItem {
	private final MythicItem mi;
	private final MythicConfig config;
	
	private final List<Map<String, Object>> abilities;
	
	public CasterItem(MythicItem mi) {
		this.mi = mi;
		this.config = mi.getConfig();
		
		this.abilities = (List<Map<String, Object>>)(Object)config.getMapList("Abilities");
	}
	
	public void applyData(MythicMobItemGenerateEvent e) {
		ItemStack item = e.getItemStack();
		e.setItemStack(this.genItem(item));
	}
	
	private ItemStack genItem(ItemStack item) {
		NBTItem nbt_item = new NBTItem(item);
		NBTCompound nbt = nbt_item.getOrCreateCompound("ItemCaster");
		
		if (abilities != null) parseAbilities(abilities, nbt);
		
		return nbt_item.getItem();
	}
	
	public void parseAbilities(List<Map<String, Object>> data, NBTCompound nbt) {
		NBTCompoundList abilitiesList = nbt.getCompoundList("abilities");
		for (Map<String, Object> ability : abilities) {
			
			if (!ability.containsKey("skill") || !ability.containsKey("activator")) {
				MythicLogger.errorItemConfig(mi, config, "Required attributes `skill` and `activator` in Abilities component!");
				continue;
			}
			
			NBTCompound skillCompound = abilitiesList.addCompound();
			skillCompound.setString("skill", (String)ability.get("skill"));
			skillCompound.setString("activator", (String)ability.get("activator"));
			
			if (ability.containsKey("power")) skillCompound.setFloat("power", (Float)ability.get("power"));
			if (ability.containsKey("signal")) skillCompound.setString("signal", (String)ability.get("signal"));
			if (ability.containsKey("interval")) skillCompound.setInteger("interval", (Integer)ability.get("interval"));
			
			if (ability.containsKey("variables")) {
				Map<String, Object> variables = (Map<String, Object>)ability.get("variables");
				NBTCompound variableCompound = skillCompound.getOrCreateCompound("variables");
				for (Map.Entry<String, Object> entry : variables.entrySet()) {
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
	
	public MythicItem getItem() {
		return this.mi;
	}
}
