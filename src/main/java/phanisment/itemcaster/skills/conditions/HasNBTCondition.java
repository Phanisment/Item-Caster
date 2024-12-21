/*package phanisment.itemcaster.skills.conditions;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;

import de.tr7zw.nbtapi.NBTItem;

public class HasNBTCondition implements IEntityCondition {
	private String slot;
	private String key;
	
	public HasNBTCondition(MythicLineConfig config) {
		slot = config.getString(new String[]{"slot", "s"}, "HAND");
		key = config.getString(new String[]{"key", "k"});
	}
	
	@Override
	public boolean check(AbstractEntity target) {
		LivingEntity entity = (LivingEntity)target.getBukkitEntity();
		ItemStack item;
		switch (EquipmentSlot.valueOf(slot.toUpperCase())) {
			case HAND:
				item = entity.getEquipment().getItemInMainHand();
				break;
			case OFF_HAND:
				item = entity.getEquipment().getItemInOffHand();
				break;
			case HEAD:
				item = entity.getEquipment().getHelmet();
				break;
			case CHEST:
				item = entity.getEquipment().getChestplate();
				break;
			case LEGS:
				item = entity.getEquipment().getLeggings();
				break;
			case FEET:
				item = entity.getEquipment().getBoots();
				break;
		}
		return new NBTItem(item).hasTag(key);
	}
}*/