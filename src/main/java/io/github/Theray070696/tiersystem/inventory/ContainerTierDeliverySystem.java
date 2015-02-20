package io.github.Theray070696.tiersystem.inventory;

import io.github.Theray070696.tiersystem.block.tile.TileTierDeliverySystem;
import io.github.Theray070696.tiersystem.inventory.slot.SlotTierDeliverySystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Theray on 10/20/14.
 */
public class ContainerTierDeliverySystem extends Container
{

    public TileTierDeliverySystem tierDeliverySystem;

    public ContainerTierDeliverySystem(IInventory playerInventory, TileTierDeliverySystem tierDeliverySystem)
    {
        this.tierDeliverySystem = tierDeliverySystem;

        this.addSlotToContainer(new SlotTierDeliverySystem(this.tierDeliverySystem, 0, 10, 31));

        this.addPlayerInventory(playerInventory);
    }

    private void addPlayerInventory(IInventory playerInventory)
    {
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return this.tierDeliverySystem.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();

            if(slotIndex < this.tierDeliverySystem.getSizeInventory())
            {
                if(!this.mergeItemStack(itemStack1, this.tierDeliverySystem.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            } else if(!this.mergeItemStack(itemStack1, 0, this.tierDeliverySystem.getSizeInventory(), false))
            {
                return null;
            }

            if(itemStack1.stackSize == 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }

            slot.onPickupFromSlot(player, itemStack1);
        }

        return itemStack;
    }
}