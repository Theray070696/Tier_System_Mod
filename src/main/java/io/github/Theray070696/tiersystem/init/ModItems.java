package io.github.Theray070696.tiersystem.init;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.Theray070696.tiersystem.item.ItemTierSystem;
import io.github.Theray070696.tiersystem.item.ItemTierUp;

/**
 * Created by Theray on 10/10/14.
 */
public class ModItems
{

    public static final ItemTierSystem tierUp = new ItemTierUp();

    public static void loadItems()
    {
        GameRegistry.registerItem(tierUp, "tierUp");
    }
}