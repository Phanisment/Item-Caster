package phanisment.itemcaster.skills;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SkillCooldown {
	private final Map<Player, Map<String, Integer>> data = new HashMap<>();
	private final Player player;

	public SkillCooldown(Player player) {
		this.player = player;
	}

	public void setCooldown(String skill, int duration) {
		Map<String, Integer> cooldown = data.get(player);
		if (cooldown != null) {
			cooldown.put(skill, duration);
		}
	}

	public void addCooldown(String skill, int duration) {
		Map<String, Integer> cooldown = data.get(player);
		if (cooldown != null) {
			cooldown.put(skill, cooldown.getOrDefault(skill, 0) + duration);
		} else {
			setCooldown(skill, duration);
		}
	}

	public void removeCooldown(String skill) {
		Map<String, Integer> cooldown = data.get(player);
		if (cooldown != null) {
			cooldown.remove(skill);
			if (cooldown.isEmpty()) {
				data.remove(player);
			}
		}
	}

	public void decreaseCooldown(String skill) {
		Map<String, Integer> cooldown = data.get(player);
		if (cooldown != null && cooldown.containsKey(skill)) {
			int currentCooldown = cooldown.get(skill);
			if (currentCooldown > 1) {
				cooldown.put(skill, currentCooldown - 1);
			} else {
				cooldown.remove(skill);
			}
			if (cooldown.isEmpty()) {
				data.remove(player);
			}
		}
	}

	public boolean hasCooldown(String skill) {
		Map<String, Integer> cooldown = data.get(player);
		return cooldown != null && cooldown.getOrDefault(skill, 0) > 0;
	}

	public int getCooldown(String skill) {
		Map<String, Integer> cooldown = data.get(player);
		return cooldown != null ? cooldown.getOrDefault(skill, 0) : 0;
	}

	public Player getPlayer() {
		return player;
	}
	
	public Map<String, Integer> getCooldownData() {
		return data.get(player);
	}
	
	public Map<Player, Map<String, Integer>> getData() {
		return data;
	}
}