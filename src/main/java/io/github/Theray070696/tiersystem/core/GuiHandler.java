package io.github.Theray070696.tiersystem.core;

import cpw.mods.fml.common.network.IGuiHandler;
import io.github.Theray070696.tiersystem.block.tile.TileTierDeliverySystem;
import io.github.Theray070696.tiersystem.client.gui.inventory.GuiTierDeliverySystem;
import io.github.Theray070696.tiersystem.inventory.ContainerTierDeliverySystem;
import io.github.Theray070696.tiersystem.lib.GuiIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Theray on 10/20/14.
 */
public class GuiHandler implements IGuiHandler
{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GuiIds.TIERDELIVERYSYSTEM.ordinal() && (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileTierDeliverySystem))
        {
            return new ContainerTierDeliverySystem(player.inventory, (TileTierDeliverySystem) world.getTileEntity(x, y, z));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GuiIds.TIERDELIVERYSYSTEM.ordinal() && (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileTierDeliverySystem))
        {
            return new GuiTierDeliverySystem(new ContainerTierDeliverySystem(player.inventory, (TileTierDeliverySystem) world.getTileEntity(x, y, z)));
        }

        return null;
    }
}