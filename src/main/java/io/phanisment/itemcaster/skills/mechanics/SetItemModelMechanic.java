package io.phanisment.itemcaster.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import dev.lone.itemsadder.api.CustomStack;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;

import io.phanisment.itemcaster.ItemCaster;

public class SetItemModelMechanic implements ITargetedEntitySkill {
	private final int modelData;
	
	public SetItemModelMechanic(MythicLineConfig config) {
		this.modelData = config.getInteger(new String[]{"modelData", "model", "data", "m", "d"}, 0);
	}

	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		LivingEntity entity = (LivingEntity) BukkitAdapter.adapt(target);
		ItemStack	item = entity.getEquipment().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		if (item != null && item.getType() != Material.AIR) {
			if (modelData >= 1) {
				meta.setCustomModelData(modelData);
				item.setItemMeta(meta);
				entity.getEquipment().setItemInMainHand(item);
			}
			return SkillResult.SUCCESS;
		}
		return SkillResult.INVALID_CONFIG;
	}
}
