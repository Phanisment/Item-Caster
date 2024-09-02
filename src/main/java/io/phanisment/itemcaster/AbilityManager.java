package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

public class AbilityManager {
	private String skill;
	private String event;
	private int timer;
	
	public AbilityManager(Player player) {
		this(player.getInventory().getItemInHand());
	}
	
	public AbilityManager(ItemStack item) {
		NBTItem nbtItem = new NBTItem(item);
		NBTCompound abilities = nbtItem.getCompound("Abilities");
		if(abilities != null) {
			NBTCompoundList listAbility = abilities.getCompoundList("Abilities");
			for(ReadWriteNBT ability : listAbility) {
				String skill = ability.getString("skill");
				String event = ability.getString("event");
				int timer = ability.getInteger("timer");
			}
		}
	}
	
	public String getSkill() {
		return this.skill;
	}
	
	public String getEvent() {
		return this.event;
	}
	
	public int getTimer() {
		return this.timer;
	}
}