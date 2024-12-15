package phanisment.itemcaster.skills;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SkillCooldown {
	private static final Map<Player, Map<String, Integer>> data = new HashMap<>();
	private final Player player;

	public SkillCooldown(Player player) {
		this.player = player;
		data.putIfAbsent(player, new HashMap<>());
	}

	public void setCooldown(String skill, int duration) {
		Map<String, Integer> cd = data.get(player);
		player.sendMessage("Set Cooldown [" + skill + "](" + player.getName() + "): "+ duration);
		cd.put(skill, duration);
	}

	public boolean hasCooldown(String skill) {
		Map<String, Integer> cd = data.get(player);
		return cd.containsKey(skill);
	}

	public static Map<Player, Map<String, Integer>> getData() {
		return data;
	}
}