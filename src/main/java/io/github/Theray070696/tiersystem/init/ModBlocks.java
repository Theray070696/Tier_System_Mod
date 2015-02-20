package io.github.Theray070696.tiersystem.init;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.Theray070696.tiersystem.block.BlockTierDeliverySystem;
import io.github.Theray070696.tiersystem.block.BlockTierSystem;
import io.github.Theray070696.tiersystem.block.tile.TileTierDeliverySystem;

/**
 * Created by Theray on 10/10/14.
 */
public class ModBlocks
{

    public static final BlockTierSystem tierDeliverySystem = new BlockTierDeliverySystem();

    public static void loadBlocks()
    {
        GameRegistry.registerBlock(tierDeliverySystem, "tierDeliverySystem");
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileTierDeliverySystem.class, "tierDeliverySystem");
    }
}