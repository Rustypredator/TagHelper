package io.github.samarium150.minecraft.mod.taghelper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.samarium150.minecraft.mod.taghelper.config.TagHelperConfig;
import io.github.samarium150.minecraft.mod.taghelper.permission.PermissionManager;
import io.github.samarium150.minecraft.mod.taghelper.util.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

public final class Remove {
    
    private Remove() { }
    
    private static int executesHoldingWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove")) {
            ItemStack item = CommandUtil.getMainHandItem(source);
            if (item == null) return Command.SINGLE_SUCCESS;
            removeTagAndNotify(source, item, tag);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesHolding(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove")) {
            ItemStack item = CommandUtil.getMainHandItem(source);
            if (item == null) return Command.SINGLE_SUCCESS;
            removeAllTagsAndNotify(source, item);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesSlotWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove")) {
            int slotId = IntegerArgumentType.getInteger(context, "slot");
            ItemStack item = CommandUtil.getItemFromSlot(source, slotId);
            if (item == null) return Command.SINGLE_SUCCESS;
            removeTagAndNotify(source, item, tag);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesSlot(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove")) {
            int slotId = IntegerArgumentType.getInteger(context, "slot");
            ItemStack item = CommandUtil.getItemFromSlot(source, slotId);
            if (item == null) return Command.SINGLE_SUCCESS;
            removeAllTagsAndNotify(source, item);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesHotbarWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove") && TagHelperConfig.enableHotbarCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processHotbarItems(source, item -> {
                if (removeTagSilently(item, tag)) {
                    count.incrementAndGet();
                }
            });
            
            if (processed == 0) {
                source.sendFailure(new TextComponent("no items found in hotbar"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(new TextComponent("Removed tag '" + tag + "' from " + count + " items in hotbar"), false);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesHotbar(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove") && TagHelperConfig.enableHotbarCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processHotbarItems(source, item -> {
                removeAllTagsSilently(item);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(new TextComponent("no items found in hotbar"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(new TextComponent("Removed all NBT from " + count + " items in hotbar"), false);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesInventoryWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove") && TagHelperConfig.enableInventoryCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processInventoryItems(source, item -> {
                if (removeTagSilently(item, tag)) {
                    count.incrementAndGet();
                }
            });
            
            if (processed == 0) {
                source.sendFailure(new TextComponent("no items found in inventory"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(new TextComponent("Removed tag '" + tag + "' from " + count + " items in inventory"), false);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesInventory(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove") && TagHelperConfig.enableInventoryCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processInventoryItems(source, item -> {
                removeAllTagsSilently(item);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(new TextComponent("no items found in inventory"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(new TextComponent("Removed all NBT from " + count + " items in inventory"), false);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesEChestWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove") && TagHelperConfig.enableEnderChestCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processEnderChestItems(source, item -> {
                if (removeTagSilently(item, tag)) {
                    count.incrementAndGet();
                }
            });
            
            if (processed == 0) {
                source.sendFailure(new TextComponent("no items found in ender chest"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(new TextComponent("Removed tag '" + tag + "' from " + count + " items in ender chest"), false);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesEChest(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "remove") && TagHelperConfig.enableEnderChestCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processEnderChestItems(source, item -> {
                removeAllTagsSilently(item);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(new TextComponent("no items found in ender chest"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(new TextComponent("Removed all NBT from " + count + " items in ender chest"), false);
        } else
            source.sendFailure(new TextComponent("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static void removeTagAndNotify(CommandSourceStack source, ItemStack item, String tag) {
        CompoundTag targetNBT = item.getTag();
        if (targetNBT == null) return;
        targetNBT.remove(tag);
        item.setTag(targetNBT);
        MutableComponent text = new TextComponent("current NBT: ")
            .append(targetNBT.toString());
        source.sendSuccess(text, false);
    }
    
    private static void removeAllTagsAndNotify(CommandSourceStack source, ItemStack item) {
        item.setTag(null);
        source.sendSuccess(new TextComponent("NBT is removed"), false);
    }
    
    /**
     * Removes a tag from an item silently (without notification)
     * @param item The item to remove the tag from
     * @param tag The tag to remove
     * @return true if the tag was removed, false if it didn't exist
     */
    private static boolean removeTagSilently(ItemStack item, String tag) {
        CompoundTag targetNBT = item.getTag();
        if (targetNBT == null) return false;
        if (!targetNBT.contains(tag)) return false;
        targetNBT.remove(tag);
        item.setTag(targetNBT);
        return true;
    }
    
    private static void removeAllTagsSilently(ItemStack item) {
        item.setTag(null);
    }
    
    public static void register(@Nonnull CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literal = CommandUtil.literal
            // Holding subcommand
            .then(Commands.literal("holding")
                .then(Commands.literal("remove")
                    .then(Commands.argument("key", StringArgumentType.string())
                        .executes(context -> executesHoldingWithTag(context,
                            StringArgumentType.getString(context, "key")))
                    )
                    .executes(Remove::executesHolding)
                )
            )
            // Slot subcommand
            .then(Commands.literal("slot")
                .then(Commands.argument("slot", IntegerArgumentType.integer(0, 40))
                    .then(Commands.literal("remove")
                        .then(Commands.argument("key", StringArgumentType.string())
                            .executes(context -> executesSlotWithTag(context,
                                StringArgumentType.getString(context, "key")))
                        )
                        .executes(Remove::executesSlot)
                    )
                )
            )
            // Hotbar subcommand
            .then(Commands.literal("hotbar")
                .then(Commands.literal("remove")
                    .then(Commands.argument("key", StringArgumentType.string())
                        .executes(context -> executesHotbarWithTag(context,
                            StringArgumentType.getString(context, "key")))
                    )
                    .executes(Remove::executesHotbar)
                )
            )
            // Inventory subcommand
            .then(Commands.literal("inventory")
                .then(Commands.literal("remove")
                    .then(Commands.argument("key", StringArgumentType.string())
                        .executes(context -> executesInventoryWithTag(context,
                            StringArgumentType.getString(context, "key")))
                    )
                    .executes(Remove::executesInventory)
                )
            )
            // Ender Chest subcommand
            .then(Commands.literal("echest")
                .then(Commands.literal("remove")
                    .then(Commands.argument("key", StringArgumentType.string())
                        .executes(context -> executesEChestWithTag(context,
                            StringArgumentType.getString(context, "key")))
                    )
                    .executes(Remove::executesEChest)
                )
            );
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(literal);
        dispatcher.register(CommandUtil.alias.redirect(cmd));
    }
}
