package io.github.adainish.argentumpokebuilderfabric;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import io.github.adainish.argentumpokebuilderfabric.cmd.Command;
import io.github.adainish.argentumpokebuilderfabric.config.Config;
import io.github.adainish.argentumpokebuilderfabric.config.LanguageConfig;
import io.github.adainish.argentumpokebuilderfabric.listener.PlayerListener;
import io.github.adainish.argentumpokebuilderfabric.wrapper.DataWrapper;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ArgentumPokeBuilderFabric implements ModInitializer {

    public static ArgentumPokeBuilderFabric instance;
    public static final String MOD_NAME = "ArgentumPokeBuilder";
    public static final String VERSION = "1.0.0-Beta";
    public static final String AUTHORS = "Winglet";
    public static final String YEAR = "2023";
    private static final Logger log = LogManager.getLogger(MOD_NAME);
    private static File configDir;
    private static File storage;
    private static File playerStorageDir;
    private static MinecraftServer server;

    public static DataWrapper dataWrapper;

    public static Config config;

    public static LanguageConfig languageConfig;

    public static PlayerListener playerListener;


    public static Logger getLog() {
        return log;
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static void setServer(MinecraftServer server) {
        ArgentumPokeBuilderFabric.server = server;
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(File configDir) {
        ArgentumPokeBuilderFabric.configDir = configDir;
    }

    public static File getStorage() {
        return storage;
    }

    public static void setStorage(File storage) {
        ArgentumPokeBuilderFabric.storage = storage;
    }

    public static File getPlayerStorageDir() {
        return playerStorageDir;
    }

    public static void setPlayerStorageDir(File playerStorageDir) {
        ArgentumPokeBuilderFabric.playerStorageDir = playerStorageDir;
    }
    @Override
    public void onInitialize() {
        instance = this;
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
        //do data set up
        CobblemonEvents.SERVER_STARTED.subscribe(Priority.NORMAL, minecraftServer -> {
            setServer(minecraftServer);
            //init subscriptions
            playerListener = new PlayerListener();
            dataWrapper = new DataWrapper();
            reload();
            return Unit.INSTANCE;
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Command.getCommand());
        });

        CobblemonEvents.SERVER_STOPPING.subscribe(Priority.NORMAL, minecraftServer -> {
            dataWrapper.playerCache.forEach((uuid, player) -> {
                player.save();
            });
            return Unit.INSTANCE;
        });
    }


    public void initDirs() {
        setConfigDir(new File(FabricLoader.getInstance().getConfigDir() + "/ArgentumPokeBuilder/"));
        getConfigDir().mkdir();
        setStorage(new File(getConfigDir(), "/storage/"));
        getStorage().mkdirs();
        setPlayerStorageDir(new File(storage, "/playerdata/"));
        getPlayerStorageDir().mkdirs();
    }



    public void initConfigs() {
        log.warn("Loading Config Files");

        //write language files then assign them
        LanguageConfig.writeConfig();
        languageConfig = LanguageConfig.getConfig();

        Config.writeConfig();
        config = Config.getConfig();
    }

    public void reload() {
        initDirs();
        initConfigs();

    }
}
