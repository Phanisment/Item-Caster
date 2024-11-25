package phanisment.artifact;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.MythicUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MythicMobs {
	public static void cast(String id, Player player) {
		LivingEntity target = MythicUtil.getTargetedEntity(player);
		ArrayList<Entity> targets = new ArrayList<Entity>();
		targets.add((Entity)target);
		MythicBukkit.inst().getAPIHelper().castSkill((Entity)player, id, (Entity)player, player.getLocation(), targets, null, 1.0f);
	}
}