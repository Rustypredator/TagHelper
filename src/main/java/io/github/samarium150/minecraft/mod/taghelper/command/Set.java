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
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

public final class Set {
    
    private Set() { }
    
    private static int executesHoldingWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag, Tag value)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set")) {
            ItemStack item = CommandUtil.getMainHandItem(source);
            if (item == null) return Command.SINGLE_SUCCESS;
            setTagAndNotify(source, item, tag, value);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesHoldingWithCompound(@Nonnull CommandContext<CommandSourceStack> context, CompoundTag targetNBT)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set")) {
            ItemStack item = CommandUtil.getMainHandItem(source);
            if (item == null) return Command.SINGLE_SUCCESS;
            setTagAndNotify(source, item, targetNBT);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesSlotWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag, Tag value)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set")) {
            int slotId = IntegerArgumentType.getInteger(context, "slot");
            ItemStack item = CommandUtil.getItemFromSlot(source, slotId);
            if (item == null) return Command.SINGLE_SUCCESS;
            setTagAndNotify(source, item, tag, value);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesSlotWithCompound(@Nonnull CommandContext<CommandSourceStack> context, CompoundTag targetNBT)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set")) {
            int slotId = IntegerArgumentType.getInteger(context, "slot");
            ItemStack item = CommandUtil.getItemFromSlot(source, slotId);
            if (item == null) return Command.SINGLE_SUCCESS;
            setTagAndNotify(source, item, targetNBT);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesHotbarWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag, Tag value)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set") && TagHelperConfig.enableHotbarCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processHotbarItems(source, item -> {
                setTagSilently(item, tag, value);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(Component.literal("no items found in hotbar"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(Component.literal("Set tag '" + tag + "' on " + count + " items in hotbar"), false);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesHotbarWithCompound(@Nonnull CommandContext<CommandSourceStack> context, CompoundTag targetNBT)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set") && TagHelperConfig.enableHotbarCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processHotbarItems(source, item -> {
                setTagSilently(item, targetNBT);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(Component.literal("no items found in hotbar"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(Component.literal("Set NBT on " + count + " items in hotbar"), false);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesInventoryWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag, Tag value)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set") && TagHelperConfig.enableInventoryCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processInventoryItems(source, item -> {
                setTagSilently(item, tag, value);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(Component.literal("no items found in inventory"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(Component.literal("Set tag '" + tag + "' on " + count + " items in inventory"), false);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesInventoryWithCompound(@Nonnull CommandContext<CommandSourceStack> context, CompoundTag targetNBT)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set") && TagHelperConfig.enableInventoryCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processInventoryItems(source, item -> {
                setTagSilently(item, targetNBT);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(Component.literal("no items found in inventory"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(Component.literal("Set NBT on " + count + " items in inventory"), false);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesEChestWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag, Tag value)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set") && TagHelperConfig.enableEnderChestCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processEnderChestItems(source, item -> {
                setTagSilently(item, tag, value);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(Component.literal("no items found in ender chest"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(Component.literal("Set tag '" + tag + "' on " + count + " items in ender chest"), false);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesEChestWithCompound(@Nonnull CommandContext<CommandSourceStack> context, CompoundTag targetNBT)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "set") && TagHelperConfig.enableEnderChestCommands.get()) {
            AtomicInteger count = new AtomicInteger(0);
            
            int processed = CommandUtil.processEnderChestItems(source, item -> {
                setTagSilently(item, targetNBT);
                count.incrementAndGet();
            });
            
            if (processed == 0) {
                source.sendFailure(Component.literal("no items found in ender chest"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSuccess(Component.literal("Set NBT on " + count + " items in ender chest"), false);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command!"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static void setTagAndNotify(CommandSourceStack source, ItemStack item, String tag, Tag value) {
        CompoundTag targetNBT = item.getOrCreateTag();
        targetNBT.put(tag, value);
        item.setTag(targetNBT);
        MutableComponent text = Component.literal("current NBT: ")
            .append(targetNBT.toString());
        source.sendSuccess(text, false);
    }
    
    private static void setTagAndNotify(CommandSourceStack source, ItemStack item, CompoundTag targetNBT) {
        item.setTag(targetNBT);
        MutableComponent text = Component.literal("current NBT: ")
            .append(targetNBT.toString());
        source.sendSuccess(text, false);
    }
    
    private static void setTagSilently(ItemStack item, String tag, Tag value) {
        CompoundTag targetNBT = item.getOrCreateTag();
        targetNBT.put(tag, value);
        item.setTag(targetNBT);
    }
    
    private static void setTagSilently(ItemStack item, CompoundTag targetNBT) {
        item.setTag(targetNBT.copy()); // Copy to avoid reference issues
    }
    
    public static void register(@Nonnull CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literal = CommandUtil.literal
            // Holding subcommand
            .then(Commands.literal("holding")
                .then(Commands.literal("set")
                    .then(Commands.argument("key", StringArgumentType.string())
                        .then(Commands.argument("value", NbtTagArgument.nbtTag())
                            .executes(context -> executesHoldingWithTag(context,
                                StringArgumentType.getString(context, "key"),
                                NbtTagArgument.getNbtTag(context, "value"))
                            )
                        )
                    )
                    .then(Commands.argument("NBT", CompoundTagArgument.compoundTag())
                        .executes(context -> executesHoldingWithCompound(context,
                            CompoundTagArgument.getCompoundTag(context, "NBT"))
                        )
                    )
                )
            )
            // Slot subcommand
            .then(Commands.literal("slot")
                .then(Commands.argument("slot", IntegerArgumentType.integer(0, 40))
                    .then(Commands.literal("set")
                        .then(Commands.argument("key", StringArgumentType.string())
                            .then(Commands.argument("value", NbtTagArgument.nbtTag())
                                .executes(context -> executesSlotWithTag(context,
                                    StringArgumentType.getString(context, "key"),
                                    NbtTagArgument.getNbtTag(context, "value"))
                                )
                            )
                        )
                        .then(Commands.argument("NBT", CompoundTagArgument.compoundTag())
                            .executes(context -> executesSlotWithCompound(context,
                                CompoundTagArgument.getCompoundTag(context, "NBT"))
                            )
                        )
                    )
                )
            )
            // Hotbar subcommand
            .then(Commands.literal("hotbar")
                .then(Commands.literal("set")
                    .then(Commands.argument("key", StringArgumentType.string())
                        .then(Commands.argument("value", NbtTagArgument.nbtTag())
                            .executes(context -> executesHotbarWithTag(context,
                                StringArgumentType.getString(context, "key"),
                                NbtTagArgument.getNbtTag(context, "value"))
                            )
                        )
                    )
                    .then(Commands.argument("NBT", CompoundTagArgument.compoundTag())
                        .executes(context -> executesHotbarWithCompound(context,
                            CompoundTagArgument.getCompoundTag(context, "NBT"))
                        )
                    )
                )
            )
            // Inventory subcommand
            .then(Commands.literal("inventory")
                .then(Commands.literal("set")
                    .then(Commands.argument("key", StringArgumentType.string())
                        .then(Commands.argument("value", NbtTagArgument.nbtTag())
                            .executes(context -> executesInventoryWithTag(context,
                                StringArgumentType.getString(context, "key"),
                                NbtTagArgument.getNbtTag(context, "value"))
                            )
                        )
                    )
                    .then(Commands.argument("NBT", CompoundTagArgument.compoundTag())
                        .executes(context -> executesInventoryWithCompound(context,
                            CompoundTagArgument.getCompoundTag(context, "NBT"))
                        )
                    )
                )
            )
            // Ender Chest subcommand
            .then(Commands.literal("echest")
                .then(Commands.literal("set")
                    .then(Commands.argument("key", StringArgumentType.string())
                        .then(Commands.argument("value", NbtTagArgument.nbtTag())
                            .executes(context -> executesEChestWithTag(context,
                                StringArgumentType.getString(context, "key"),
                                NbtTagArgument.getNbtTag(context, "value"))
                            )
                        )
                    )
                    .then(Commands.argument("NBT", CompoundTagArgument.compoundTag())
                        .executes(context -> executesEChestWithCompound(context,
                            CompoundTagArgument.getCompoundTag(context, "NBT"))
                        )
                    )
                )
            );
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(literal);
        dispatcher.register(CommandUtil.alias.redirect(cmd));
    }
}
