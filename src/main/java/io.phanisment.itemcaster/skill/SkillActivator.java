package io.phanisment.itemcaster.skill;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.MythicUtil;
import io.lumine.mythic.api.skills.SkillMetadata;

import de.tr7zw.nbtapi.NBTType;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import de.tr7zw.nbtapi.iface.NBTHandler;

import io.phanisment.itemcaster.util.DebugUtil;
import io.phanisment.itemcaster.util.NbtUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class SkillActivator {
	private static final Map<String, Integer> skill_interval = new HashMap<>();
	
	private final ItemStack item;
	private final Player player;
	private final Activator activator;
	private NBTCompound inst;
	private String signal;
	private int index;
	
	public SkillActivator(Player player, ItemStack item, Activator activator) {
		this.item = item;
		this.player = player;
		this.activator = activator;
		if (!validateItem(this.item)) return;
		this.inst = new NBTItem(item).getCompound("ItemCaster");
		
		if (this.inst == null) return;
		
		NBTCompoundList abilities = inst.getCompoundList("abilities");
		abilities.forEach(this::readAbilityAttributes);
	}
	
	/** 
	 * Check if item is not empty.
	 */
	public static boolean validateItem(ItemStack item) {
		return item != null && item.getType() != Material.AIR;
	}
	
	private void readAbilityAttributes(ReadableNBT ability) {
		String skill = ability.getString("skill");
		String event = ability.getString("activator").toUpperCase();
		
		int curindex = index++;
		if (skill.isEmpty()) {
			DebugUtil.warn("Ability slot {0} required attributes skill to work!", curindex);
			return;
		}
		
		if (event.equals(activator.toString())) {
			DebugUtil.info("Event is equal: {0} = {1}", event, activator.toString());
			float power = NbtUtil.getFloatSafe(ability, "power", 1.0F);
			
			if (event.equals("SIGNAL") && ability.hasTag("signal", NBTType.NBTTagString)) {
				String i_signal = ability.getString("signal");
				if (!signal.equals(i_signal)) return;
			}
			
			if (event.equals("TICK")) {
				int interval = ability.getInteger("interval");
				int count = skill_interval.getOrDefault(skill, 0);
				DebugUtil.info("Interval remain: {0}", count);
				if (count > 0) {
					skill_interval.put(skill, count - 1);
					return;
				}
				skill_interval.put(skill, interval);
			}
			
			ReadableNBT variables = ability.getCompound("variables");
			cast(player, new AbilityMeta(skill, Activator.value(event), new AbilityMeta.SkillAttributes(power, signal, variables)));
		}
	}
	
	public static void cast(Player player, AbilityMeta meta) {
		LivingEntity target = MythicUtil.getTargetedEntity(player);
		ArrayList<Entity> targets = new ArrayList<>();
		targets.add((Entity)target);
		MythicBukkit.inst().getAPIHelper().castSkill(
			(Entity)player,
			meta.skill(),
			(Entity)player,
			player.getLocation(),
			targets,
			null,
			meta.attributes().power(),
			data -> setVariables(data, meta)
		);
	}
	
	private static void setVariables(SkillMetadata data, AbilityMeta meta) {
		ReadableNBT variables = meta.attributes().variables();
		meta.attributes().getVarName().forEach(key -> {
			switch(variables.getType(key)) {
				case NBTTagFloat:
					data.getVariables().putFloat(key, variables.getFloat(key));
					break;
				case NBTTagInt:
					data.getVariables().putInt(key, variables.getInteger(key));
					break;
				case NBTTagString:
					data.getVariables().putString(key, variables.getString(key));
					break;
				default:
					data.getVariables().putFloat(key, variables.getFloat(key));
					break;
			}
		});
	}
	
	public void setSignal(String signal) {
		this.signal = signal;
	}
	
	public static enum Activator {
		LEFT_CLICK,
		RIGHT_CLICK,
		TICK,
		SIGNAL;
		
		public static Activator value(String name) {
			return Enum.valueOf(Activator.class, name);
		}
	}
}