package io.phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public interface SubCommand {
	String[] getName();
	void execute(CommandSender sender, String[] args);
	default List<String> tabComplete(CommandSender sender, String[] args) {return new ArrayList<>();}
	default boolean allowConsole() {return true;}
	default boolean allowMember() {return false;}
}