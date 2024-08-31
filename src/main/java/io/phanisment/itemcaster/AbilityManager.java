package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

public class AbilityManager {
	public String getNbt(ItemStack item) {
		NBTItem nbtItem = new NBTItem(item);
		NBTCompound abilities = nbtItem.getCompound("Abilities");
		if(abilities != null) {
			NBTCompoundList listAbility = abilities.getCompoundList("Abilities");
			for(ReadWriteNBT ability : listAbility) {
				String skill = ability.getString("skill");
				String event = ability.getString("event");
				int timer = ability.getInteger("timer");
				String toString = "{Skill" + skill + "Event:" + event + "Timer:" + timer;
				return toString;
			}
		} else {
			return null;
		}
	}
}