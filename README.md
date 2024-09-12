<div align="center">
	<a href="https://www.example.com">Discord</a> |
	<a href="https://www.example.com">GitHub</a> |
	<a href="https://www.example.com">Donation</a>
	<p>&nbsp;</p>
</div>

![Title](https://cdn.modrinth.com/data/cached_images/271c6f9e6fc2b79c986d4c35659e59c23a3d0ab3.png)

---

Cast Skill form Plugin [MythicMobs](https://www.example.com), like plugin [MMOItems](https://www.example.com) or [MythicCrucible](https://www.example.com) but with less feature for who need Oraxen/ItemsAdder as item management, if oraxen or ItemsAdder has custom nbt modifier mybe you can make item that cast skill with only nbt because focus this plugin is use nbt as data for cast skill.

## Main Feature:
- Make datapack can use cast Skill with item.
- Support Oraxen/ItemsAdder or other plugin that use nbt as modifier item.
- Flexible for other plugin, maybe will little bit break if another plugin using nbt `Abilities` but i will add configuration for that.

## How to install:
- Download this plugin in this [Link](https://www.example.com) or Packages below.
- Open your server panel.
- Upload or Drag & Drop plugin file to `./plugins`

## How to Use plugin:
Give command

Lower version of 1.20.4:
```
/give @s diamond_sword{ Abilities: [ { skill: "SmashAttack", action: "right_click" }, { skill: "regeneration", timer: 1 } ] }
```

Json like format
```json
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