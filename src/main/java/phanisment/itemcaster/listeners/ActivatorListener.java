package phanisment.itemcaster.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import phanisment.itemcaster.skill.SkillActivator;
import phanisment.itemcaster.Main;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class ActivatorListener implements Listener {
	private final Set<Player> activePlayer = new HashSet<>();
	private Main plugin;

	public ActivatorListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null || item.getType() != Material.AIR) {
			switch(event.getAction()) {
				case LEFT_CLICK_AIR:
				case LEFT_CLICK_BLOCK:
					new SkillActivator(player, item, SkillActivator.Activator.LEFT_CLICK);
					break;
				case RIGHT_CLICK_AIR:
				case RIGHT_CLICK_BLOCK:
					new SkillActivator(player, item, SkillActivator.Activator.USE);
					break;
				default:
					return;
			}
		}
	}

	@EventHandler
	public void onPlayerSwing(PlayerAnimationEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null || item.getType() != Material.AIR) {
			switch (event.getAnimationType()) {
				case ARM_SWING:
					new SkillActivator(player, item, SkillActivator.Activator.SWING);
					break;
				case OFF_ARM_SWING:
					new SkillActivator(player, item, SkillActivator.Activator.OFF_SWING);
					break;
				default:
					return;
			}
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
			ItemStack item = player.getInventory().getItemInMainHand();
			if (item != null || item.getType() != Material.AIR) {
				new SkillActivator(player, item, SkillActivator.Activator.DAMAGED);
				switch(event.getCause()) {
					case FALL:
						new SkillActivator(player, item, SkillActivator.Activator.FALL);
						break;
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player)event.getDamager();
			ItemStack item = player.getInventory().getItemInMainHand();
			if (item != null || item.getType() != Material.AIR) {
				new SkillActivator(player, item, SkillActivator.Activator.ATTACK);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.USE);
			new SkillActivator(player, item, SkillActivator.Activator.INTERACT_ENTITY);
		}
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.TOGGLE_SNEAK);
			if (event.isSneaking()) {
				new SkillActivator(player, item, SkillActivator.Activator.SNEAK);
			} else {
				new SkillActivator(player, item, SkillActivator.Activator.UNSNEAK);
			}
		}
	}

	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.CONSUME);
		}
	}

	@EventHandler
	public void onPlayerShoot(EntityShootBowEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			ItemStack item = event.getBow();
			if (item != null || item.getType() != Material.AIR) {
				new SkillActivator(player, item, SkillActivator.Activator.SHOOT);
			}
		}
	}

	@EventHandler
	public void onPlayerSwapHand(PlayerSwapHandItemsEvent event) {
		Player player = event.getPlayer();
		ItemStack item = null;
		if (event.getMainHandItem().getType() != Material.AIR) {
			item = event.getMainHandItem();
		} else if (event.getOffHandItem().getType() != Material.AIR) {
			item = event.getOffHandItem();
		}
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.SWAP_HAND);
		}
	}
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getBrokenItem();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.BREAK);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null || item.getType() != Material.AIR) {
			new SkillActivator(player, item, SkillActivator.Activator.DEATH);
		}
	}

	public static void runTick(Main pl) {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					ItemStack item = player.getInventory().getItemInMainHand();
					if (item != null || item.getType() != Material.AIR) {
						new SkillActivator(player, item, SkillActivator.Activator.TICK);
					}
				}
			}
		}.runTaskTimer(pl, 0L, 1L);
	}
}