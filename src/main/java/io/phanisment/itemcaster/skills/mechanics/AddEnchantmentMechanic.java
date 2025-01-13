package io.phanisment.itemcaster.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;

public class AddEnchantmentMechanic implements ITargetedEntitySkill {
	private final EquipmentSlot slot;
	private final Enchantment enchantment;
	private final int level;
	
	public AddEnchantmentMechanic(MythicLineConfig config) {
		this.slot = EquipmentSlot.valueOf(config.getString(new String[]{"slot", "s"}, "HAND").toUpperCase());
		this.enchantment = Enchantment.getByName(config.getString(new String[]{"enchantment", "enchant", "enc", "e"}).toUpperCase());
		this.level = config.getInteger(new String[]{"level", "lvl", "l"}, 1);
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
		if (item != null && item.getType() != Material.AIR) {
			item.addEnchantment(enchantment, level);
			return SkillResult.SUCCESS;
		}
		return SkillResult.INVALID_CONFIG;
	}
}