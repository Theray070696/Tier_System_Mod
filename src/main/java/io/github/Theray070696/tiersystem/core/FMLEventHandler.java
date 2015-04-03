package io.github.Theray070696.tiersystem.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.github.Theray070696.tiersystem.TierSystem;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.network.packet.PacketSendServerConfigs;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncItems;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncTiers;
import io.github.Theray070696.tiersystem.tier.CraftingChecker;
import io.github.Theray070696.tiersystem.tier.ItemChecker;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import io.github.Theray070696.tiersystem.util.ArrayUtilities;
import io.github.Theray070696.tiersystem.util.LogHelper;
import net.minecraft.entity.player.EntityPlayerMP;
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
        CraftingChecker.checkCrafting(event);

        ItemChecker.checkForBlockedItems(event);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if(event.type.equals(TickEvent.Type.WORLD) && ConfigCore.tierCount > 0 && ConfigCore.tiersAreServerWide)
        {
            if(TierHandler.getCurrentTier() < ConfigCore.thingsToKillForTiers.length)
            {
                String[] tiersAndBosses = null;
                String[] bossesKilled = TierHandler.getBossesKilled();

                if(ConfigCore.thingsToKillForTiers[TierHandler.getCurrentTier()].contains(","))
                {
                    tiersAndBosses = ConfigCore.thingsToKillForTiers[TierHandler.getCurrentTier()].split(",");
                    Arrays.sort(tiersAndBosses);
                } else
                {
                    tiersAndBosses = new String[] { ConfigCore.thingsToKillForTiers[TierHandler.getCurrentTier()] };
                }

                if(bossesKilled.length > 1)
                {
                    Arrays.sort(bossesKilled);
                }

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
                        String[] tiersAndBosses = null;
                        String[] bossesKilled = TierHandler.getBossesKilledForPlayer(player);

                        if(ConfigCore.thingsToKillForTiers[TierHandler.getCurrentTierForPlayer(player)].contains(","))
                        {
                            tiersAndBosses = ConfigCore.thingsToKillForTiers[TierHandler.getCurrentTierForPlayer(player)].split(",");
                            Arrays.sort(tiersAndBosses);
                        } else
                        {
                            tiersAndBosses = new String[] { ConfigCore.thingsToKillForTiers[TierHandler.getCurrentTierForPlayer(player)] };
                        }

                        if(bossesKilled.length > 1)
                        {
                            Arrays.sort(bossesKilled);
                        }

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