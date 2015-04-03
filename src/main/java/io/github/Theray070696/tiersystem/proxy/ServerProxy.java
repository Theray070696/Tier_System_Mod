package io.github.Theray070696.tiersystem.proxy;

import cpw.mods.fml.relauncher.Side;

/**
 * Created by Theray on 10/4/14.
 */
@SuppressWarnings("unused")
public class ServerProxy extends CommonProxy
{

    @Override
    public Side getSide()
    {
        return Side.SERVER;
    }

    @Override
    public void loadCapes()
    {
        // Do nothing, as this is server side.
    }
}