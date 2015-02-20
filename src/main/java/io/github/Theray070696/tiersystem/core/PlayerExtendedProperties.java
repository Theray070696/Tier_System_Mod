package io.github.Theray070696.tiersystem.core;

import com.google.common.base.Joiner;
import io.github.Theray070696.tiersystem.TierSystem;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncItems;
import io.github.Theray070696.tiersystem.network.packet.PacketSyncTiers;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import io.github.Theray070696.tiersystem.util.ArrayUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.Map;

/**
 * Created by Theray on 10/10/14.
 */
public class PlayerExtendedProperties implements IExtendedEntityProperties
{

    private EntityPlayerMP player;

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        compound.setInteger("currentTier", TierHandler.getCurrentTierForPlayer(this.player));

        String[] bossesKilled = TierHandler.getBossesKilledForPlayer(this.player);
        compound.setString("bossesKilled", Joiner.on("/").skipNulls().join(bossesKilled));

        Map<String, Integer> itemsTurnedIn;

        if(TierHandler.getItemsTurnedInForPlayer(this.player) != null)
        {
            itemsTurnedIn = TierHandler.getItemsTurnedInForPlayer(this.player);
        } else
        {
            itemsTurnedIn = null;
        }

        String[] itemsTurnedInString;
        int[] amountTurnedInInt;

        if(itemsTurnedIn != null)
        {
            if(itemsTurnedIn.keySet().toArray().length > 0 && itemsTurnedIn.values().toArray().length > 0)
            {
                itemsTurnedInString = ArrayUtilities.objectArrayToStringArray(itemsTurnedIn.keySet().toArray());
                amountTurnedInInt = ArrayUtilities.objectArrayToIntArray(itemsTurnedIn.values().toArray());
            } else
            {
                itemsTurnedInString = null;
                amountTurnedInInt = null;
            }
        } else
        {
            itemsTurnedInString = null;
            amountTurnedInInt = null;
        }

        if(itemsTurnedInString != null && amountTurnedInInt != null)
        {
            compound.setString("itemsTurnedInNames", Joiner.on("/").skipNulls().join(itemsTurnedInString));
            compound.setIntArray("itemsTurnedInAmounts", amountTurnedInInt);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
        TierHandler.setCurrentTierForPlayer(compound.getInteger("currentTier"), this.player);

        String[] bossesKilled = compound.getString("bossesKilled").split("/");
        for(int i = 0; i < bossesKilled.length; i++)
        {
            TierHandler.addBossToPlayerKilledBossList(bossesKilled[i], this.player);
        }

        String[] itemsTurnedIn = compound.getString("itemsTurnedInNames").split("/");
        int[] amountTurnedIn = compound.getIntArray("itemsTurnedInAmounts");
        if(itemsTurnedIn.length == amountTurnedIn.length)
        {
            for(int i = 0; i < itemsTurnedIn.length; i++)
            {
                String itemTurnedIn = itemsTurnedIn[i];
                String metadataString = "";

                if(itemTurnedIn.contains(" "))
                {
                    String[] itemAndMeta = itemTurnedIn.split(" ", 2);

                    itemTurnedIn = itemAndMeta[0];
                    metadataString = itemAndMeta[1];
                }

                int metadata;
                int amount = amountTurnedIn[i];

                if(metadataString != "")
                {
                    metadata = Integer.parseInt(metadataString);
                } else
                {
                    metadata = 0;
                }

                TierHandler.addItemTurnedInForPlayer(this.player, itemTurnedIn, metadata, amount);
            }
        }
    }

    @Override
    public void init(Entity entity, World world)
    {
        if(entity instanceof EntityPlayerMP)
        {
            this.player = (EntityPlayerMP) entity;

            TierHandler.setupPlayerBosses(this.player);
            TierHandler.setupPlayerTiers(this.player);
            TierHandler.setupPlayerItems(this.player);
        }
    }

    public void sync()
    {
        TierSystem.pipeline.sendTo(new PacketSyncTiers(this.player), this.player);
        TierSystem.pipeline.sendTo(new PacketSyncItems(this.player), this.player);
    }
}