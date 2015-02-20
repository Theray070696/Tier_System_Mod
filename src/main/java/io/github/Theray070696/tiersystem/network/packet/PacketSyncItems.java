package io.github.Theray070696.tiersystem.network.packet;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import io.github.Theray070696.tiersystem.util.ArrayUtilities;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

/**
 * Created by Theray on 11/5/14.
 */
public class PacketSyncItems extends AbstractPacket
{

    EntityPlayer entityPlayer;
    String[] items;

    public PacketSyncItems() {}

    public PacketSyncItems(EntityPlayer player)
    {
        this.entityPlayer = player;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        Map<String, Integer> itemsTurnedIn;

        if(TierHandler.getItemsTurnedInForPlayer(this.entityPlayer) != null)
        {
            itemsTurnedIn = TierHandler.getItemsTurnedInForPlayer(this.entityPlayer);
        } else
        {
            itemsTurnedIn = null;
        }

        String[] itemsTurnedInString;
        int[] amountTurnedInInt;

        if(itemsTurnedIn != null && !itemsTurnedIn.isEmpty())
        {
            itemsTurnedInString = ArrayUtilities.objectArrayToStringArray(itemsTurnedIn.keySet().toArray());
            amountTurnedInInt = ArrayUtilities.objectArrayToIntArray(itemsTurnedIn.values().toArray());
        } else
        {
            itemsTurnedInString = null;
            amountTurnedInInt = null;
        }

        if(itemsTurnedIn != null && itemsTurnedInString != null && amountTurnedInInt != null && itemsTurnedInString.length == amountTurnedInInt.length)
        {
            String[] items = new String[itemsTurnedInString.length];

            for(int i = 0; i < itemsTurnedInString.length; i++)
            {
                items[i] = itemsTurnedInString[i] + "%" + amountTurnedInInt[i];
            }

            ByteBufUtils.writeUTF8String(buffer, Joiner.on("/").skipNulls().join(items));
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        if(FMLCommonHandler.instance().getSide().isClient())
        {
            this.items = ByteBufUtils.readUTF8String(buffer).split("/");
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        TierHandler.clearItemsTurnedInForPlayer(player);

        for(int i = 0; i < this.items.length; i++)
        {
            String[] itemAndAmount = this.items[i].split("%", 2);

            String item = itemAndAmount[0];
            int amount = Integer.parseInt(itemAndAmount[1]);
            int metadata = 0;

            if(item.contains(" "))
            {
                String[] itemMetaAndAmount = item.split(" ", 2);
                item = itemMetaAndAmount[0];
                metadata = Integer.parseInt(itemMetaAndAmount[1]);
            }

            TierHandler.addItemTurnedInForPlayer(player, item, metadata, amount);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {}
}