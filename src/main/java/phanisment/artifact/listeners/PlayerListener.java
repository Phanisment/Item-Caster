package phanisment.artifact.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBT;
import phanisment.artifact.MythicMobs;

public class PlayerListener implements Listener {

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if(event.getAction() == Action.LEFT_CLICK_AIR && event.getAction() == Action.LEFT_CLICK_BLOCK) {
      Player player = event.getPlayer();
      ItemStack item = player.getInventory().getItemInMainHand();
      String data = NBT.get(item, nbt -> (String)nbt.getString("Left-Click"));
      if (!data.isEmpty()) {
        player.sendMessage("Has Data: " + data);
        MythicMobs.cast(data, player);
      }
    }
  }
}
