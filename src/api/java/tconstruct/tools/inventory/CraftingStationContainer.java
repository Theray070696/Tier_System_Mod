package tconstruct.tools.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;

/**
 * This class is used for the TinkersConstruct plugin.
 * It is just an empty class that is used to grab the fields
 * that are needed for blocking crafting in the Crafting Station.
 */
public class CraftingStationContainer extends Container
{

    /**
     * The crafting matrix inventory (3x3).
     */
    public InventoryCrafting craftMatrix;// = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult;// = new InventoryCraftResult();

    public CraftingStationContainer() {}

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}