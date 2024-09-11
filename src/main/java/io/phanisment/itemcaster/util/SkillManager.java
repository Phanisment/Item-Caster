package io.phanisment.itemcaster.util;

import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.MythicMobs;

import java.util.HashMap;
import java.util.Map;

public class SkillManager {
	private Map<Player, Map<String, Integer>> skillTimers = new HashMap<>();
	
	public SkillManager runSkill(Player player, String event) {
		ItemStack item = player.getInventory().getItemInMainHand();
		NBTItem nbtItem = new NBTItem(item);
		NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");
		if (abilities != null) {
			for (ReadWriteNBT ability : abilities) {
				String skill = ability.getString("skill");
				String action = ability.getString("action");
				String timer = ability.getInteger("timer");
				
				skillTimers.putIfAbsent(player, new HashMap<>());
				int cooldown = skillTimers.get(player).getOrDefault(skill, 0);
				
				if (event == action && skill != null && action != null) {
					MythicMobs.runSkill(this.SKILL, player);
				} else if (event == "timer" && action == null && action != null) 
					cooldown++;
					if (cooldown >= timer) {
						MythicMobs.runSkill(skill, player);
						skillTimers.remove(player);
					}
					skillTimers.get(player).put(skill, cooldown);
				}
			}
		}
	}
}