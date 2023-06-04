package io.github.adainish.argentumpokebuilderfabric.obj;

import io.github.adainish.argentumpokebuilderfabric.ArgentumPokeBuilderFabric;
import io.github.adainish.argentumpokebuilderfabric.storage.PlayerStorage;
import io.github.adainish.argentumpokebuilderfabric.util.Util;

import java.util.UUID;

public class Player
{
    public UUID uuid;
    public int tokenCount;

    public Player(UUID uuid)
    {
        this.uuid = uuid;
        this.tokenCount = 0;
    }


    public void updateCache() {
        ArgentumPokeBuilderFabric.dataWrapper.playerCache.put(uuid, this);
    }

    public void save() {
        //save to storage file
        PlayerStorage.savePlayer(this);
    }

    public void sendMessage(String message)
    {
        Util.send(Util.getPlayer(uuid), message);
    }


}
