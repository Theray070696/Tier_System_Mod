package io.github.Theray070696.tiersystem;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.core.FMLEventHandler;
import io.github.Theray070696.tiersystem.core.ForgeEventHandler;
import io.github.Theray070696.tiersystem.core.GuiHandler;
import io.github.Theray070696.tiersystem.init.ModBlocks;
import io.github.Theray070696.tiersystem.init.ModItems;
import io.github.Theray070696.tiersystem.lib.ModInfo;
import io.github.Theray070696.tiersystem.network.PacketPipeline;
import io.github.Theray070696.tiersystem.plugin.PluginHandler;
import io.github.Theray070696.tiersystem.proxy.IProxy;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import io.github.Theray070696.tiersystem.util.LogHelper;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Theray on 10/4/14.
 */
@SuppressWarnings("unused")
@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION)
public class TierSystem
{

    public static final PacketPipeline pipeline = new PacketPipeline();
    @Mod.Instance(ModInfo.MOD_ID)
    public static TierSystem INSTANCE;
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
    public static IProxy proxy;

    public TierSystem()
    {
        PluginHandler.getInstance().registerBuiltInPlugins();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LogHelper.info("Pre-Init");

        ConfigCore.loadConfig(event);

        ModItems.loadItems();

        if(ConfigCore.enableDebugMode)
        {
            ModBlocks.loadBlocks();
        }

        PluginHandler.getInstance().preInit();

        proxy.loadCapes();

        if(ConfigCore.tierCount <= 0)
        {
            LogHelper.warn("Mod was installed and tier count is 0!");
            LogHelper.warn("To use this mod properly, go to the config");
            LogHelper.warn("and setup some tiers and ways to unlock those tiers.");

            LogHelper.warn("If this is the first launch, ignore this message.");
            LogHelper.info("If you are using this mod on your client and connecting");
            LogHelper.info("to a server, ignore this message, your tiers will be");
            LogHelper.info("setup by the server you connect to, if it isn't,");
            LogHelper.info("contact the server owner and tell them to setup some tiers.");
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        LogHelper.info("Init");

        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());

        if(ConfigCore.enableDebugMode)
        {
            ModBlocks.registerTileEntities();
        }

        FMLCommonHandler.instance().bus().register(new FMLEventHandler());
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());

        pipeline.initialize();

        PluginHandler.getInstance().init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LogHelper.info("Post-Init");

        pipeline.postInitialize();

        PluginHandler.getInstance().postInit();
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        TierHandler.saveData = null;
    }
}