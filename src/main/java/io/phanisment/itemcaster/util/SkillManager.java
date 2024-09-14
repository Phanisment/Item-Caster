package io.phanisment.itemcaster.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;

import io.phanisment.itemcaster.MythicMobs;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

public class SkillManager {

	private static final Map<Player, Map<String, Integer>> skillTimers = new HashMap<>();
	private Player player;
	private String event;
	private NBTCompoundList abilities;

	public SkillManager runSkill(Player player, String event) {
		this.player = player;
		this.event = event;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!isValidItem(item)) {
			NBTItem nbtItem = new NBTItem(item);
			this.abilities = nbtItem.getCompoundList("Abilities");
		}
		return this;
	}

	public void activeSkill() {
		if (abilities == null) return;

		for (ReadWriteNBT ability : abilities) {
			String skill = ability.getString("skill");
			String action = ability.getString("action");

			if (isSkillEvent(event, skill, action)) {
				MythicMobs.runSkill(skill, player);
			}
		}
	}

	public void passiveSkill() {
		if (abilities == null) return;

		for (ReadWriteNBT ability : abilities) {
			String skill = ability.getString("skill");
			String action = ability.getString("action");
			int timer = ability.getInteger("timer");

			if (isTimerEvent(event, skill, action)) {
				processSkillTimer(player, skill, timer);
			}
		}
	}

	private void processSkillTimer(Player player, String skill, int timer) {
		skillTimers.putIfAbsent(player, new HashMap<>());
		Map<String, Integer> playerSkills = skillTimers.get(player);
		int cooldown = playerSkills.getOrDefault(skill, timer);
		if (cooldown <= 0) {
			MythicMobs.runSkill(skill, player);
			playerSkills.put(skill, timer);
		} else {
			playerSkills.put(skill, cooldown - 1);
		}
	}

	private boolean isValidItem(ItemStack item) {
		return item != null && item.getType() != Material.AIR;
	}

	private boolean isSkillEvent(String event, String skill, String action) {
		return event.equalsIgnoreCase(action) && skill != null && !action.trim().isEmpty();
	}

	private boolean isTimerEvent(String event, String skill, String action) {
		return event.equals("timer") && skill != null && (action == null || action.trim().isEmpty());
	}
}
