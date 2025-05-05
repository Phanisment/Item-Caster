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
	private final EquipmentSlot slot;
	private final int modelData;
	private final String ri;
	
	public SetItemModelMechanic(MythicLineConfig config) {
		this.slot = EquipmentSlot.valueOf(config.getString(new String[]{"slot", "s"}, "HAND").toUpperCase());
		this.modelData = config.getInteger(new String[]{"modelData", "model", "data", "m", "d"}, 0);
		this.ri = config.getString(new String[]{"item","i"});
	}

	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		LivingEntity entity = (LivingEntity) BukkitAdapter.adapt(target);
		ItemStack item = null;
		switch(slot) {
			case CHEST:
				item = entity.getEquipment().getChestplate();
				break;
			case FEET:
				item = entity.getEquipment().getBoots();
				break;
			case HAND:
				item = entity.getEquipment().getItemInMainHand();
				break;
			case HEAD:
				item = entity.getEquipment().getHelmet();
				break;
			case LEGS:
				item = entity.getEquipment().getLeggings();
				break;
			case OFF_HAND:
				item = entity.getEquipment().getItemInOffHand();
				break;
		}
		ItemMeta meta = item.getItemMeta();
		if (item != null && item.getType() != Material.AIR) {
			if (modelData >= 1) {
				meta.setCustomModelData(modelData);
				item.setItemMeta(meta);
			} else {
				if (ItemCaster.hasItemsAdder()) {
					CustomStack stack = CustomStack.getInstance(ri);
					if (stack != null) {
						ItemStack ri_i = stack.getItemStack();
						item.setType(ri_i.getType());
						meta.setCustomModelData(ri_i.getItemMeta().getCustomModelData());
						item.setItemMeta(meta);
					}
				}
			}
			return SkillResult.SUCCESS;
		}
		return SkillResult.INVALID_CONFIG;
	}
}
