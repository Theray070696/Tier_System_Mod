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
        DevCapes.getInstance().registerConfig("https://dl.dropboxusercontent.com/u/85222445/Tier%20System/capes/config.json");
    }
}