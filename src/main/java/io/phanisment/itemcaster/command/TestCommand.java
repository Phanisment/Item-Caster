package io.phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.script.FormatScript;
import io.phanisment.itemcaster.util.Message;

import java.util.ArrayList;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class TestCommand implements SubCommand {
	private final ItemCaster plugin;
	
	public TestCommand(ItemCaster plugin) {
		this.plugin = plugin;
	}

	@Override
	public String[] getName() {
		return new String[]{"test", "t"};
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		List<String> lore = new ArrayList<>();
		lore.add("This is legendary sword.");
		lore.add("that has ben slayed a god");
		lore.add("now is Abondent in mystical forest that no one knows where is it.");
		FormatScript.ScriptData lore_script = new FormatScript.ScriptData("lore", lore);
		Message.send(sender, "Result: " + plugin.formatScript.run(args[1], lore_script));
	}
}