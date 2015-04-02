package io.github.Theray070696.tiersystem.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * Created by Theray on 4/2/15.
 */
public class ItemBlocked extends ItemTierSystem
{
    private ItemStack blockedItem;

    public ItemBlocked()
    {
        super();
        this.setUnlocalizedName("blockedItem");
        this.setCreativeTab(CreativeTabs.tabTools);
        this.maxStackSize = 1;
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        if(this.blockedItem != null)
        {
            return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name") + " (Name of blocked item here)").trim();
        } else
        {
            return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name") + " ERROR! INVALID DATA!").trim();
        }
    }

    public void setBlockedItem(ItemStack stack)
    {
        this.blockedItem = stack;
    }


}