package phanisment.artifact.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import phanisment.artifact.MythicMobs;

public class ArtifactCommand implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender ,Command command, String label, String[] args) {
    if (args.length >= 1) return false;
    Player player = (Player)sender;
    MythicMobs.cast(args[0], player);
    return true;
  }
}
