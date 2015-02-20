package io.github.Theray070696.tiersystem.plugin.TiCon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.tier.CraftingHandler;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.tools.inventory.CraftingStationContainer;

/**
 * Created by Theray on 10/18/14.
 */
public class TiConFMLEventHandler
{

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(ConfigCore.tierCount > 0 && event.player != null)
        {
            EntityPlayer player = event.player;

            if(player.openContainer != null)
            {
                Container container = player.openContainer;

                if(container instanceof CraftingStationContainer)
                {
                    CraftingStationContainer craftingStationContainer = (CraftingStationContainer) container;
                    InventoryCrafting craftMatrix = craftingStationContainer.craftMatrix;
                    InventoryCraftResult craftResult = (InventoryCraftResult) craftingStationContainer.craftResult;

                    if(craftMatrix != null && craftResult != null)
                    {
                        for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
                        {
                            ItemStack itemStack = craftMatrix.getStackInSlot(i);
                            if(itemStack != null)
                            {
                                if(!Item.itemRegistry.getNameForObject(itemStack.getItem()).equals(""))
                                {
                                    String itemName = Item.itemRegistry.getNameForObject(itemStack.getItem());
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

                                                    if(restriction.equalsIgnoreCase(itemName) && (metadata == -1 || itemStack.getItemDamage() == metadata))
                                                    {
                                                        CraftingHandler.setSlotToNull(craftResult, 0);

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

                                                if(restriction.equalsIgnoreCase(itemName) && (metadata == -1 || itemStack.getItemDamage() == metadata))
                                                {
                                                    CraftingHandler.setSlotToNull(craftResult, 0);

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
                                                        GameRegistry.UniqueIdentifier mod = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());

                                                        if(mod != null)
                                                        {
                                                            if(modID.equalsIgnoreCase(mod.modId))
                                                            {
                                                                CraftingHandler.setSlotToNull(craftResult, 0);

                                                                return;
                                                            }
                                                        }
                                                    }
                                                } else if(!ConfigCore.modsBlocked[j].contains(",") && !ConfigCore.modsBlocked[j].equals(""))
                                                {
                                                    String modID = ConfigCore.modsBlocked[j];
                                                    GameRegistry.UniqueIdentifier mod = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());

                                                    if(mod != null)
                                                    {
                                                        if(modID.equalsIgnoreCase(mod.modId))
                                                        {
                                                            CraftingHandler.setSlotToNull(craftResult, 0);

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

                        ItemStack itemStack = craftResult.getStackInSlot(0);

                        if(itemStack != null)
                        {
                            if(!Item.itemRegistry.getNameForObject(itemStack.getItem()).equals(""))
                            {
                                String itemName = Item.itemRegistry.getNameForObject(itemStack.getItem());
                                for(int i = 0; i < ConfigCore.tierRestrictions.length; i++)
                                {
                                    if(i >= TierHandler.getCurrentTierForPlayer(player))
                                    {
                                        if(ConfigCore.tierRestrictions[i].contains(","))
                                        {
                                            String[] restrictions = ConfigCore.tierRestrictions[i].split(",");
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

                                                if(restriction.equalsIgnoreCase(itemName) && (metadata == -1 || itemStack.getItemDamage() == metadata))
                                                {
                                                    CraftingHandler.setSlotToNull(craftResult, 0);

                                                    return;
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

                                            if(restriction.equalsIgnoreCase(itemName) && (metadata == -1 || itemStack.getItemDamage() == metadata))
                                            {
                                                CraftingHandler.setSlotToNull(craftResult, 0);

                                                return;
                                            }
                                        }
                                    }
                                }

                                if(ConfigCore.blockMods)
                                {
                                    for(int i = 0; i < ConfigCore.modsBlocked.length; i++)
                                    {
                                        if(i >= TierHandler.getCurrentTierForPlayer(player))
                                        {
                                            if(ConfigCore.modsBlocked[i].contains(","))
                                            {
                                                String[] modIDs = ConfigCore.modsBlocked[i].split(",");
                                                for(int j = 0; j < modIDs.length; j++)
                                                {
                                                    String modID = modIDs[j];
                                                    GameRegistry.UniqueIdentifier mod = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());

                                                    if(mod != null)
                                                    {
                                                        if(modID.equalsIgnoreCase(mod.modId))
                                                        {
                                                            CraftingHandler.setSlotToNull(craftResult, 0);

                                                            return;
                                                        }
                                                    }
                                                }
                                            } else if(!ConfigCore.modsBlocked[i].contains(",") && !ConfigCore.modsBlocked[i].equals(""))
                                            {
                                                String modID = ConfigCore.modsBlocked[i];
                                                GameRegistry.UniqueIdentifier mod = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());

                                                if(mod != null)
                                                {
                                                    if(modID.equalsIgnoreCase(mod.modId))
                                                    {
                                                        CraftingHandler.setSlotToNull(craftResult, 0);

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
        }
    }
}