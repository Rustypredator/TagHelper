package io.github.samarium150.minecraft.mod.taghelper.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class TagHelperConfig {
    
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.BooleanValue enableGetCommand;
    public static final ForgeConfigSpec.BooleanValue enableSetCommand;
    public static final ForgeConfigSpec.BooleanValue enableRemoveCommand;
    public static final ForgeConfigSpec.BooleanValue enableHotbarCommands;
    public static final ForgeConfigSpec.BooleanValue enableInventoryCommands;
    public static final ForgeConfigSpec.BooleanValue enableEnderChestCommands;
    public static final ForgeConfigSpec.BooleanValue usePermissions;
    
    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("configs").push("General");
        enableGetCommand = builder.define("enableGetCommand", true);
        enableSetCommand = builder.define("enableSetCommand", true);
        enableRemoveCommand = builder.define("enableRemoveCommand", true);
        enableHotbarCommands = builder.define("enableHotbarCommands", true);
        enableInventoryCommands = builder.define("enableInventoryCommands", true);
        enableEnderChestCommands = builder.define("enableEnderChestCommands", true);
        usePermissions = builder.comment("If true, uses permission system instead of config values for command access. Permissions: taghelper.get, taghelper.set, taghelper.remove")
                .define("usePermissions", false);
        COMMON_CONFIG = builder.build();
        builder.pop();
    }
}
