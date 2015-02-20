package io.github.Theray070696.tiersystem.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.Theray070696.tiersystem.TierSystem;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.network.packet.PacketSendServerConfigs;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncItems;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncTiers;
import io.github.Theray070696.tiersystem.tier.CraftingHandler;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import io.github.Theray070696.tiersystem.util.ArrayUtilities;
import io.github.Theray070696.tiersystem.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Theray on 10/5/14.
 */
@SuppressWarnings({"unused", "unchecked"})
public class FMLEventHandler
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

                if(container instanceof ContainerWorkbench)
                {
                    ContainerWorkbench containerWorkbench = (ContainerWorkbench) container;
                    InventoryCrafting craftMatrix = containerWorkbench.craftMatrix;
                    InventoryCraftResult craftResult = (InventoryCraftResult) containerWorkbench.craftResult;

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

                        if(itemStack != null && !Item.itemRegistry.getNameForObject(itemStack.getItem()).equals(""))
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

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if(event.type.equals(TickEvent.Type.WORLD) && ConfigCore.tierCount > 0 && ConfigCore.tiersAreServerWide)
        {
            if(TierHandler.getCurrentTier() < ConfigCore.thingsToKillForTiers.length)
            {
                String[] tiersAndBosses = ConfigCore.thingsToKillForTiers[TierHandler.getCurrentTier()].split(",");
                String[] bossesKilled = TierHandler.getBossesKilled();
                Arrays.sort(tiersAndBosses);
                Arrays.sort(bossesKilled);

                if(Arrays.equals(bossesKilled, tiersAndBosses))
                {
                    MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(StatCollector.translateToLocal("everyBossSlain1") + " " + (TierHandler.getCurrentTier() + 1) + " " + StatCollector.translateToLocal("everyBossSlain2") + "!"));

                    TierHandler.setCurrentTier(TierHandler.getCurrentTier() + 1);
                    TierHandler.clearBossesKilled();

                    MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(StatCollector.translateToLocal("tierHasBeenUnlocked1") + " " + TierHandler.getCurrentTier() + " " + StatCollector.translateToLocal("tierHasBeenUnlocked2") + "!"));
                }
            }

            if(TierHandler.saveData.currentTier != TierHandler.getCurrentTier())
            {
                TierHandler.saveData.currentTier = TierHandler.getCurrentTier();
                TierHandler.saveData.markDirty();

                TierSystem.pipeline.sendToAll(new PacketSyncTiers());
            }

            if(!Arrays.equals(TierHandler.saveData.bossesKilled, TierHandler.getBossesKilled()))
            {
                TierHandler.saveData.bossesKilled = TierHandler.getBossesKilled();
                TierHandler.saveData.markDirty();
            }

            if(!Arrays.equals(TierHandler.saveData.itemsTurnedIn, ArrayUtilities.objectArrayToStringArray(TierHandler.getItemsTurnedIn().keySet().toArray())) || !Arrays.equals(TierHandler.saveData.amountsTurnedIn, ArrayUtilities.objectArrayToIntArray(TierHandler.getItemsTurnedIn().values().toArray())))
            {
                TierHandler.saveData.itemsTurnedIn = ArrayUtilities.objectArrayToStringArray(TierHandler.getItemsTurnedIn().keySet().toArray());
                TierHandler.saveData.amountsTurnedIn = ArrayUtilities.objectArrayToIntArray(TierHandler.getItemsTurnedIn().values().toArray());
                TierHandler.saveData.markDirty();

                TierSystem.pipeline.sendToAll(new PacketSyncItems());
            }
        } else if(event.type.equals(TickEvent.Type.WORLD) && ConfigCore.tierCount > 0 && !ConfigCore.tiersAreServerWide)
        {
            List objects = event.world.playerEntities;
            for(Object object : objects)
            {
                if(object != null && object instanceof EntityPlayerMP)
                {
                    EntityPlayerMP player = (EntityPlayerMP) object;

                    if(TierHandler.getCurrentTierForPlayer(player) < ConfigCore.thingsToKillForTiers.length)
                    {
                        String[] tiersAndBosses = ConfigCore.thingsToKillForTiers[TierHandler.getCurrentTierForPlayer(player)].split(",");
                        String[] bossesKilled = TierHandler.getBossesKilledForPlayer(player);
                        Arrays.sort(tiersAndBosses);
                        Arrays.sort(bossesKilled);

                        if(Arrays.equals(bossesKilled, tiersAndBosses))
                        {
                            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("everyBossSlain1") + " " + (TierHandler.getCurrentTierForPlayer(player) + 1) + " " + StatCollector.translateToLocal("everyBossSlain2") + "!"));

                            TierHandler.setCurrentTierForPlayer(TierHandler.getCurrentTierForPlayer(player) + 1, player);
                            TierHandler.clearBossesKilledForPlayer(player);

                            TierSystem.pipeline.sendTo(new PacketSyncTiers(player), player);

                            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tierHasBeenUnlocked1") + " " + TierHandler.getCurrentTierForPlayer(player) + " " + StatCollector.translateToLocal("tierHasBeenUnlocked2") + "!"));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event)
    {
        LogHelper.info("Sending current tier info to " + event.player.getCommandSenderName() + ".");
        TierSystem.pipeline.sendTo(new PacketSyncTiers(event.player), (EntityPlayerMP) event.player);

        TierSystem.pipeline.sendTo(new PacketSendServerConfigs(), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        ConfigCore.tierCount = ConfigCore.clientTierCount;
        ConfigCore.tierRestrictions = ConfigCore.clientTierRestrictions;
        ConfigCore.modsBlocked = ConfigCore.clientModsBlocked;
        ConfigCore.blockMods = ConfigCore.clientBlockMods;
        ConfigCore.tiersAreServerWide = ConfigCore.clientTiersAreServerWide;
        ConfigCore.itemsToTurnInForNextTier = ConfigCore.clientItemsToTurnInForNextTier;
    }
}