package io.phanisment.itemcaster.skills;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Cancellable;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.MythicUtil;

import de.tr7zw.changeme.nbtapi.NBTType;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class SkillActivator {
	public SkillActivator(Player player, ItemStack item, Activator activator) {
		this(player, item, activator, "");
	}

	public SkillActivator(Player player, ItemStack item, Activator activator, String signal) {
		this(player, item, activator, signal, null);
	}

	public SkillActivator(Player player, ItemStack item, Activator activator, Cancellable event) {
		this(player, item, activator, "", event);
	}

	public SkillActivator(Player player, ItemStack item, Activator activator, String signal, Cancellable event) {
		if (item == null || item.getType() == Material.AIR) return;
		NBTCompound nbt = new NBTItem(item).getCompound("ItemCaster");
		if (nbt != null) {
			NBTCompoundList abilities = nbt.getCompoundList("Abilities");
			if(!abilities.isEmpty()) {
				for (ReadableNBT ability : abilities) {
					String skill = ability.getString("skill");
					String type = ability.getString("activator").toUpperCase();
					if (type.equals(activator.toString()) && !skill.isEmpty()) {
						float power = 1.0F;
						if (ability.hasTag("power", NBTType.NBTTagInt) || ability.hasTag("power", NBTType.NBTTagFloat)) power = ability.getFloat("power");
						if (ability.hasTag("signal", NBTType.NBTTagString) && !signal.equals(ability.getString("signal"))) return;
						if ((ability.getBoolean("sneak") != null && ability.getBoolean("sneak")) && player.isSneaking()) return;
						if (ability.getBoolean("cancel_event") != null && ability.getBoolean("cancel_event")) event.setCancelled(true);
						
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
										default:
											data.getVariables().putFloat(key, variable.getFloat(key));
											break;
									}
								}
							}
						});
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
		HOLD_SNEAK,
		DOUBLE_SNEAK,
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
		//CHANGE_ARMOR,
		LOGIN,
		QUIT,
		FISHING,
		TELEPORT,
		SIGNAL,
		TICK;
	}
}