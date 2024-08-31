package io.phanisment.itemcaster;

import io.phanisment.itemcaster.nbtapi.NBTCompound;
import io.phanisment.itemcaster.nbtapi.NBTItem;
import io.phanisment.itemcaster.nbtapi.NBTListCompound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NBTChecker {

	public List<Ability> getAbilities(Player player) {
		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		if (itemInHand == null || !itemInHand.hasItemMeta()) {
			return null;
		}

		NBTItem nbtItem = new NBTItem(itemInHand);
		if (!nbtItem.hasKey("Abilities")) {
			return null;
		}

		NBTCompound abilitiesCompound = nbtItem.getCompound("Abilities");
		List<Ability> abilities = new ArrayList<>();
		for (NBTListCompound abilityCompound : abilitiesCompound.getCompoundList("Abilities")) {
			String id = abilityCompound.getString("id");
			String event = abilityCompound.hasKey("event") ? abilityCompound.getString("event") : null;
			Integer timer = abilityCompound.hasKey("timer") ? abilityCompound.getInteger("timer") : null;
			abilities.add(new Ability(id, event, timer));
		}

		return abilities;
	}

	public static class Ability {
		private final String id;
		private final String event;
		private final Integer timer;

		public Ability(String id, String event, Integer timer) {
			this.id = id;
			this.event = event;
			this.timer = timer;
		}

		public String getId() {
			return id;
		}

		public String getEvent() {
			return event;
		}

		public Integer getTimer() {
			return timer;
		}

		@Override
		public String toString() {
			return "Ability{id='" + id + "', event='" + event + "', timer=" + timer + "}";
		}
	}
}
