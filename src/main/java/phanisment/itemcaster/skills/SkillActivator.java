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
				if (ability.getString("activator").replaceAll(" ", "_").toUpperCase().equals(activator.toString()) && !skill.isEmpty()) {
					
					// Power System
					float power = 1.0F;
					if (ability.hasTag("power", NBTType.NBTTagInt) || ability.hasTag("power", NBTType.NBTTagFloat)) {
						power = ability.getFloat("power");
					}
					
					// Cooldown System
					SkillCooldown cd = new SkillCooldown(player);
					if (ability.hasTag("cooldown", NBTType.NBTTagInt)) {
						cd.setCooldown(skill, ability.getInteger("cooldown"));
					}
					
					if (!cd.hasCooldown(skill)) {
						cast(player, skill, power);
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
		LEFT_CLICK,
		RIGHT_CLICK,
		EQUIP, // Not yet added: Im lazy to add this.
		SNEAK,
		UNSNEAK,
		ATTACK,
		DAMAGED,
		CONSUME,
		BOW_SHOOT,
		DEATH,
		DROP,
		PICKUP, 
		SIGNAL, // Not yet added: Will add in last development version.
		TICK;
	}
}