package io.phanisment.itemcaster;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

import java.util.ArrayList;
import java.util.List;

public class AbilityManager {
	private List<Ability> abilities:
	
	public AbilityManager(Player player) {
		ItemStack item = player.getInventory().getItemInHand();
		NBTItem nbtItem = new NBTItem(item);
		NBTCompound abilities = nbtItem.getCompound("Abilities");
		if(abilities != null) {
			NBTCompoundList listAbility = abilities.getCompoundList("Abilities");
			for(ReadWriteNBT ability : listAbility) {
				String skill = ability.getString("skill");
				String event = ability.getString("event");
				int timer = ability.getInteger("timer");
				this.abilities.add(new Ability(skill, event, timer));
			}
			return abilities;
		}
		return null;
	}

	public List<Ability> getAbility() {
		return abilities
	}

	public static class Ability {
		private String skill;
		private String event;
		private int timer;

		public Ability(String skill, String event, int timer) {
			this.skill = skill;
			this.event = event;
			this.timer = timer;
		}

		public String getSkill() {
			return skill;
		}

		public String getEvent() {
			return event;
		}

	public int getTimer() {
		return timer;
	}
}
