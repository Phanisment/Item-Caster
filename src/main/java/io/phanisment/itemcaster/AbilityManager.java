package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTCompound;

public class AbilityManager {
	public AbilityManager(ItemStack item) {
		if(item != null) {
			NBTItem nbtItem = item.getCompound();
			return item;
		}
	}
	
	public String getSkill() {
		return item.getString("Skill");
	}
	
	public String getEvent() {
		return item.getString("Event");
	}
}
