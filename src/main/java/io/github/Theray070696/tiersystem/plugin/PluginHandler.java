/**
 * This class was created by <Theray070696>. It's distributed as
 * part of the Inheritance Mod.
 *
 * Inheritance is Open Source
 *
 * File Created @ [Dec 4, 2013, 10:09:34 AM (EST)]
 */
package io.github.Theray070696.tiersystem.plugin;

import cpw.mods.fml.common.Loader;
import io.github.Theray070696.tiersystem.plugin.TiCon.TinkersConstructPlugin;
import io.github.Theray070696.tiersystem.util.LogHelper;

import java.util.LinkedList;
import java.util.List;

public class PluginHandler
{

    private static PluginHandler instance;
    private List<IPlugin> plugins = new LinkedList<IPlugin>();
    private Phase currPhase = Phase.PRELAUNCH;

    private PluginHandler()
    {
    }

    public static PluginHandler getInstance()
    {
        if(instance == null)
        {
            instance = new PluginHandler();
        }
        return instance;
    }

    public void registerPlugin(IPlugin plugin)
    {
        this.loadPlugin(plugin);
    }

    private void loadPlugin(IPlugin plugin)
    {
        if(!Loader.isModLoaded(plugin.getModID()))
        {
            return;
        }

        LogHelper.info("Registering plugin for " + plugin.getModID());
        this.plugins.add(plugin);

        switch(this.currPhase)
        {
            case DONE:
            case POSTINIT:
                plugin.preInit();
                plugin.init();
                plugin.postInit();
                break;
            case INIT:
                plugin.preInit();
                plugin.init();
                break;
            case PREINIT:
                plugin.preInit();
                break;
            default:
                break;
        }
    }

    public void preInit()
    {
        this.currPhase = Phase.PREINIT;
        for(IPlugin pl : this.plugins)
        {
            pl.preInit();
        }
    }

    public void init()
    {
        this.currPhase = Phase.INIT;
        for(IPlugin pl : this.plugins)
        {
            pl.init();
        }
    }

    public void postInit()
    {
        this.currPhase = Phase.POSTINIT;
        for(IPlugin pl : this.plugins)
        {
            pl.postInit();
        }
        this.currPhase = Phase.DONE;
    }

    public void registerBuiltInPlugins()
    {
        this.registerPlugin(new TinkersConstructPlugin());
    }

    private enum Phase
    {
        PRELAUNCH, PREINIT, INIT, POSTINIT, DONE
    }
}