package io.phanisment.itemcaster.skills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.MythicUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class HandActivator {
	private static Map<Player, Map<String, Activator>> data = new HashMap<>();
	
	public HandActivator(Player player, Activator event) {
		if (!data.containsKey(player)) return;
		
		Map<String, Activator> skills = data.get(player);
		if (skills == null ) return;
		for (Map.Entry<String, Activator> skill : skills.entrySet()) {
			if (skill.getValue() == event) castSkill(player, skill.getKey());
		}
	}
	
	private void castSkill(Player player, String skill) {
		LivingEntity target = MythicUtil.getTargetedEntity(player);
		ArrayList<Entity> targets = new ArrayList<Entity>();
		targets.add((Entity)target);
		MythicBukkit.inst().getAPIHelper().castSkill((Entity)player, skill, (Entity)player, player.getLocation(), targets, null, 1.0F);
	}
	
	public static void set(Player player, Activator activator, String skill) {
		data.putIfAbsent(player, new HashMap<>());
		Map<String, Activator> skills = data.get(player);
		skills.put(skill, activator);
	}
	
	public static void remove(Player player, String skill) {
		Map<String, Activator> skills = data.get(player);
		if (skills != null) {
			skills.remove(skill);
			if (skills.isEmpty()) {
				data.remove(player);
			}
		}
	}
	
	public static void removeAll(Player player) {
		data.remove(player);
	}
	
	public static Map<String, HandActivator.Activator> getSkills(Player player) {
		Map<String, HandActivator.Activator> skills = data.get(player);
		if (skills == null) {
			return new HashMap<>();
		}
		return skills;
	}
	
	public enum Activator {
		LEFT_CLICK,
		RIGHT_CLICK,
		SNEAK,
		TICK;
	}
}