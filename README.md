<div align="center">
	<a href="https://www.example.com">Discord</a> |
	<a href="https://www.example.com">GitHub</a> |
	<a href="https://www.example.com">Donation</a>
	<p>&nbsp;</p>
</div>

![Title](https://cdn.modrinth.com/data/cached_images/271c6f9e6fc2b79c986d4c35659e59c23a3d0ab3.png)

<div align="center">
	<a href="">
		<img alt="" src=https://img.shields.io/badge/builtbybit-%232D87C3?style=for-the-badge&logo=builtbybit&logoColor=white>
	</a>
	<a href="">
		<img alt="" src=https://img.shields.io/badge/SpigotMC-%23ED8106?style=for-the-badge&logo=spigotmc&logoColor=white>
	</a>
	<a href="">
		<img alt="" src=https://img.shields.io/badge/Modrinth-%2300AF5C?style=for-the-badge&logo=modrinth&logoColor=white>
	</a>
</div>

---

[![Java CI with Maven](https://github.com/Phanisment/Item-Nbt-Skill-Cast/actions/workflows/maven.yml/badge.svg)](https://github.com/Phanisment/Item-Nbt-Skill-Cast/actions/workflows/maven.yml) [![Test Code](https://github.com/Phanisment/Item-Nbt-Skill-Cast/actions/workflows/test.yml/badge.svg)](https://github.com/Phanisment/Item-Nbt-Skill-Cast/actions/workflows/test.yml)

> [!warning]
> This plugin is in development, if you use this plugin now the risk is on you.

Cast Skill form Plugin [MythicMobs](https://www.example.com), like plugin [MMOItems](https://www.example.com) or [MythicCrucible](https://www.example.com) but with less feature for who need Oraxen/ItemsAdder as item management, if oraxen or ItemsAdder has custom nbt modifier mybe you can make item that cast skill with only nbt because focus this plugin is use nbt as data for cast skill.


## Main Feature:
- Make datapack can Cast Skill with item.
- Support Oraxen/ItemsAdder or other plugin that use nbt as modifier item.
- Flexible for other plugin, maybe will little bit break if another plugin using nbt `Abilities` but i will add configuration for that.

## How to install:
- Download this plugin in this [Link](https://www.example.com) or Packages below.
- Open your server panel.
- Upload or Drag & Drop plugin file to `./plugins`.
- Install dependency plugin MythicMobs in your server.
- Start the server

## How to Use plugin:
### In-game Nbt

← 1.20.4:
```
/give @s diamond_sword{ Abilities: [ { skill: "SmashAttack", action: "right_click" }, { skill: "regeneration", timer: 1 } ] }
```

1.20.5 →:
```
/give @s diamond_sword[ { Abilities: [ { skill: "SmashAttack", action: "right_click" }, { skill: "regeneration", timer: 1 } ] } ]
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

## Contribution:
If someone contribute in this project i will very heppy for that. To contribute:
1. Fork this project.
2. Create new branch for your code changes.
3. Submit pull request with clear description of the changes.

## Social
If you have questions or suggestions just tag me on discord, im always online on discord(if im not busy).