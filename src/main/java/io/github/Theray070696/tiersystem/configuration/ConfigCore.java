package io.github.Theray070696.tiersystem.configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by Theray on 10/4/14.
 */
public class ConfigCore
{

    public static final int tierCountDefault = 0;
    public static final String[] tierRestrictionsDefault = {""};
    public static final String[] thingsToKillForTiersDefault = {""};
    public static final String[] modsBlockedDefault = {""};
    public static final boolean blockModsDefault = false;
    public static final boolean tiersAreServerWideDefault = true;
    public static final String[] itemsToTurnInForNextTierDefault = {""};

    public static int tierCount;
    public static String[] tierRestrictions;
    public static String[] thingsToKillForTiers;
    public static String[] modsBlocked;
    public static boolean blockMods;
    public static boolean tiersAreServerWide;
    public static String[] itemsToTurnInForNextTier;

    public static int clientTierCount;
    public static String[] clientTierRestrictions;
    public static String[] clientModsBlocked;
    public static boolean clientBlockMods;
    public static boolean clientTiersAreServerWide;
    public static String[] clientItemsToTurnInForNextTier;

    public static void loadConfig(FMLPreInitializationEvent event)
    {
        File configFile = event.getSuggestedConfigurationFile();
        Configuration config = new Configuration(configFile);

        tierCount = config.get("Tiers", "tierCount", tierCountDefault, "Number of Tiers").getInt(tierCountDefault);
        tierRestrictions = config.get("Tiers", "tierRestrictions", tierRestrictionsDefault, "What item(s) are locked for which tier. Each line is a different tier. Separate items with a comma, ','. Separate metadata with a space, ' ', use metadata -1 to block all damage values. Use the name used for /give including the 'modname:' or 'minecraft:', IE: 'minecraft:cobblestone,minecraft:stone,minecraft:log 1' will prevent people from crafting anything that uses cobblestone, stone, or spruce wood in its recipe until that tier is unlocked").getStringList();
        thingsToKillForTiers = config.get("Tiers", "thingsToKillForTiers", thingsToKillForTiersDefault, "What mob(s) you have to kill to unlock a tier. Each line is a different tier. Separate mobs with a comma, ','. Use the name used for /summon. IE: 'Pig,Cow' would set it so you have to kill a pig and a cow to unlock that tier").getStringList();
        modsBlocked = config.get("Tiers", "blockedMods", modsBlockedDefault, "Mod id's that are blocked, anything from this mod cannot be crafted or used in crafting.").getStringList();
        blockMods = config.get("Tiers", "blockMods", blockModsDefault, "Set this to true to enable blocking entire mods.").getBoolean(blockModsDefault);
        tiersAreServerWide = config.get("Tiers", "tiersAreServerWide", tiersAreServerWideDefault, "If true, tiers will be shared with the entire server, aka, everybody is on the same tier").getBoolean(tiersAreServerWideDefault);
        itemsToTurnInForNextTier = config.get("Tiers", "itemsToTurnInForNextTier", itemsToTurnInForNextTierDefault, "What item(s)/block(s) you have to turn in to get to the next tier. Formatted the same as restricting items. You can specify an amount by putting it in after the name with a slash, '/', if there is no metadata. If there is metadata, put the slash between the metadata and the amount. IE: 'minecraft:stone/5,minecraft:log 1/6' would make it so you must turn in 5 stone and 6 spruce wood in order to progress to the next tier.").getStringList();

        config.save();

        clientTierCount = tierCount;
        clientTierRestrictions = tierRestrictions;
        clientModsBlocked = modsBlocked;
        clientBlockMods = blockMods;
        clientTiersAreServerWide = tiersAreServerWide;
        clientItemsToTurnInForNextTier = itemsToTurnInForNextTier;
    }
}