<div align="center">
	<a href="https://www.example.com">Discord</a> |
	<a href="https://www.example.com">GitHub</a> |
	<a href="https://www.example.com">Donation</a>
	<p>&nbsp;</p>
</div>

![Title](https://cdn.modrinth.com/data/cached_images/271c6f9e6fc2b79c986d4c35659e59c23a3d0ab3.png)

---

> [!warning]
> This plugin is in development, if you use this plugin now the risk is on you.

Cast Skill form Plugin [MythicMobs](https://www.example.com), like plugin [MMOItems](https://www.example.com) or [MythicCrucible](https://www.example.com) but with less feature for who need Oraxen/ItemsAdder as item management, if oraxen or ItemsAdder has custom nbt modifier mybe you can make item that cast skill with only nbt because focus this plugin is use nbt as data for cast skill.

## Resource Links:
- Modrith
- SpigotMC
- <svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>BuiltByBit</title><path d="M11.877.032 1.252 5.885a.253.253 0 0 0 .003.446l5.679 3.02c.077.041.17.04.246-.004l4.694-2.697a.254.254 0 0 1 .253 0l4.692 2.697a.253.253 0 0 0 .246.004l5.682-3.021a.253.253 0 0 0 .003-.446L12.122.031a.254.254 0 0 0-.245 0ZM6.924 10.898l-5.71-3.036a.254.254 0 0 0-.373.224V18.25c0 .093.05.178.131.222l9.976 5.495a.254.254 0 0 0 .376-.222v-6.053a.255.255 0 0 0-.127-.22l-4.012-2.305a.252.252 0 0 1-.127-.22v-3.825a.253.253 0 0 0-.135-.224Zm10.152 0 5.71-3.035a.254.254 0 0 1 .373.224v10.164c0 .093-.05.178-.131.222l-9.976 5.495a.254.254 0 0 1-.376-.222v-6.053c0-.091.049-.175.127-.22l4.012-2.305a.252.252 0 0 0 .127-.22v-3.825c0-.094.052-.18.135-.224Z"/></svg>

## Main Feature:
- Make datapack can use cast Skill with item.
- Support Oraxen/ItemsAdder or other plugin that use nbt as modifier item.
- Flexible for other plugin, maybe will little bit break if another plugin using nbt `Abilities` but i will add configuration for that.

## How to install:
- Download this plugin in this [Link](https://www.example.com) or Packages below.
- Open your server panel.
- Upload or Drag & Drop plugin file to `./plugins`

## How to Use plugin:
### In-game Nbt

Lower version of 1.20.4:
```
/give @s diamond_sword{ Abilities: [ { skill: "SmashAttack", action: "right_click" }, { skill: "regeneration", timer: 1 } ] }
```

Json like format
```
{
  Abilities: [
    {
      skill: "SmashAttack",
      action: "right_click"
    },
    {
      skill: "regeneration",
      timer: 1
    }
  ]
}
```

### Nbt Format

### Action/Event activator skill

### Condition nbt
In basic MMOItems or MythicCrucible you use condition skill on mythicmobs, but mythicmobs condition mechanic is has something lost and i want to add it like condition when attack cooldown player is not over will not cast.


### Oraxen/ItemsaAdder support