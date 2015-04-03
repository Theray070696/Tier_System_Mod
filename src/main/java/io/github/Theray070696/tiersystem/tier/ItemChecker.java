package io.github.Theray070696.tiersystem.tier;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Theray on 4/2/15.
 */
public class ItemChecker
{

    public static void checkForBlockedItems(TickEvent.PlayerTickEvent event)
    {
        if(ConfigCore.tierCount > 0 && event.player != null)
        {
            EntityPlayer player = event.player;

            InventoryPlayer playerInventory = player.inventory;

            for(int slot = 0; slot < playerInventory.getSizeInventory() - 1; slot++)
            {
                ItemStack stack = playerInventory.getStackInSlot(slot);

                if(stack != null && !Item.itemRegistry.getNameForObject(stack.getItem()).equals(""))
                {
                    String itemName = Item.itemRegistry.getNameForObject(stack.getItem());
                    for(int j = 0; j < ConfigCore.tierRestrictions.length; j++)
                    {
                        if(j >= TierHandler.getCurrentTierForPlayer(player))
                        {
                            if(ConfigCore.tierRestrictions[j].contains(","))
                            {
                                String[] restrictions = ConfigCore.tierRestrictions[j].split(",");
                                for(int k = 0; k < restrictions.length; k++)
                                {
                                    String restriction = restrictions[k];
                                    String metadataString = "";

                                    if(restriction.contains(" "))
                                    {
                                        String[] nameAndMeta = restriction.split(" ", 2);

                                        restriction = nameAndMeta[0];
                                        metadataString = nameAndMeta[1];
                                    }

                                    int metadata;

                                    if(!metadataString.equals(""))
                                    {
                                        metadata = Integer.parseInt(metadataString);
                                    } else
                                    {
                                        metadata = 0;
                                    }

                                    if(restriction.equalsIgnoreCase(itemName) && (metadata == -1 || stack.getItemDamage() == metadata))
                                    {
                                        // Save item to ItemStack
                                        // Switch item in slot to ItemBlocked
                                        // Save ItemStack to ItemBlocked

                                        ItemStack oldItemStack = stack;
                                        ItemStack newItemStack = new ItemStack(ModItems.blockedItem);
                                        NBTTagCompound oldItemData = new NBTTagCompound();

                                        oldItemData = oldItemStack.writeToNBT(oldItemData);

                                        newItemStack.stackTagCompound = new NBTTagCompound();
                                        newItemStack.stackTagCompound.setTag("TierSystem:BlockedItem", oldItemData);

                                        playerInventory.setInventorySlotContents(slot, newItemStack);

                                        return;
                                    }
                                }
                            } else if(!ConfigCore.tierRestrictions[j].contains(",") && !ConfigCore.tierRestrictions[j].equals(""))
                            {
                                String restriction = ConfigCore.tierRestrictions[j];
                                String metadataString = "";

                                if(restriction.contains(" "))
                                {
                                    String[] nameAndMeta = restriction.split(" ", 2);

                                    restriction = nameAndMeta[0];
                                    metadataString = nameAndMeta[1];
                                }

                                int metadata;

                                if(!metadataString.equals(""))
                                {
                                    metadata = Integer.parseInt(metadataString);
                                } else
                                {
                                    metadata = 0;
                                }

                                if(restriction.equalsIgnoreCase(itemName) && (metadata == -1 || stack.getItemDamage() == metadata))
                                {
                                    // Save item to ItemStack
                                    // Switch item in slot to ItemBlocked
                                    // Save ItemStack to ItemBlocked

                                    ItemStack oldItemStack = stack;
                                    ItemStack newItemStack = new ItemStack(ModItems.blockedItem);
                                    NBTTagCompound oldItemData = new NBTTagCompound();

                                    oldItemData = oldItemStack.writeToNBT(oldItemData);

                                    newItemStack.stackTagCompound = new NBTTagCompound();
                                    newItemStack.stackTagCompound.setTag("TierSystem:BlockedItem", oldItemData);

                                    playerInventory.setInventorySlotContents(slot, newItemStack);

                                    return;
                                }
                            }
                        }
                    }

                    if(ConfigCore.blockMods)
                    {
                        for(int j = 0; j < ConfigCore.modsBlocked.length; j++)
                        {
                            if(j >= TierHandler.getCurrentTierForPlayer(player))
                            {
                                if(ConfigCore.modsBlocked[j].contains(","))
                                {
                                    String[] modIDs = ConfigCore.modsBlocked[j].split(",");
                                    for(int k = 0; k < modIDs.length; k++)
                                    {
                                        String modID = modIDs[k];
                                        GameRegistry.UniqueIdentifier mod = GameRegistry.findUniqueIdentifierFor(stack.getItem());

                                        if(mod != null)
                                        {
                                            if(modID.equalsIgnoreCase(mod.modId))
                                            {
                                                // Save item to ItemStack
                                                // Switch item in slot to ItemBlocked
                                                // Save ItemStack to ItemBlocked

                                                ItemStack oldItemStack = stack;
                                                ItemStack newItemStack = new ItemStack(ModItems.blockedItem);
                                                NBTTagCompound oldItemData = new NBTTagCompound();

                                                oldItemData = oldItemStack.writeToNBT(oldItemData);

                                                newItemStack.stackTagCompound = new NBTTagCompound();
                                                newItemStack.stackTagCompound.setTag("TierSystem:BlockedItem", oldItemData);

                                                playerInventory.setInventorySlotContents(slot, newItemStack);

                                                return;
                                            }
                                        }
                                    }
                                } else if(!ConfigCore.modsBlocked[j].contains(",") && !ConfigCore.modsBlocked[j].equals(""))
                                {
                                    String modID = ConfigCore.modsBlocked[j];
                                    GameRegistry.UniqueIdentifier mod = GameRegistry.findUniqueIdentifierFor(stack.getItem());

                                    if(mod != null)
                                    {
                                        if(modID.equalsIgnoreCase(mod.modId))
                                        {
                                            // Save item to ItemStack
                                            // Switch item in slot to ItemBlocked
                                            // Save ItemStack to ItemBlocked

                                            ItemStack oldItemStack = stack;
                                            ItemStack newItemStack = new ItemStack(ModItems.blockedItem);
                                            NBTTagCompound oldItemData = new NBTTagCompound();

                                            oldItemData = oldItemStack.writeToNBT(oldItemData);

                                            newItemStack.stackTagCompound = new NBTTagCompound();
                                            newItemStack.stackTagCompound.setTag("TierSystem:BlockedItem", oldItemData);

                                            playerInventory.setInventorySlotContents(slot, newItemStack);

                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}