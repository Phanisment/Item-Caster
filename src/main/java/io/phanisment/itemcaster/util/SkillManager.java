package io.phanisment.itemcaster.util;

import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.MythicMobs;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

public class SkillManager {
	private Player player;
	private String event;
	private Boolean hasTimer;
	
	public String SKILL;
	public String ACTION;
	public Integer TIMER;
	
	private Map<Player, Map<String, Integer>> skillTimers = new HashMap<>();
	
	public SkillManager(Player player, String event) {
		this.player = player;
		this.event = event;
	}

	public SkillManager runSkill() {
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.getType() != Material.AIR || item == null) {
			NBTItem nbtItem = new NBTItem(item);
			NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");
			if (abilities != null) {
				for (ReadWriteNBT ability : abilities) {
					this.SKILL = ability.getString("skill");
					this.ACTION = ability.getString("action");
					this.TIMER = ability.getInteger("timer");
					if(this.TIMER <= 0) {
						this.hasTimer = true;
					}
					this.hasTimer = false;
				}
			}
		}
		return this;
	}

	public void activeSkill() {
		if (this.event == this.ACTION && this.SKILL != null && this.ACTION != null && !this.hasTimer) {
			// Plans i want make is make a custom cooldown.
			MythicMobs.runSkill(this.SKILL, player);
		}
	}
	
	public void passiveSkill() {
		skillTimers.putIfAbsent(player, new HashMap<>());
		int cooldown = skillTimers.get(player).getOrDefault(this.SKILL, 0);
		if (this.event == "timer" && this.ACTION == null && this.SKILL != null && this.hasTimer) {
			cooldown++;
			if (cooldown >= this.TIMER) {
				MythicMobs.runSkill(this.SKILL, player);
			}
			
			skillTimers.get(player).put(this.SKILL, cooldown);
		}
	}
}