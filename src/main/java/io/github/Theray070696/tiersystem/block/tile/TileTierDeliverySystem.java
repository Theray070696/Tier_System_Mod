package io.github.Theray070696.tiersystem.block.tile;

import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Theray on 10/20/14.
 */
public class TileTierDeliverySystem extends TileTierSystem implements IInventory
{

    private EntityPlayer owner;
    private ItemStack[] inventory = new ItemStack[1];

    @Override
    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return this.inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount)
    {
        ItemStack itemStack = this.getStackInSlot(slotIndex);
        if(itemStack != null)
        {
            if(itemStack.stackSize <= decrementAmount)
            {
                this.setInventorySlotContents(slotIndex, null);
            } else
            {
                itemStack = itemStack.splitStack(decrementAmount);
                if(itemStack.stackSize == 0)
                {
                    this.setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        ItemStack itemStack = this.getStackInSlot(slotIndex);
        if(itemStack != null)
        {
            this.setInventorySlotContents(slotIndex, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        this.inventory[slotIndex] = itemStack;
        if(itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
        {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return "container.tierDeliverySystem";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.owner == null || player.getCommandSenderName().equalsIgnoreCase(this.getOwner().getCommandSenderName());
    }

    @Override
    public void openInventory() {} // Not needed

    @Override
    public void closeInventory() {} // Not needed

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        NBTTagList nbtTagList = nbtTagCompound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for(int i = 0; i < nbtTagList.tagCount(); ++i)
        {
            NBTTagCompound nbtTagCompound1 = nbtTagList.getCompoundTagAt(i);
            int j = nbtTagCompound1.getByte("Slot") & 255;

            if(j >= 0 && j < this.inventory.length)
            {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbtTagCompound1);
            }
        }

        if(nbtTagCompound.hasKey("Owner"))
        {
            String ownerName = nbtTagCompound.getString("Owner");
            if(this.worldObj != null && this.worldObj.playerEntities != null)
            {
                for(Object object : this.worldObj.playerEntities)
                {
                    if(object != null && object instanceof EntityPlayer)
                    {
                        EntityPlayer player = (EntityPlayer) object;

                        if(player.getCommandSenderName().equalsIgnoreCase(ownerName))
                        {
                            this.setOwner(player);

                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        NBTTagList nbtTagList = new NBTTagList();

        for(int i = 0; i < this.inventory.length; ++i)
        {
            if(this.inventory[i] != null)
            {
                NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
                nbtTagCompound1.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbtTagCompound1);
                nbtTagList.appendTag(nbtTagCompound1);
            }
        }

        nbtTagCompound.setTag("Items", nbtTagList);

        if(this.getOwner() != null)
        {
            nbtTagCompound.setString("Owner", this.getOwner().getCommandSenderName());
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack)
    {
        return true;
    }

    public EntityPlayer getOwner()
    {
        return this.owner;
    }

    public void setOwner(EntityPlayer owner)
    {
        this.owner = owner;
    }

    public void updateEntity()
    {
        if(!this.worldObj.isRemote)
        {
            if(ConfigCore.itemsToTurnInForNextTier.length > 0 && this.getOwner() != null && this.getStackInSlot(0) != null && !ConfigCore.itemsToTurnInForNextTier[TierHandler.getCurrentTierForPlayer(this.getOwner())].equals(""))
            {
                String itemToTurnIn = Item.itemRegistry.getNameForObject(this.getStackInSlot(0).getItem());
                int amountLeft = TierHandler.addItemTurnedInForPlayer(this.getOwner(), itemToTurnIn, this.getStackInSlot(0).getItemDamage(), this.getStackInSlot(0).stackSize);
                ItemStack stack = this.getStackInSlot(0).copy();

                if(amountLeft != 0)
                {
                    stack.stackSize = amountLeft;
                } else
                {
                    stack = null;
                }

                this.setInventorySlotContents(0, stack);
            }
        }
    }
}