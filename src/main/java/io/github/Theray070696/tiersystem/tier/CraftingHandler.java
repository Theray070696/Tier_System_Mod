package io.github.Theray070696.tiersystem.tier;

import net.minecraft.inventory.IInventory;

/**
 * Created by Theray on 10/9/14.
 */
public class CraftingHandler
{

    public static void setSlotToNull(IInventory inventory, int slot)
    {
        inventory.setInventorySlotContents(slot, null);
    }
}