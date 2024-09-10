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
	private Player player;
	private String event;

	public SkillManager(Player player, String event) {
		this.player = player;
		this.event = event;
	}

	public void runSkill() {
		ItemStack item = player.getInventory().getItemInMainHand();
		NBTItem nbtItem = new NBTItem(item);
		NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");
		
		if (abilities != null) {
			for (ReadWriteNBT ability : abilities) {
				String skill = ability.getString("skill");
				String type = ability.getString("type");
				
				if (skill != null && type != null) {
					this.activeSkill(player, skill, type, event);
				}
			}
		}
	}

	public void activeSkill(Player player, String skill, String type, String event) {
		if (type.equals(event)) {
			MythicMobs.runSkill(skill, player);
		}
	}
}
