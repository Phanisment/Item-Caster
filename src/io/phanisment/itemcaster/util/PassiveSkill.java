package io.phanisment.itemcaster.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.inventory.ItemStack;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.api.CastSkill;
import io.phanisment.itemcaster.util.AbilityManager;
import io.phanisment.itemcaster.util.AbilityManager.Ability;

import java.util.List;

public class PassiveSkill {
	private final ItemCaster plugin;
	private final AbilityManager nbtAbility = new AbilityManager()
	
	
	public PassiveSkillChecker(ItemCaster plugin) {
		this.plugin = plugin;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					ItemStack item = player.getInventory().getItemInMainHand();
					List<AbilityManager.Ability> abilites = nbtAbility.getAbilities(item);
					if(abilites != null) {
						for(AbilityManager ability : abilites) {
							if(abilites.getEvent() == null && abilites.getTimer() != null) {
								new CastSkill.cast(abilites.getId(), player);
							}
						}
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
}