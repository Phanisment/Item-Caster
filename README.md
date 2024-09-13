<div align="center">
	<a href="https://discord.gg/grJCgRHKvg">Discord</a> |
	<a href="https://github.com/Phanisment/Item-Nbt-Skill-Cast">GitHub</a> |
	<a href="https://buymeacoffee.com/Phanisment">Donation</a>
	<p>&nbsp;</p>
</div>

![Title](https://cdn.modrinth.com/data/cached_images/271c6f9e6fc2b79c986d4c35659e59c23a3d0ab3.png)

<div align="center">
	<a href="">
		<img alt="SpigotMC" src=https://img.shields.io/badge/SpigotMC-%23ED8106?style=for-the-badge&logo=spigotmc&logoColor=white>
	</a>
	<a href="https://modrinth.com/project/item-caster">
		<img alt="Modrinth" src=https://img.shields.io/badge/Modrinth-%2300AF5C?style=for-the-badge&logo=modrinth&logoColor=white>
	</a>
</div>

---

> [!NOTE]
> This plugin documentation is not complete yet.

Cast Skill form Plugin MythicMobs, like plugin MMOItems or MythicCrucible but with less feature for who need Oraxen/ItemsAdder as item management, if oraxen or ItemsAdder has custom nbt modifier mybe you can make item that cast skill with only nbt because focus this plugin is use nbt as data for cast skill.

## Main Feature:
- Make datapack can Cast Skill with item.
- Support Oraxen/ItemsAdder or other plugin that use nbt as modifier item.
- Flexible for other plugin.

## How to install:
- Download this plugin in this [Link](https://www.example.com) or Packages below.
- Open your server panel.
- Upload or Drag & Drop plugin file to `./plugins`.
- Install dependency plugin [MythicMobs](https://modrinth.com/plugin/mythicmobs) in your server.
- Start the server

## How to Use plugin:
### In-game Nbt

← 1.20.4:
```
/give @s diamond_sword{ Abilities: [ { skill: "SmashAttack", action: "right_click" }, { skill: "regeneration", timer: 1 } ] }
```

<details open>
	<summary>Json like format</summary>

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

</details>

### Nbt Format
- `{skill:"<Skill ID>"}`
Id skill on MythicMobs

- `{action:"<Event>"}`
Skill Executor
  Event, this is list event for now Next update i will add more than this.
  - right_click
  - left_click

- `{timer:<Delay>}`
Will Cast Skill while player hold item with nbt skill


### Action/Event activator skill

Event can't use combination with timer, if you try it, the item nbt will not cast skill.

|Events|Descriptions|
|---|---|
|`right_click`|Cast skill when right click|
|`left_click`|Cast skill when left click|


### Condition nbt
> [!TIP]
> Will added in future

In basic MMOItems or MythicCrucible you use condition skill on mythicmobs, but mythicmobs condition mechanic is has something lost and i want to add it like condition when attack cooldown player is not over will not cast.


### Oraxen/ItemsaAdder support

## Contribution:
If someone contribute in this project i will very heppy for that. To contribute:
1. Fork this project.
2. Create new branch for your code changes.
3. Submit pull request with clear description of the changes.

## Credits
If you have questions or suggestions just tag me on discord, im always online on discord(if im not busy).
