package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

public class AbilityManager {
	public Object AbilityManager(Player player) {
		ItemStack item = player.getInventory().getItemInHand();
		NBTItem nbtItem = new NBTItem(item);
		NBTCompound abilities = nbtItem.getCompound("Abilities");
		if(abilities != null) {
			NBTCompoundList listAbility = abilities.getCompoundList("Abilities");
			for(ReadWriteNBT ability : listAbility) {
				String skill = ability.getString("skill");
				String event = ability.getString("event");
				int timer = ability.getInteger("timer");
			}
			return abilities;
		}
		return null
	}
	
	public class Abiltity {
		private this.skill = skill;
		private this.event = event;
		private this.timer = timer;
		
		public String getSkill() {
			return skill;
		}
	}
}