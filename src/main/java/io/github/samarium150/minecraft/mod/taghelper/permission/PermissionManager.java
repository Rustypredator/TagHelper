package io.github.samarium150.minecraft.mod.taghelper.permission;

import io.github.samarium150.minecraft.mod.taghelper.config.TagHelperConfig;
import io.github.samarium150.minecraft.mod.taghelper.util.GeneralUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;

/**
 * Permission manager for TagHelper commands
 * Supports permission plugins like LuckPerms with permission nodes
 * taghelper.get, taghelper.set, taghelper.remove
 * Falls back to vanilla permission levels if no permission plugin is present
 */
public final class PermissionManager {

    private static final String PERMISSION_PREFIX = GeneralUtil.MOD_ID + ".";
    
    // Permission nodes
    public static final String GET_PERMISSION = PERMISSION_PREFIX + "get";
    public static final String SET_PERMISSION = PERMISSION_PREFIX + "set";
    public static final String REMOVE_PERMISSION = PERMISSION_PREFIX + "remove";
    
    // These are internal vanilla permission levels as fallback
    private static final int GET_PERMISSION_LEVEL = 0; // Everyone can use get command
    private static final int SET_PERMISSION_LEVEL = 2; // Operator level for set command
    private static final int REMOVE_PERMISSION_LEVEL = 2; // Operator level for remove command

    private PermissionManager() { }

    /**
     * Check if the command source has permission to use a command
     * First checks permission node if available, then falls back to config and vanilla permission
     * 
     * @param source The command source
     * @param permission The permission node to check (without prefix)
     * @return true if the source has permission, false otherwise
     */
    public static boolean hasPermission(@Nonnull CommandSourceStack source, @Nonnull String permission) {
        // If permissions are disabled in config, use the config toggle
        if (!TagHelperConfig.usePermissions.get()) {
            return isEnabledInConfig(permission);
        }
        
        // Try to check against permission system if player
        if (source.getEntity() instanceof ServerPlayer player) {
            String permNode = PERMISSION_PREFIX + permission;
            
            // This function doesn't exist in vanilla but would in Bukkit/Spigot/Forge with permission mods
            // First try to use the hasPermission method that permission plugins would add
            try {
                // Using reflection to check if the hasPermission method exists and call it
                // This would catch and handle cases where different permission systems are used
                java.lang.reflect.Method hasPermMethod = player.getClass().getMethod("hasPermission", String.class);
                if (hasPermMethod != null) {
                    return (boolean) hasPermMethod.invoke(player, permNode);
                }
            } catch (Exception ignored) {
                // Method doesn't exist or reflection failed, falling back to vanilla permissions
            }
        }
        
        // Fall back to vanilla permission levels if no permission plugin found
        return source.hasPermission(getRequiredPermissionLevel(permission));
    }
    
    /**
     * Check if a command is enabled in the config
     * Used as fallback when permissions are disabled
     * 
     * @param permission The permission/command to check
     * @return true if enabled in config, false otherwise
     */
    private static boolean isEnabledInConfig(@Nonnull String permission) {
        return switch (permission) {
            case "get" -> TagHelperConfig.enableGetCommand.get();
            case "set" -> TagHelperConfig.enableSetCommand.get();
            case "remove" -> TagHelperConfig.enableRemoveCommand.get();
            default -> false;
        };
    }

    /**
     * Get the required vanilla permission level for a specific permission
     * Used as fallback when no permission plugin is found
     * 
     * @param permission The permission to check
     * @return The required permission level (0-4)
     */
    private static int getRequiredPermissionLevel(@Nonnull String permission) {
        return switch (permission) {
            case "get" -> GET_PERMISSION_LEVEL;
            case "set" -> SET_PERMISSION_LEVEL;
            case "remove" -> REMOVE_PERMISSION_LEVEL;
            default -> 2; // Default to operator level
        };
    }
}