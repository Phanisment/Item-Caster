package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import io.phanisment.nbtapi.iface.ReadWriteNBT;
import io.phanisment.nbtapi.NBTCompoundList;
import io.phanisment.nbtapi.NBTCompound;
import io.phanisment.nbtapi.NBTItem;

public class AbilityManager {
	public AbilityManager(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		NBTItem nbtItem = new NBTItem(item);
		NBTCompound abilities = nbtItem.getCompound("Abilities");
		if(abilities != null) {
			NBTCompoundList listAbility = abilities.getCompoundList("Abilities");
			for(ReadWriteNBT ability : listAbility) {
				String skill = ability.getString("skill");
				String event = ability.getString("event");
				int timer = ability.getInteger("timer");
				player.sendMessage("Skill: " + skill + ", event: " + event + ", timer: " + timer);
			}
		}
	}
}