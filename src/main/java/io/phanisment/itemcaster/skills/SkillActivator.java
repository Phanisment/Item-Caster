package io.phanisment.itemcaster.skills;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.MythicUtil;

import de.tr7zw.nbtapi.NBTType;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.iface.ReadableNBT;

import io.phanisment.itemcaster.skills.SkillCooldown;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class SkillActivator {
	public SkillActivator(Player player, ItemStack item, Activator activator) {
		this(player, item, activator, "");
	}

	public SkillActivator(Player player, ItemStack item, Activator activator, String signal) {
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
					
					if (ability.hasTag("signal", NBTType.NBTTagString)) {
						if (!signal.equals(ability.getString("signal"))) return;
					}
					
					SkillCooldown cd = new SkillCooldown(player);
					if (!cd.hasCooldown(skill)) {
						LivingEntity target = MythicUtil.getTargetedEntity(player);
						ArrayList<Entity> targets = new ArrayList<Entity>();
						targets.add((Entity)target);
						MythicBukkit.inst().getAPIHelper().castSkill((Entity)player, skill, (Entity)player, player.getLocation(), targets, null, power, data -> {
							if (ability.hasTag("variable", NBTType.NBTTagCompound)) {
								NBTCompound variable = (NBTCompound)ability.getCompound("variable");
								for(String key : variable.getKeys()) {
									switch(variable.getType(key)) {
										case NBTTagFloat:
											data.getVariables().putFloat(key, variable.getFloat(key));
											break;
										case NBTTagInt:
											data.getVariables().putInt(key, variable.getInteger(key));
											break;
										case NBTTagString:
											data.getVariables().putString(key, variable.getString(key));
											break;
									}
								}
							}
						});
						if (ability.hasTag("cooldown", NBTType.NBTTagInt)) {
							cd.setCooldown(skill, ability.getInteger("cooldown"));
						}
					}
				}
			}
		}
	}
	
	public enum Activator {
		LEFT_CLICK,
		RIGHT_CLICK,
		BLOCK_PLACE,
		BLOCK_DAMAGED,
		BLOCK_STOP_DAMAGED,
		BLOCK_BREAK,
		SNEAK,
		UNSNEAK,
		TOGGLE_SNEAK,
		SPRINT,
		UNSPRINT,
		TOGGLE_SPRINT,
		ATTACK,
		DAMAGED,
		FALL_DAMAGE,
		FIRE_DAMAGE,
		FIRE_TICK_DAMAGE,
		FREEZE_DAMAGE,
		LIGHTNING_DAMAGE,
		VOID_DAMAGE,
		WITHER_DAMAGE,
		CONSUME,
		BOW_SHOOT,
		DEATH,
		ITEM_BREAK,
		DROP,
		PICKUP,
		CHANGE_SLOT,
		CHANGE_ARMOR,
		LOGIN,
		QUIT,
		FISHING,
		TELEPORT,
		SIGNAL,
		TICK;
	}
}