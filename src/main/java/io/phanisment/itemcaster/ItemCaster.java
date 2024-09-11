package io.phanisment.itemcaster;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

import io.phanisment.itemcaster.SkillExecutor;
import io.phanisment.itemcaster.util.SkillManager;

public class ItemCaster extends JavaPlugin {
	private Long Timer;

	@Override
	public void onEnable() {
		getLogger().info("Plugin Installed!");
		getServer().getPluginManager().registerEvents(new SkillExecutor(), this);
		
		getServer().getScheduler().runTaskTimer(this, new PassiveSkill() {
			for (Player player : Bukkit.getOnlinePlayers()) {
				ItemStack item = player.getInventory().getItemInMainHand();
				NBTItem nbtItem = new NBTItem(item);
				NBTCompoundList abilities = nbtItem.getCompoundList("Abilities");
				if (abilities != null) {
					for (ReadWriteNBT ability : abilities) {
						String skill = ability.getString("skill");
						String action  = ability.getString("action");
						this.TIMER = ability.getInt("timer");
					}
			}
			}
		}, 0L, this.Timer);
}