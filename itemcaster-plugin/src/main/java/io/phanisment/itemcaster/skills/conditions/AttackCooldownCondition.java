package io.phanisment.itemcaster.skills.conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.core.logging.MythicLogger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AttackCooldownCondition implements IEntityCondition {
	@Override
	public boolean check(AbstractEntity target) {
		Entity entity = target.getBukkitEntity();
		if (!(entity instanceof Player)) return false;
		Player player = (Player)entity;
		MythicLogger.debug(MythicLogger.DebugLevel.CONDITION, "Player {0} attack cooldown {1}", player.getName(), player.getAttackCooldown() < 1.0);
		return player.getAttackCooldown() < 1.0;
	}
}