package io.phanisment.itemcaster.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

import io.phanisment.itemcaster.MythicMobs;

import java.util.Optional;

public class SkillManager {
	
	public SkillManager(Player player, String event) {
		ItemStack item = player.getInventory().getItemInMainHand();
		NBTItem nbtItem = new NBTItem(item);
		NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");
		if(abilities != null) {
			for(ReadWriteNBT ability : abilities) {
				String skill = ability.getString("skill");
				String type = ability.getString("type");
			}
		}
	}
	
	public void runSkill(Player player, String event) {
		if (skill != null && type != null) {
			this.activeSkill(player, skill, type, event);
		}
		// Passive Skill.
	}
	
	public void activeSkill(Player player, String skill, String type, String event) {
		Boolean isSame = (type == event) ? true : false;

		if(isSame){
			MythicMobs.runSkill(skill, player);
		}
	}
}