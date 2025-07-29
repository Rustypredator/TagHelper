# TagHelper
[![Forge](http://cf.way2muchnoise.eu/560743.svg)](https://www.curseforge.com/minecraft/mc-mods/taghelper)
[![Minecraft](http://cf.way2muchnoise.eu/versions/For%20MC_560743_all.svg)](https://minecraft.fandom.com/wiki)
[![LICENSE](https://img.shields.io/github/license/Samarium150/TagHelper)](https://github.com/Samarium150/TagHelper/blob/main/LICENSE)
[![Gradle CI](https://github.com/Samarium150/TagHelper/actions/workflows/Gradle%20CI.yml/badge.svg)](https://github.com/Samarium150/TagHelper/actions/workflows/Gradle%20CI.yml)

A Minecraft mod for editing **Named Binary Tags** (NBT) by commands.

## Usage

- `/taghelper get`  
  Get NBT of the item on the main hand.
- `/taghelper set <key> <value>`  
  Add or Replace`{key: value}` in NBT of the item on the main hand.
- `/taghelper set <NBT>`  
  Set NBT of the item on the main hand as `<NBT>`
- `/taghelper remove <key>`  
  Remove `{key: value}` in NBT of the item on the main hand.
- `/taghelper remove`  
  Remove NBT of the item on the main hand.

`/th` is an alias of `/taghelper`


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