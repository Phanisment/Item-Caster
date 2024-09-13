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

	public void runSkill(Player player, String event) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!isValidItem(item)) {
			return;
		}

		NBTItem nbtItem = new NBTItem(item);
		NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");

		if (abilities != null) {
			for (ReadWriteNBT ability : abilities) {
				String skill = ability.getString("skill");
				String action = ability.getString("action");
				int timer = ability.getInteger("timer");

				if (isValidSkillEvent(event, skill, action)) {
					MythicMobs.runSkill(skill, player);
				} else if (isTimerEvent(event, skill, action)) {
					processSkillTimer(player, skill, timer);
				}
			}
		}
	}

	private boolean isValidItem(ItemStack item) {
		return item != null && item.getType() != Material.AIR;
	}

	private boolean isValidSkillEvent(String event, String skill, String action) {
		return event.equals(action) && skill != null && action != null && !action.trim().isEmpty();
	}

	private boolean isTimerEvent(String event, String skill, String action) {
		return event.equals("timer") && skill != null && (action == null || action.trim().isEmpty());
	}

	private void processSkillTimer(Player player, String skill, int timer) {
		Optional<Integer> optionalTimer = Optional.ofNullable(timer);
		optionalTimer.ifPresent(data -> {
			skillTimers.putIfAbsent(player, new HashMap<>());
			int cooldown = skillTimers.get(player).getOrDefault(skill, 0);

			cooldown++;
			if (cooldown >= data) {
				MythicMobs.runSkill(skill, player);
				skillTimers.remove(player);
			} else {
				skillTimers.get(player).put(skill, cooldown);
			}
		});
	}
}
