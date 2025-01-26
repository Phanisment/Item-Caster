package io.phanisment.itemcaster.skills;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class SkillCooldown {
	private static Map<Player, Map<String, Integer>> data = new HashMap<>();
	private final Player player;

	public SkillCooldown(Player player) {
		this.player = player;
		data.putIfAbsent(player, new HashMap<>());
	}

	public void setCooldown(String skill, int duration) {
		Map<String, Integer> cd = data.get(player);
		cd.put(skill, duration);
	}

	public boolean hasCooldown(String skill) {
		Map<String, Integer> cd = data.get(player);
		return cd.containsKey(skill);
	}

	public int getCooldown(String skill) {
		Map<String, Integer> cd = data.get(player);
		return cd.getOrDefault(skill, 0);
	}

	public void runTick() {
		Map<String, Integer> cd = data.get(player);
		if (cd == null || cd.isEmpty()) return;
		List<String> expiredSkills = new ArrayList<>();
		cd.forEach((skill, duration) -> {
			if (duration >= 1) {
				cd.put(skill, duration - 1);
			} else {
				expiredSkills.add(skill);
			}
		});
		expiredSkills.forEach(cd::remove);
	}
}