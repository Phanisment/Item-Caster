package io.phanisment.itemcaster;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

import io.phanisment.itemcaster.MythicMobs;

public class SkillExecutor implements Listener {
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null) {
			Player player = event.getPlayer();
			ItemStack item = player.getInventory().getItemInMainHand();
			NBTItem nbtItem = new NBTItem(item);
			NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");
			if(abilities != null) {
				for(ReadWriteNBT ability : abilities) {
					String skill = ability.getString("skill");
					String type = ability.getString("event");
					int timer = ability.getInteger("timer");
					player.sendMessage("Skill: " + skill + ", event: " + type + ", timer: " + timer);
					MythicMobs.runSkill(skill, player);
				}
			}
		}
	}
}