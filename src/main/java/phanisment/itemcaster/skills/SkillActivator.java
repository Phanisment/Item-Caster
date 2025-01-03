package phanisment.itemcaster.skills;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.MythicUtil;

import de.tr7zw.nbtapi.NBTType;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadableNBT;

import phanisment.itemcaster.skills.SkillCooldown;

import java.util.ArrayList;

public class SkillActivator {
	public SkillActivator(Player player, ItemStack item, Activator activator) {
		if (item == null || item.getType() == Material.AIR) return;
		NBTCompoundList nbt = new NBTItem(item).getCompoundList("Artifact");
		if (!nbt.isEmpty()) {
			for (ReadableNBT ability : nbt) {
				String skill = ability.getString("skill");
				String type = ability.getString("activator").toUpperCase();
				if (type.equals(activator.toString()) && !skill.isEmpty()) {
					float power = 1.0F;
					if (ability.hasTag("power", NBTType.NBTTagInt) || ability.hasTag("power", NBTType.NBTTagFloat)) {
						power = ability.getFloat("power");
					}
					SkillCooldown cd = new SkillCooldown(player);
					if (!cd.hasCooldown(skill)) {
						cast(player, skill, power);
						if (ability.hasTag("cooldown", NBTType.NBTTagInt)) {
							cd.setCooldown(skill, ability.getInteger("cooldown"));
						}
					}
				}
			}
		}
	}
	
	private void cast(Player player, String skill, float power) {
		LivingEntity target = MythicUtil.getTargetedEntity(player);
		ArrayList<Entity> targets = new ArrayList<Entity>();
		targets.add((Entity)target);
		MythicBukkit.inst().getAPIHelper().castSkill((Entity)player, skill, (Entity)player, player.getLocation(), targets, null, power);
	}
	
	public enum Activator {
		LEFT_CLICK, RIGHT_CLICK,
		LEFT_CLICK_AIR, LEFT_CLICK_BLOCK,
		RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK,
		SNEAK, UNSNEAK,
		ATTACK, DAMAGED, CRITICAL_ATTACK, SPRINT_ATTACK,
		CONSUME, BOW_SHOOT,
		DEATH, ITEM_BREAK,
		DROP, PICKUP,
		CHANGE_SLOT,
		LOGIN, QUIT,
		SIGNAL, TICK;
	}
}