package io.github.Theray070696.tiersystem.block;

import io.github.Theray070696.tiersystem.TierSystem;
import io.github.Theray070696.tiersystem.block.tile.TileTierDeliverySystem;
import io.github.Theray070696.tiersystem.lib.GuiIds;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Theray on 10/20/14.
 */
public class BlockTierDeliverySystem extends BlockTierSystem implements ITileEntityProvider
{

    public BlockTierDeliverySystem()
    {
        super();
        this.setBlockName("tierDeliverySystem");
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileTierDeliverySystem();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack)
    {
        if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileTierDeliverySystem && placer instanceof EntityPlayer && ((TileTierDeliverySystem) world.getTileEntity(x, y, z)).getOwner() == null)
        {
            TileTierDeliverySystem tileTierDeliverySystem = (TileTierDeliverySystem) world.getTileEntity(x, y, z);
            EntityPlayer player = (EntityPlayer) placer;

            tileTierDeliverySystem.setOwner(player);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote)
        {
            if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileTierDeliverySystem && ((TileTierDeliverySystem) world.getTileEntity(x, y, z)).getOwner() != null && player.getCommandSenderName().equalsIgnoreCase(((TileTierDeliverySystem) world.getTileEntity(x, y, z)).getOwner().getCommandSenderName()))
            {
                player.openGui(TierSystem.INSTANCE, GuiIds.TIERDELIVERYSYSTEM.ordinal(), world, x, y, z);
                return true;
            }
            return false;
        }
        return false;
    }
}