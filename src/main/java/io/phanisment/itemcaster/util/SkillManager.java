package io.phanisment.itemcaster.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

import io.phanisment.itemcaster.MythicMobs;

public class SkillManager {
	public void runSkill(Player player, String event) {
		ItemStack item = player.getInventory().getItemInMainHand();
		NBTItem nbtItem = new NBTItem(item);
		NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");
		if(abilities != null) {
			for(ReadWriteNBT ability : abilities) {
				String skill = ability.getString("skill");
				String type = ability.getString("type");
				int timer = ability.getInteger("timer");
				
				if (timer != NaN) {
					if (id != null && type != null) {
						this.activeSkill(player, skill, type, event);
					}
				//} else {
					//Passive.runSkill()
				}
			}
		}
	}
	
	public void activeSkill(Player player, String skill, String type, String event) {
		Boolean isSame = (type == event) ? true : false;

		if(isSame){
			MythicMobs.runSkill(skill, player);
		}
	}
}