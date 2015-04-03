package io.github.Theray070696.tiersystem.proxy;

import com.jadarstudios.developercapes.DevCapes;
import cpw.mods.fml.relauncher.Side;

/**
 * Created by Theray on 10/4/14.
 */
@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy
{

    @Override
    public Side getSide()
    {
        return Side.CLIENT;
    }

    @Override
    public void loadCapes()
    {
        DevCapes.getInstance().registerConfig("https://github.com/Theray070696/Tier_System_Mod/blob/master/capes/config.json");
    }
}