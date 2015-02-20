package io.github.Theray070696.tiersystem.network.packet;

import cpw.mods.fml.common.FMLCommonHandler;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Theray on 10/6/14.
 */
public class PacketSyncTiers extends AbstractPacket
{

    int tier;
    EntityPlayer entityPlayer;

    public PacketSyncTiers() {}

    public PacketSyncTiers(EntityPlayer entityPlayer)
    {
        this.entityPlayer = entityPlayer;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(TierHandler.getCurrentTierForPlayer(entityPlayer));
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        if(FMLCommonHandler.instance().getSide().isClient())
        {
            tier = buffer.readInt();
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        TierHandler.setCurrentTierForPlayer(tier, player);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {}
}