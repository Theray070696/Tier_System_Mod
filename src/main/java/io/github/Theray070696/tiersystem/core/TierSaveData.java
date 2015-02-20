package io.github.Theray070696.tiersystem.core;

import com.google.common.base.Joiner;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

/**
 * Created by Theray on 10/6/14.
 */
public class TierSaveData extends WorldSavedData
{

    public int currentTier;
    public String[] bossesKilled;
    public String[] itemsTurnedIn;
    public int[] amountsTurnedIn;

    public TierSaveData(String par1Str)
    {
        super(par1Str);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        currentTier = tagCompound.getInteger("currentTier");
        bossesKilled = tagCompound.getString("bossesKilled").split(",");

        itemsTurnedIn = tagCompound.getString("itemsTurnedInNames").split("/");
        amountsTurnedIn = tagCompound.getIntArray("itemsTurnedInAmounts");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("currentTier", currentTier);
        tagCompound.setString("bossesKilled", Joiner.on(",").skipNulls().join(bossesKilled));

        tagCompound.setString("itemsTurnedInNames", Joiner.on("/").skipNulls().join(itemsTurnedIn));
        tagCompound.setIntArray("itemsTurnedInAmounts", amountsTurnedIn);
    }
}