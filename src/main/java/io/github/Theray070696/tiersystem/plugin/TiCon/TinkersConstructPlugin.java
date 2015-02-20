package io.github.Theray070696.tiersystem.plugin.TiCon;

import cpw.mods.fml.common.FMLCommonHandler;
import io.github.Theray070696.tiersystem.plugin.IPlugin;

/**
 * Created by Theray on 10/18/14.
 */
public class TinkersConstructPlugin implements IPlugin
{

    @Override
    public String getModID()
    {
        return "TConstruct";
    }

    @Override
    public void preInit() {}

    @Override
    public void init()
    {
        FMLCommonHandler.instance().bus().register(new TiConFMLEventHandler());
    }

    @Override
    public void postInit() {}
}