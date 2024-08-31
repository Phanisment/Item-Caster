package io.phanisment.itemcaster.util;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AbilityManager {
	public List<Abilites> getAbilites(ItemStack item) {
		if(item == null || !item.hasItemMeta()){
			return null;
		}
		
		NBTItem itemNbt = new NBTItem(item);
		List<Abilites> abilites = new ArrayList<>();
		for(NBTListCompound itemNbt : itemNbt.getCompoundList("Abilites")) {
			String id = itemNbt.getString("id");
			String event = itemNbt.hasKey("event") ? itemNbt.getString("event") : null;
			Integer timer = itemNbt.hasKey("timer") ? itemNbt.getInteger("timer") : null;
			abilities.add(new Ability(id, event, timer));
		}
			return abilities;
		}
	}
	
	public static class Ability {
		private final String id;
		private final String event;
		private final Integer timer;
		
		public String getId() {
			return id;
		}
		
		public String getEvent() {
			return event;
		}
		
		public int getTimer() {
			return timer;
		}
		
		public String toString() {
			return "Ability{id='" + id + "', event='" + event + "', timer=" + timer + "}";
		}
	}
}
