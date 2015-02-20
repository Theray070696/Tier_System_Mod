package io.github.Theray070696.tiersystem.item;

import io.github.Theray070696.tiersystem.TierSystem;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncTiers;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by Theray on 10/10/14.
 */
public class ItemTierUp extends ItemTierSystem
{

    public ItemTierUp()
    {
        super();
        this.setUnlocalizedName("tierUp");
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if(ConfigCore.tierCount > 0)
        {
            if(!world.isRemote)
            {
                if(TierHandler.getCurrentTierForPlayer(entityPlayer) < ConfigCore.tierCount)
                {
                    TierHandler.setCurrentTierForPlayer(TierHandler.getCurrentTierForPlayer(entityPlayer) + 1, entityPlayer);
                    TierHandler.clearBossesKilledForPlayer(entityPlayer);
                    if(ConfigCore.tiersAreServerWide)
                    {
                        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(StatCollector.translateToLocal("tierHasBeenUnlocked1") + " " + TierHandler.getCurrentTierForPlayer(entityPlayer) + " " + StatCollector.translateToLocal("tierHasBeenUnlocked2") + "!"));
                        TierSystem.pipeline.sendToAll(new PacketSyncTiers());
                    } else
                    {
                        entityPlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tierHasBeenUnlocked1") + " " + TierHandler.getCurrentTierForPlayer(entityPlayer) + " " + StatCollector.translateToLocal("tierHasBeenUnlocked2") + "!"));
                        TierSystem.pipeline.sendTo(new PacketSyncTiers(entityPlayer), (EntityPlayerMP) entityPlayer);
                    }


                    itemStack.stackSize--;

                }
            }
        }

        return itemStack;
    }
}