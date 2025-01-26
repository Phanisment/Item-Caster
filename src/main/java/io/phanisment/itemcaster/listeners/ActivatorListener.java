package io.phanisment.itemcaster.listeners;

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

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;

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
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import io.phanisment.itemcaster.skills.SkillActivator;
import io.phanisment.itemcaster.skills.SkillCooldown;
import io.phanisment.itemcaster.skills.HandActivator;
import io.phanisment.itemcaster.ItemCaster;

import java.util.HashMap;
import java.util.Map;

public class ActivatorListener implements Listener {
	private Map<Player, Long> lastSneak = new HashMap<>();
	private ItemCaster plugin;
	
	public ActivatorListener(ItemCaster plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			new HandActivator(player, HandActivator.Activator.RIGHT_CLICK);
			runSkill(player, SkillActivator.Activator.RIGHT_CLICK);
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		new HandActivator(player, HandActivator.Activator.RIGHT_CLICK);
		runSkill(player, SkillActivator.Activator.RIGHT_CLICK);
	}

	@EventHandler
	public void onPlayerSwing(PlayerAnimationEvent event) {
		Player player = event.getPlayer();
		switch (event.getAnimationType()) {
			case ARM_SWING:
				new HandActivator(player, HandActivator.Activator.LEFT_CLICK);
				ItemStack hand = player.getInventory().getItemInMainHand();
				if (hand != null && hand.getType() != Material.AIR) {
					new SkillActivator(player, hand, SkillActivator.Activator.LEFT_CLICK);
				}
				break;
			case OFF_ARM_SWING:
				new HandActivator(player, HandActivator.Activator.LEFT_CLICK);
				ItemStack offHand = player.getInventory().getItemInOffHand();
				if (offHand != null && offHand.getType() != Material.AIR) {
					new SkillActivator(player, offHand, SkillActivator.Activator.LEFT_CLICK);
				}
				break;
		}
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		if (item != null && item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.DROP);
		}
	}

	@EventHandler
	public void onPlayerPickUp(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		if (item != null && item.getType() != Material.AIR) {
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
			switch(event.getCause()) {
				case FALL:
					runSkill(player, SkillActivator.Activator.FALL_DAMAGE);
					break;
				case FIRE:
					runSkill(player, SkillActivator.Activator.FIRE_DAMAGE);
					break;
				case FIRE_TICK:
					runSkill(player, SkillActivator.Activator.FIRE_TICK_DAMAGE);
					break;
				case FREEZE:
					runSkill(player, SkillActivator.Activator.FREEZE_DAMAGE);
					break;
				case LIGHTNING:
					runSkill(player, SkillActivator.Activator.LIGHTNING_DAMAGE);
					break;
				case VOID:
					runSkill(player, SkillActivator.Activator.VOID_DAMAGE);
					break;
				case WITHER:
					runSkill(player, SkillActivator.Activator.WITHER_DAMAGE);
			}
		}
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		long currentTime = System.currentTimeMillis();
		runSkill(player, SkillActivator.Activator.TOGGLE_SNEAK);
		if (event.isSneaking()) {
			new HandActivator(player, HandActivator.Activator.SNEAK);
			runSkill(player, SkillActivator.Activator.SNEAK);
			if (lastSneak.containsKey(player) && (currentTime - lastSneak.get(player) <= 500)) runSkill(player, SkillActivator.Activator.DOUBLE_SNEAK);
			lastSneak.put(player, currentTime);
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
			if (item != null && item.getType() != Material.AIR) {
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
		if (item != null && item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.ITEM_BREAK);
		}
	}

	@EventHandler
	public void onFish(PlayerFishEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.FISHING);
	}

	@EventHandler
	public void onSprint(PlayerToggleSprintEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.TOGGLE_SPRINT);
		if (event.isSprinting()) {
			runSkill(player, SkillActivator.Activator.SPRINT);
		} else {
			runSkill(player, SkillActivator.Activator.UNSPRINT);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemInHand();
		if (item != null && item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.BLOCK_PLACE);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.BLOCK_BREAK);
	}

	@EventHandler
	public void onBlockDamaged(BlockDamageEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemInHand();
		if (item != null && item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.BLOCK_DAMAGED);
		}
	}

	@EventHandler
	public void onBlockStopDamaged(BlockDamageAbortEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemInHand();
		if (item != null && item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.BLOCK_STOP_DAMAGED);
		}
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		runSkill(player, SkillActivator.Activator.TELEPORT);
	}
/*
	@EventHandler
	public void onChangeArmor(PlayerArmorChangeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getNewItem();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.CHANGE_ARMOR);
		}
	}
*/
	public static void runTick(ItemCaster pl) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (Bukkit.getOnlinePlayers().isEmpty()) return;
				for (Player player : Bukkit.getOnlinePlayers()) {
					runSkill(player, SkillActivator.Activator.TICK);
					new SkillCooldown(player).runTick();
					new HandActivator(player, HandActivator.Activator.TICK);
					if (player.isSneaking()) {
						runSkill(player, SkillActivator.Activator.HOLD_SNEAK);
					}
				}
			}
		}.runTaskTimer(pl, 0L, 1L);
	}

	private static void runSkill(Player player, SkillActivator.Activator type) {
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		if (mainHand != null && mainHand.getType() != Material.AIR) {
			new SkillActivator(player, mainHand, type);
		}
		
		ItemStack offHand = player.getInventory().getItemInOffHand();
		if (offHand != null && offHand.getType() != Material.AIR) {
			new SkillActivator(player, offHand, type);
		}
		
		ItemStack helmet = player.getInventory().getHelmet();
		if (helmet != null && helmet.getType() != Material.AIR) {
			new SkillActivator(player, helmet, type);
		}
		
		ItemStack chestplate = player.getInventory().getChestplate();
		if (chestplate != null && chestplate.getType() != Material.AIR) {
			new SkillActivator(player, chestplate, type);
		}
		
		ItemStack leggings = player.getInventory().getLeggings();
		if (leggings != null && leggings.getType() != Material.AIR) {
			new SkillActivator(player, leggings, type);
		}
		
		ItemStack boots = player.getInventory().getBoots();
		if (boots != null && boots.getType() != Material.AIR) {
			new SkillActivator(player, boots, type);
		}
	}
}