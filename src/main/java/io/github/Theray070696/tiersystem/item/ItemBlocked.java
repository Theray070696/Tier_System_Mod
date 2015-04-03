package io.github.Theray070696.tiersystem.item;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by Theray on 4/2/15.
 */
public class ItemBlocked extends ItemTierSystem
{
    public ItemBlocked()
    {
        super();
        this.setUnlocalizedName("blockedItem");
        this.setCreativeTab(CreativeTabs.tabTools);
        this.maxStackSize = 1;
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        ItemStack blockedItem = null;

        if(stack.stackTagCompound != null)
        {
            blockedItem = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("TierSystem:BlockedItem"));
        }

        if(blockedItem != null)
        {
            return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name") + " (" + blockedItem.getDisplayName() + ")").trim();
        } else
        {
            return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name") + " ERROR! INVALID ITEM DATA!").trim();
        }
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if(!world.isRemote)
        {
            if(ConfigCore.tierCount > 0)
            {
                ItemStack blockedItem = null;

                if(stack.stackTagCompound != null)
                {
                    blockedItem = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("TierSystem:BlockedItem"));
                }

                if(blockedItem != null && !Item.itemRegistry.getNameForObject(blockedItem.getItem()).equals(""))
                {
                    String itemName = Item.itemRegistry.getNameForObject(blockedItem.getItem());
                    for(int i = 0; i < ConfigCore.tierRestrictions.length; i++)
                    {
                        if(i < TierHandler.getCurrentTierForPlayer(player))
                        {
                            if(ConfigCore.tierRestrictions[i].contains(","))
                            {
                                String[] restrictions = ConfigCore.tierRestrictions[i].split(",");
                                for(int j = 0; j < restrictions.length; j++)
                                {
                                    String restriction = restrictions[j];
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

                                    if(restriction.equalsIgnoreCase(itemName) && (metadata == -1 || blockedItem.getItemDamage() == metadata))
                                    {
                                        // Switch ItemStack to original item.

                                        stack = blockedItem;

                                        return stack;
                                    }
                                }
                            } else if(!ConfigCore.tierRestrictions[i].contains(",") && !ConfigCore.tierRestrictions[i].equals(""))
                            {
                                String restriction = ConfigCore.tierRestrictions[i];
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

                                if(restriction.equalsIgnoreCase(itemName) && (metadata == -1 || blockedItem.getItemDamage() == metadata))
                                {
                                    // Switch ItemStack to original item.

                                    stack = blockedItem;

                                    return stack;
                                }
                            }
                        }
                    }

                    if(ConfigCore.blockMods)
                    {
                        for(int i = 0; i < ConfigCore.modsBlocked.length; i++)
                        {
                            if(i < TierHandler.getCurrentTierForPlayer(player))
                            {
                                if(ConfigCore.modsBlocked[i].contains(","))
                                {
                                    String[] modIDs = ConfigCore.modsBlocked[i].split(",");
                                    for(int j = 0; j < modIDs.length; j++)
                                    {
                                        String modID = modIDs[j];
                                        GameRegistry.UniqueIdentifier mod = GameRegistry.findUniqueIdentifierFor(blockedItem.getItem());

                                        if(mod != null)
                                        {
                                            if(modID.equalsIgnoreCase(mod.modId))
                                            {
                                                // Switch ItemStack to original item.

                                                stack = blockedItem;

                                                return stack;
                                            }
                                        }
                                    }
                                } else if(!ConfigCore.modsBlocked[i].contains(",") && !ConfigCore.modsBlocked[i].equals(""))
                                {
                                    String modID = ConfigCore.modsBlocked[i];
                                    GameRegistry.UniqueIdentifier mod = GameRegistry.findUniqueIdentifierFor(blockedItem.getItem());

                                    if(mod != null)
                                    {
                                        if(modID.equalsIgnoreCase(mod.modId))
                                        {
                                            // Switch ItemStack to original item.

                                            stack = blockedItem;

                                            return stack;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return stack;
    }


}