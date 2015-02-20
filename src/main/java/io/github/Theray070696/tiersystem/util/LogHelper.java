package io.github.Theray070696.tiersystem.util;

import cpw.mods.fml.common.FMLLog;
import io.github.Theray070696.tiersystem.lib.ModInfo;
import org.apache.logging.log4j.Level;

/**
 * Created by Theray on 10/6/14.
 */
public class LogHelper
{

    public static void log(Level logLevel, Object object)
    {
        FMLLog.log(ModInfo.MOD_NAME, logLevel, "[Tier System]" + String.valueOf(object));
    }

    public static void all(Object object)
    {
        log(Level.ALL, object);
    }

    public static void error(Object object)
    {
        log(Level.ERROR, object);
    }

    public static void debug(Object object)
    {
        log(Level.DEBUG, object);
    }

    public static void warn(Object object)
    {
        log(Level.WARN, object);
    }

    public static void info(Object object)
    {
        log(Level.INFO, object);
    }

    public static void fatal(Object object)
    {
        log(Level.FATAL, object);
    }
}