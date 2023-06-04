package io.github.adainish.argentumpokebuilderfabric.managers;

import com.cobblemon.mod.common.CobblemonItems;
import io.github.adainish.argentumpokebuilderfabric.enumerations.BuilderType;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class PokeBuilderDataManager
{
    public HashMap<BuilderType, Integer> baseAttributeCosts = new HashMap<>();
    public HashMap<BuilderType, ItemStack> attributeIcons = new HashMap<>();

    public PokeBuilderDataManager()
    {
        init();
    }

    public void init()
    {
        if (baseAttributeCosts.isEmpty()) {
            for (BuilderType builderType : BuilderType.values()) {
                if (builderType.equals(BuilderType.UNDECIDED))
                    continue;
                baseAttributeCosts.put(builderType, 100);
            }
        }
        if (attributeIcons.isEmpty())
        {
            for (BuilderType builderType : BuilderType.values()) {
                if (builderType.equals(BuilderType.UNDECIDED))
                    continue;
                attributeIcons.put(builderType, new ItemStack(CobblemonItems.POKE_BALL.get()));
            }
        }
    }
}
