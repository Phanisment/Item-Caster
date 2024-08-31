package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class AbilityManager {
	public AbilityManager(ItemStack item) {
		NBTItem nbtItem = new NBTItem(item);
		if(nbtItem.hasKey("Skill")) {
			String skill = nbtItem.getString("Skill");
		}
		
		if(nbtItem.hasKey("Event")) {
			String event = nbtItem.getString("Event");
		}
	}
	
	public String getSkill() {
		return skill;
	}
	
	public String getEvent() {
		return event;
	}
}
