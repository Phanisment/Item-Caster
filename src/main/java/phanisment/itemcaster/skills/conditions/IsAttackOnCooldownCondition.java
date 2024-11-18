package phanisment.itemcaster.skills.conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class IsAttackOnCooldownCondition implements IEntityCondition {
	public IsAttackOnCooldownCondition(MythicLineConfig config) {
	}
	
	@Override
	public boolean check(AbstractEntity e) {
		Entity entity = e.getBukkitEntity();
		if (!(entity instanceof Player)) return false;
		Player player = (Player)entity;
		return player.getAttackCooldown() <= 0.0;
	}
}