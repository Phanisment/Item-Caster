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
	private final String skill;
	private final String action;
	private final int timer;
	
	private static Map<Player, Map<String, Integer>> skillTimers = new HashMap<>();

	public void runSkill(Player player, String event) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item == null || item.getType().isAir()) {
			return;
		}
		
		NBTItem nbtItem = new NBTItem(item);
		NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");
		if (abilities != null) {
			for (ReadWriteNBT ability : abilities) {
				this.skill = ability.getString("skill");
				this.action = ability.getString("action");
				this.timer = ability.getInteger("timer");
			}
			
			try {
				if (event.equals(action) && skill != null && timer == 0 && action != null || action.trim().isEmpty()) {
					MythicMobs.runSkill(skill, player);
				} else if (event.equals("timer") && skill != null && timer != 0 && action == null || action.trim().isEmpty()) {
					skillTimers.putIfAbsent(player, new HashMap<>());
					int cooldown = skillTimers.get(player).getOrDefault(skill, 0);
					
					cooldown++;
					if (cooldown >= timer) {
						MythicMobs.runSkill(skill, player);
						skillTimers.remove(player);
					}
					skillTimers.get(player).put(skill, cooldown);
				}
			} catch (Exception e) {
				
			}
		}
	}
}