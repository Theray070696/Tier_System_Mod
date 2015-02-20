package io.github.Theray070696.tiersystem.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import io.github.Theray070696.tiersystem.TierSystem;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncItems;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncTiers;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.List;

/**
 * Created by Theray on 10/6/14.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ForgeEventHandler
{

    @SubscribeEvent
    public void onEntityKilled(LivingDeathEvent event)
    {
        if(ConfigCore.tierCount > 0)
        {
            EntityLivingBase entityLiving = event.entityLiving;
            String entityName = EntityList.getEntityString(entityLiving);
            DamageSource damageSource = event.source;

            if(damageSource.getSourceOfDamage() instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) damageSource.getSourceOfDamage();

                if(TierHandler.getCurrentTierForPlayer(player) < ConfigCore.thingsToKillForTiers.length)
                {
                    String[] tiersAndBosses = ConfigCore.thingsToKillForTiers;

                    if(tiersAndBosses[TierHandler.getCurrentTierForPlayer(player)].contains(","))
                    {
                        String[] tierBosses = tiersAndBosses[TierHandler.getCurrentTierForPlayer(player)].split(",");

                        for(int j = 0; j < tierBosses.length; j++)
                        {
                            if(tierBosses[j].equalsIgnoreCase(entityName) && !TierHandler.wasBossKilledForPlayer(entityName, player))
                            {
                                TierHandler.addBossToPlayerKilledBossList(entityName, player);
                                if(ConfigCore.tiersAreServerWide)
                                {
                                    MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(player.getDisplayName() + " " + StatCollector.translateToLocal("bossKilled") + " " + entityLiving.getCommandSenderName() + "!"));
                                } else
                                {
                                    player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("bossKilledPlayer") + " " + entityLiving.getCommandSenderName() + "!"));
                                }

                                return;
                            }
                        }
                    } else if(!tiersAndBosses[TierHandler.getCurrentTierForPlayer(player)].contains(",") && !tiersAndBosses[TierHandler.getCurrentTierForPlayer(player)].equals(""))
                    {
                        String tierBoss = tiersAndBosses[TierHandler.getCurrentTierForPlayer(player)];

                        if(tierBoss.equalsIgnoreCase(entityName) && !TierHandler.wasBossKilledForPlayer(entityName, player))
                        {
                            TierHandler.addBossToPlayerKilledBossList(entityName, player);
                            if(ConfigCore.tiersAreServerWide)
                            {
                                MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(player.getDisplayName() + " " + StatCollector.translateToLocal("bossKilled") + " " + entityLiving.getCommandSenderName() + "!"));
                            } else
                            {
                                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("bossKilledPlayer") + " " + entityLiving.getCommandSenderName() + "!"));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isServer() && event.world.provider.dimensionId == 0 && ConfigCore.tierCount > 0 && ConfigCore.tiersAreServerWide)
        {
            WorldServer world = (WorldServer) event.world;
            TierSaveData saveData = (TierSaveData) world.perWorldStorage.loadData(TierSaveData.class, "TierSaveData");

            if(saveData == null)
            {
                saveData = new TierSaveData("TierSaveData");
                world.perWorldStorage.setData("TierSaveData", saveData);
            }

            TierHandler.saveData = saveData;

            TierHandler.setCurrentTier(saveData.currentTier);
            if(saveData.bossesKilled != null)
            {
                for(int i = 0; i < saveData.bossesKilled.length; i++)
                {
                    TierHandler.addBossToKilledList(saveData.bossesKilled[i]);
                }
            }

            if(saveData.itemsTurnedIn != null && saveData.amountsTurnedIn != null && saveData.itemsTurnedIn.length == saveData.amountsTurnedIn.length)
            {
                for(int i = 0; i < saveData.itemsTurnedIn.length; i++)
                {
                    String itemTurnedIn = saveData.itemsTurnedIn[i];
                    String metadataString = "";

                    if(itemTurnedIn.contains(" "))
                    {
                        String[] itemAndMeta = itemTurnedIn.split(" ", 2);

                        itemTurnedIn = itemAndMeta[0];
                        metadataString = itemAndMeta[1];
                    }

                    int metadata;
                    int amount = saveData.amountsTurnedIn[i];

                    if(!metadataString.equals(""))
                    {
                        metadata = Integer.parseInt(metadataString);
                    } else
                    {
                        metadata = 0;
                    }

                    TierHandler.addItemTurnedIn(itemTurnedIn, metadata, amount);
                }
            }

            TierSystem.pipeline.sendToAll(new PacketSyncTiers());
            TierSystem.pipeline.sendToAll(new PacketSyncItems());
        } else if(FMLCommonHandler.instance().getEffectiveSide().isServer() && event.world.provider.dimensionId == 0 && ConfigCore.tierCount > 0 && !ConfigCore.tiersAreServerWide)
        {
            List objects = event.world.playerEntities;
            for(Object object : objects)
            {
                if(object != null && object instanceof EntityPlayerMP)
                {
                    EntityPlayerMP player = (EntityPlayerMP) object;

                    TierSystem.pipeline.sendTo(new PacketSyncItems(player), player);
                    TierSystem.pipeline.sendTo(new PacketSyncTiers(player), player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityConstructed(EntityEvent.EntityConstructing event)
    {
        if(event.entity instanceof EntityPlayerMP && !ConfigCore.tiersAreServerWide)
        {
            event.entity.registerExtendedProperties("tiersystem", new PlayerExtendedProperties());
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if(event.entity instanceof EntityPlayerMP && !ConfigCore.tiersAreServerWide)
        {
            if(event.entity.getExtendedProperties("tiersystem") instanceof PlayerExtendedProperties && FMLCommonHandler.instance().getSide().equals(Side.SERVER))
            {
                PlayerExtendedProperties properties = (PlayerExtendedProperties) event.entity.getExtendedProperties("tiersystem");
                properties.sync();
            }
        }
    }
}