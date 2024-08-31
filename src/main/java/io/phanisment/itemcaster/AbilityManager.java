package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;

public class AbilityManager {
	public String getNbt(ItemStack item) {
		NBTItem nbtItem = new NBTItem(item);
		NBTCompound abilities = nbtItem.getCompound("Abilities");
		if(abilities != null) {
			NBTList<NBTCompound> listAbility = abilities.getCompoundList("Abilities");
			for(NBTCompound ability : listAbility) {
				String skill = ability.getString("skill");
				String event = ability.getString("event");
				int timer = ability.getInt("timer");
			}
		} else {
			return null;
		}
	}
}