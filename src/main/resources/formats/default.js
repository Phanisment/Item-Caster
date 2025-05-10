/**
 * Default Configuration for Lore Format.
 * 
 * You can modify this and add whatever you want,
 * recommend to check the wiki because this has
 * variable that added form the plugin.
 * 
 */ 

var ArrayList = Java.type("java.util.ArrayList");

function splitKeyValueLines(map, maxItemsPerLine) {
	var lines = new ArrayList();
	var keys = Java.from(map.keySet());
	var current = [];
	for (var i = 0; i < keys.length; i++) {
		var key = keys[i];
		var value = map.get(key);
		current.push("<gray>" + key + ": <white>" + value);
		if (current.length === maxItemsPerLine) {
			lines.add(current.join(" <dark_gray>|<dark_gray> "));
			current = [];
		}
	}
	if (current.length > 0) {
		lines.add(current.join(" <dark_gray>|<dark_gray> "));
	}
	return lines;
}

var result = new ArrayList();

if (lore != null) {
	result.add("");
	for (var i = 0; i < lore.size(); i++) {
		var line = lore.get(i);
		result.add("<gray>" + line);
	}
}

for (var i = 0; i < abilities.size(); i++) {
	var ability = abilities.get(i);
	if (ability.get("show_in_lore")) {
		var skill = ability.get("skill");
		var activator = ability.get("activator");
		var variables = ability.get("variable");
		
		result.add("");
		result.add("<color:#4b287d>" + skill + "</color> <dark_gray>- " + activator + "</dark_gray>");
		if (variables != null && !variables.isEmpty()) {
			var wrappedLines = splitKeyValueLines(variables, 3);
			result.addAll(wrappedLines);
		}
	}
}

result;