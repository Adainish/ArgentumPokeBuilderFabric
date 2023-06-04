package io.github.adainish.argentumpokebuilderfabric.cmd;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.adainish.argentumpokebuilderfabric.ArgentumPokeBuilderFabric;
import io.github.adainish.argentumpokebuilderfabric.obj.Player;
import io.github.adainish.argentumpokebuilderfabric.obj.PokeBuilder;
import io.github.adainish.argentumpokebuilderfabric.storage.PlayerStorage;
import io.github.adainish.argentumpokebuilderfabric.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class Command
{
    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("pokebuilder")
                .executes(cc -> {
                    try {
                        Player player = PlayerStorage.getPlayer(cc.getSource().getPlayerOrException().getUUID());
                        if (player != null) {
                            PokeBuilder pokeBuilder = new PokeBuilder(player);
                            //open pokebuilder GUI
                            pokeBuilder.open(cc.getSource().getPlayerOrException());
                        } else {
                            Util.send(cc.getSource(), "&cUnable to load your pokebuilder data...");
                        }
                    } catch (Exception e) {

                    }
                    return 1;
                })
                .then(Commands.literal("reload")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                        .executes(cc -> {
                            ArgentumPokeBuilderFabric.instance.reload();
                            Util.send(cc.getSource(), "&eReloaded argentum pokebuilder, please check the console for any errors.");
                            return 1;
                        })
                )
                ;

    }
}
