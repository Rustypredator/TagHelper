# TagHelper
[![Forge](http://cf.way2muchnoise.eu/560743.svg)](https://www.curseforge.com/minecraft/mc-mods/taghelper)
[![Minecraft](http://cf.way2muchnoise.eu/versions/For%20MC_560743_all.svg)](https://minecraft.fandom.com/wiki)
[![LICENSE](https://img.shields.io/github/license/Rustypredator/TagHelper)](https://github.com/Rustypredator/TagHelper/blob/main/LICENSE)
[![Gradle CI](https://github.com/Rustypredator/TagHelper/actions/workflows/Gradle%20CI.yml/badge.svg)](https://github.com/Rustypredator/TagHelper/actions/workflows/Gradle%20CI.yml)

A Minecraft mod for editing **Named Binary Tags** (NBT) by commands.

> This is a Fork of the [original Mod](https://github.com/Samarium150/TagHelper) by [Samarium_150](https://www.curseforge.com/members/samarium_150).  
> Their mod seems abandoned on Mc 1.18.2.  
> Since i needed the functionality on a newer version of Minecraft, i decided to update it and while at it i am also adding some new functionality.

## Usage

- `/taghelper <selector> get`  
  Get NBT of the item.
- `/taghelper <selector> set <key> <value>`  
  Add or Replace`{key: value}` in NBT of the item.
- `/taghelper <selector> set <NBT>`  
  Set NBT of the item as `<NBT>`
- `/taghelper <selector> remove <key>`  
  Remove `{key: value}` in NBT of the item.
- `/taghelper <selector> remove`  
  Remove all NBT of the item.

`/th` is an alias of `/taghelper`

__Explanation of `<selector>`__:  
The Selector can be one of a few different key words.  
The List below describes how each key word selects items:
- `holding`
  - This Selector targets the item in your main hand.
- `slot <nr>`
  - This Selector targets a specific slot that has to be provided.  
  For Example, `0` Represents the first slot from the left of your hotbar.
- `inventory`
  - This Selector targets your whole inventory (Excluding the hotbar).  
    Meaning it applies the tags you want to set or remove to every single item in your inventory.  
    __Warning__: This is a powerful tool and can seriously mess up your items! use with care!
- `echest`
  - This Selector allows you to target every single item in your Ender Chest inventory.  
  It works the same way as the `inventory` selector.


## Todo:
- [ ] Update to modern MC Variants
  - [ ] 1.19.x
  - [ ] 1.20.x
  - [ ] 1.21.x
- [x] Add More Capabilities
  - [x] Item/Slot Selection
    - [x] `Holding`
    - [x] `Slot <slotnumber>`
    - [x] `Hotbar`
    - [x] `Inv`
    - [x] `Echest`