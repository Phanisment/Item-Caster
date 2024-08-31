package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTCompound;

public class AbilityManager {
	public AbilityManager(ItemStack item) {
		if(item != null) {
			NBTCompound nbtItem = item.getCompound();
			NBTCompound skill = nbtItem.getCompound("Skill");
			NBTCompound event = nbtItem.getCompound("Event");
			return item;
		}
	}
	
	public String getSkill() {
		return skill;
	}
	
	public String getEvent() {
		return event;
	}
}
