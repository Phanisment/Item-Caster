package phanisment.itemcaster.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import phanisment.itemcaster.skills.SkillActivator;
import phanisment.itemcaster.skills.SkillCooldown;
import phanisment.itemcaster.Main;

import java.util.Map;

public class ActivatorListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			runSkill(player, SkillActivator.Activator.RIGHT_CLICK);
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.RIGHT_CLICK);
	}

	@EventHandler
	public void onPlayerSwing(PlayerAnimationEvent event) {
		Player player = event.getPlayer();
		switch (event.getAnimationType()) {
			case ARM_SWING:
				ItemStack hand = player.getInventory().getItemInMainHand();
				if (hand != null || hand.getType() != Material.AIR) {
					new SkillActivator(player, hand, SkillActivator.Activator.LEFT_CLICK);
				}
				break;
			case OFF_ARM_SWING:
				ItemStack offHand = player.getInventory().getItemInOffHand();
				if (offHand != null || offHand.getType() != Material.AIR) {
					new SkillActivator(player, offHand, SkillActivator.Activator.LEFT_CLICK);
				}
				break;
		}
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.DROP);
		}
	}

	@EventHandler
	public void onPlayerPickUp(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.PICKUP);
		}
	}

	@EventHandler
	public void onPlayerDamaged(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			runSkill(player, SkillActivator.Activator.DAMAGED);
		}
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player)event.getDamager();
			runSkill(player, SkillActivator.Activator.ATTACK);
			if (player.isSprinting()) {
				runSkill(player, SkillActivator.Activator.SPRINT_ATTACK);
			}
			if (!player.isOnGround()) {
				runSkill(player, SkillActivator.Activator.CRITICAL_ATTACK);
			}
		}
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (event.isSneaking()) {
			runSkill(player, SkillActivator.Activator.SNEAK);
		} else {
			runSkill(player, SkillActivator.Activator.UNSNEAK);
		}
	}

	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.CONSUME);
	}

	@EventHandler
	public void onPlayerShoot(EntityShootBowEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			ItemStack item = event.getBow();
			if (item != null || item.getType() != Material.AIR) {
				new SkillActivator(player, item, SkillActivator.Activator.BOW_SHOOT);
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		runSkill(player, SkillActivator.Activator.DEATH);
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.LOGIN);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.QUIT);
	}
	
	@EventHandler
	public void onChangeSlot(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.CHANGE_SLOT);
	}

	@EventHandler
	public void onItemBreak(PlayerItemBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getBrokenItem();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.ITEM_BREAK);
		}
	}

	public static void runTick(Main pl) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (Bukkit.getOnlinePlayers().isEmpty()) return;
				for (Player player : Bukkit.getOnlinePlayers()) {
					runSkill(player, SkillActivator.Activator.TICK);
					new SkillCooldown(player).runTick();
				}
			}
		}.runTaskTimer(pl, 0L, 1L);
	}
	
	private static void runSkill(Player player, SkillActivator.Activator type) {
		ItemStack hand = player.getInventory().getItemInMainHand();
		if (hand != null || hand.getType() != Material.AIR) {
			new SkillActivator(player, hand, type);
		}
		ItemStack offHand = player.getInventory().getItemInOffHand();
		if (offHand != null || offHand.getType() != Material.AIR) {
			new SkillActivator(player, offHand, type);
		}
	}
}