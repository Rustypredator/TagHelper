package io.github.samarium150.minecraft.mod.taghelper.init;

import com.mojang.brigadier.CommandDispatcher;
import io.github.samarium150.minecraft.mod.taghelper.command.Get;
import io.github.samarium150.minecraft.mod.taghelper.command.Remove;
import io.github.samarium150.minecraft.mod.taghelper.command.Set;
import io.github.samarium150.minecraft.mod.taghelper.util.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public final class CommandRegistry {
    
    private CommandRegistry() { }
    
    @SubscribeEvent
    public static void register(@Nonnull final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        // Register commands with new structure
        Get.register(dispatcher);
        Set.register(dispatcher);
        Remove.register(dispatcher);
    }
}
