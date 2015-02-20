package io.github.Theray070696.tiersystem.network.packet;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.util.LogHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Theray on 10/7/14.
 */
public class PacketSendServerConfigs extends AbstractPacket
{

    public PacketSendServerConfigs() {}

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        LogHelper.info("Sending server configs to client.");

        buffer.writeInt(ConfigCore.tierCount);
        ByteBufUtils.writeUTF8String(buffer, Joiner.on("/").skipNulls().join(ConfigCore.tierRestrictions));
        ByteBufUtils.writeUTF8String(buffer, Joiner.on("/").skipNulls().join(ConfigCore.modsBlocked));
        buffer.writeBoolean(ConfigCore.blockMods);
        buffer.writeBoolean(ConfigCore.tiersAreServerWide);
        ByteBufUtils.writeUTF8String(buffer, Joiner.on("/").skipNulls().join(ConfigCore.itemsToTurnInForNextTier));
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        if(FMLCommonHandler.instance().getSide().isClient())
        {
            LogHelper.info("Received server configs.");

            ConfigCore.tierCount = buffer.readInt();
            ConfigCore.tierRestrictions = ByteBufUtils.readUTF8String(buffer).split("/");
            ConfigCore.modsBlocked = ByteBufUtils.readUTF8String(buffer).split("/");
            ConfigCore.blockMods = buffer.readBoolean();
            ConfigCore.tiersAreServerWide = buffer.readBoolean();
            ConfigCore.itemsToTurnInForNextTier = ByteBufUtils.readUTF8String(buffer).split("/");
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player) {}

    @Override
    public void handleServerSide(EntityPlayer player) {}
}