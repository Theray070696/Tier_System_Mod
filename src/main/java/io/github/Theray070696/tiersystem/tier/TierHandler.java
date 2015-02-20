package io.github.Theray070696.tiersystem.tier;

import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.core.TierSaveData;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Theray on 10/6/14.
 */
@SuppressWarnings("unused")
public class TierHandler
{

    public static TierSaveData saveData = null;
    private static Map<EntityPlayer, Integer> currentTierForPlayer = new HashMap<EntityPlayer, Integer>();
    private static Map<EntityPlayer, List<String>> bossesKilledForPlayer = new HashMap<EntityPlayer, List<String>>();
    private static Map<EntityPlayer, Map<String, Integer>> itemsTurnedInForPlayer = new HashMap<EntityPlayer, Map<String, Integer>>();
    private static int currentTier = 0;
    private static List<String> bossesKilled = new ArrayList<String>();
    private static Map<String, Integer> itemsTurnedIn = new HashMap<String, Integer>();

    public static int getCurrentTier()
    {
        return currentTier;
    }

    public static void setCurrentTier(int tier)
    {
        currentTier = tier;
    }

    public static int getCurrentTierForPlayer(EntityPlayer entityPlayer)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            return getCurrentTier();
        }

        if(currentTierForPlayer.containsKey(entityPlayer))
        {
            return currentTierForPlayer.get(entityPlayer);
        } else
        {
            setupPlayerTiers(entityPlayer);

            return 0;
        }
    }

    public static void setupPlayerTiers(EntityPlayer entityPlayer)
    {
        if(!ConfigCore.tiersAreServerWide && entityPlayer != null)
        {
            if(!currentTierForPlayer.containsKey(entityPlayer))
            {
                currentTierForPlayer.put(entityPlayer, 0);
            }
        }
    }

    public static void setupPlayerItems(EntityPlayer entityPlayer)
    {
        if(!ConfigCore.tiersAreServerWide && entityPlayer != null)
        {
            if(!itemsTurnedInForPlayer.containsKey(entityPlayer))
            {
                itemsTurnedInForPlayer.put(entityPlayer, new HashMap<String, Integer>());
            }
        }
    }

    public static void setCurrentTierForPlayer(int tier, EntityPlayer entityPlayer)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            setCurrentTier(tier);
            return;
        }

        if(currentTierForPlayer.containsKey(entityPlayer))
        {
            currentTierForPlayer.remove(entityPlayer);
            currentTierForPlayer.put(entityPlayer, tier);
        } else
        {
            setupPlayerTiers(entityPlayer);
        }
    }

    public static void setupPlayerBosses(EntityPlayer entityPlayer)
    {
        if(!ConfigCore.tiersAreServerWide && entityPlayer != null)
        {
            if(!bossesKilledForPlayer.containsKey(entityPlayer))
            {
                bossesKilledForPlayer.put(entityPlayer, new ArrayList<String>());
            }
        }
    }

    public static void addBossToKilledList(String boss)
    {
        if(ConfigCore.thingsToKillForTiers[currentTier].contains(","))
        {
            String[] bosses = ConfigCore.thingsToKillForTiers[currentTier].split(",");
            for(int i = 0; i < bosses.length; i++)
            {
                if(bosses[i].equalsIgnoreCase(boss))
                {
                    bossesKilled.add(boss);

                    return;
                }
            }
        }
    }

    public static void addBossToPlayerKilledBossList(String boss, EntityPlayer entityPlayer)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            addBossToKilledList(boss);
            return;
        }

        if(!wasBossKilledForPlayer(boss, entityPlayer) && currentTierForPlayer.get(entityPlayer) < ConfigCore.thingsToKillForTiers.length && ConfigCore.tierCount > 0)
        {
            if(ConfigCore.thingsToKillForTiers[currentTierForPlayer.get(entityPlayer)].contains(","))
            {
                String[] bosses = ConfigCore.thingsToKillForTiers[currentTierForPlayer.get(entityPlayer)].split(",");
                for(int i = 0; i < bosses.length; i++)
                {
                    if(bosses[i].equalsIgnoreCase(boss))
                    {
                        if(bossesKilledForPlayer.containsKey(entityPlayer))
                        {
                            List<String> copy = bossesKilledForPlayer.get(entityPlayer);
                            copy.add(boss);

                            if(!bossesKilledForPlayer.get(entityPlayer).contains(boss))
                            {
                                bossesKilledForPlayer.put(entityPlayer, copy);
                            }
                        } else
                        {
                            setupPlayerBosses(entityPlayer);

                            List<String> copy = bossesKilledForPlayer.get(entityPlayer);
                            copy.add(boss);

                            if(!bossesKilledForPlayer.get(entityPlayer).contains(boss))
                            {
                                bossesKilledForPlayer.put(entityPlayer, copy);
                            }
                        }

                        return;
                    }
                }
            }
        }
    }

    public static String[] getBossesKilled()
    {
        String[] bossesKilled1 = new String[bossesKilled.size()];
        for(int i = 0; i < bossesKilled.size(); i++)
        {
            bossesKilled1[i] = bossesKilled.get(i);
        }
        return bossesKilled1;
    }

    public static String[] getBossesKilledForPlayer(EntityPlayer entityPlayer)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            return getBossesKilled();
        }

        if(!bossesKilledForPlayer.containsKey(entityPlayer))
        {
            setupPlayerBosses(entityPlayer);
            return new String[]{""};
        }

        String[] bossesKilled1 = new String[bossesKilledForPlayer.get(entityPlayer).size()];
        for(int i = 0; i < bossesKilledForPlayer.get(entityPlayer).size(); i++)
        {
            bossesKilled1[i] = bossesKilledForPlayer.get(entityPlayer).get(i);
        }
        return bossesKilled1;
    }

    public static boolean wasBossKilled(String bossName)
    {
        return bossesKilled.contains(bossName);
    }

    public static boolean wasBossKilledForPlayer(String bossName, EntityPlayer entityPlayer)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            return wasBossKilled(bossName);
        }

        if(bossesKilledForPlayer.containsKey(entityPlayer))
        {
            return bossesKilledForPlayer.get(entityPlayer).contains(bossName);
        } else
        {
            setupPlayerBosses(entityPlayer);

            return false;
        }
    }

    public static void clearBossesKilled()
    {
        bossesKilled.clear();
    }

    public static void clearBossesKilledForPlayer(EntityPlayer entityPlayer)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            clearBossesKilled();
            return;
        }

        if(bossesKilledForPlayer.containsKey(entityPlayer))
        {
            bossesKilledForPlayer.get(entityPlayer).clear();
        } else
        {
            setupPlayerBosses(entityPlayer);
        }
    }

    public static Map<String, Integer> getItemsTurnedInForPlayer(EntityPlayer entityPlayer)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            return getItemsTurnedIn();
        }

        if(itemsTurnedInForPlayer.containsKey(entityPlayer))
        {
            return itemsTurnedInForPlayer.get(entityPlayer);
        } else
        {
            setupPlayerItems(entityPlayer);

            return new HashMap<String, Integer>();
        }
    }

    public static Map<String, Integer> getItemsTurnedIn()
    {
        return itemsTurnedIn;
    }

    public static int addItemTurnedInForPlayer(EntityPlayer entityPlayer, String itemName, int amount)
    {
        return addItemTurnedInForPlayer(entityPlayer, itemName, 0, amount);
    }

    public static int addItemTurnedInForPlayer(EntityPlayer entityPlayer, String itemName, int metadata, int amount)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            return addItemTurnedIn(itemName, metadata, amount);
        }

        if(!itemsTurnedInForPlayer.containsKey(entityPlayer))
        {
            setupPlayerItems(entityPlayer);
        }

        if(ConfigCore.itemsToTurnInForNextTier[currentTierForPlayer.get(entityPlayer)].contains(","))
        {
            String[] items = ConfigCore.itemsToTurnInForNextTier[currentTierForPlayer.get(entityPlayer)].split(",");
            for(int i = 0; i < items.length; i++)
            {
                String itemToTurnIn = items[i];

                if(metadata <= 0)
                {
                    String itemAmount = "";
                    if(itemToTurnIn.contains("/"))
                    {
                        String[] itemAndAmount = itemToTurnIn.split("/", 2);
                        itemToTurnIn = itemAndAmount[0];
                        itemAmount = itemAndAmount[1];
                    }

                    if(itemToTurnIn.equalsIgnoreCase(itemName))
                    {
                        int amountTurnedIn;

                        if(itemsTurnedInForPlayer.get(entityPlayer).containsKey(itemName))
                        {
                            amountTurnedIn = itemsTurnedInForPlayer.get(entityPlayer).get(itemName);
                            itemsTurnedInForPlayer.get(entityPlayer).remove(itemName);
                        } else
                        {
                            amountTurnedIn = 0;
                        }

                        int amountToTurnIn;
                        int amountLeft = 0;

                        if(!itemAmount.equals(""))
                        {
                            amountToTurnIn = Integer.parseInt(itemAmount);
                        } else
                        {
                            amountToTurnIn = 1;
                        }

                        if(amountTurnedIn + amount >= amountToTurnIn)
                        {
                            amountLeft = (amountTurnedIn + amount) - amountToTurnIn;
                            amountTurnedIn = amountToTurnIn;
                        } else
                        {
                            amountTurnedIn += amount;
                        }

                        itemsTurnedInForPlayer.get(entityPlayer).put(itemName, amountTurnedIn);

                        return amountLeft;
                    }
                } else if(metadata > 0)
                {
                    String itemAmount = "";
                    if(itemToTurnIn.contains("/"))
                    {
                        String[] itemMetaAndAmount = itemToTurnIn.split("/", 2);
                        itemToTurnIn = itemMetaAndAmount[0];
                        itemAmount = itemMetaAndAmount[1];
                    }

                    if(itemToTurnIn.equalsIgnoreCase(itemName + " " + metadata))
                    {
                        int amountTurnedIn;

                        if(itemsTurnedInForPlayer.get(entityPlayer).containsKey(itemName))
                        {
                            amountTurnedIn = itemsTurnedInForPlayer.get(entityPlayer).get(itemName);
                            itemsTurnedInForPlayer.get(entityPlayer).remove(itemName);
                        } else
                        {
                            amountTurnedIn = 0;
                        }

                        int amountToTurnIn;
                        int amountLeft = 0;

                        if(!itemAmount.equals(""))
                        {
                            amountToTurnIn = Integer.parseInt(itemAmount);
                        } else
                        {
                            amountToTurnIn = 1;
                        }

                        if(amountTurnedIn + amount >= amountToTurnIn)
                        {
                            amountLeft = (amountTurnedIn + amount) - amountToTurnIn;
                            amountTurnedIn = amountToTurnIn;
                        } else
                        {
                            amountTurnedIn += amount;
                        }

                        itemsTurnedInForPlayer.get(entityPlayer).put(itemName + " " + metadata, amountTurnedIn);

                        return amountLeft;
                    }
                }
            }
        } else if(!ConfigCore.itemsToTurnInForNextTier[currentTierForPlayer.get((entityPlayer))].equals("") && !ConfigCore.itemsToTurnInForNextTier[currentTierForPlayer.get(entityPlayer)].contains(","))
        {
            String itemToTurnIn = ConfigCore.itemsToTurnInForNextTier[currentTierForPlayer.get(entityPlayer)];

            if(metadata <= 0)
            {
                String itemAmount = "";
                if(itemToTurnIn.contains("/"))
                {
                    String[] itemAndAmount = itemToTurnIn.split("/", 2);
                    itemToTurnIn = itemAndAmount[0];
                    itemAmount = itemAndAmount[1];
                }

                if(itemToTurnIn.equalsIgnoreCase(itemName))
                {
                    int amountTurnedIn;

                    if(itemsTurnedInForPlayer.get(entityPlayer).containsKey(itemName))
                    {
                        amountTurnedIn = itemsTurnedInForPlayer.get(entityPlayer).get(itemName);
                        itemsTurnedInForPlayer.get(entityPlayer).remove(itemName);
                    } else
                    {
                        amountTurnedIn = 0;
                    }

                    int amountToTurnIn;
                    int amountLeft = 0;

                    if(!itemAmount.equals(""))
                    {
                        amountToTurnIn = Integer.parseInt(itemAmount);
                    } else
                    {
                        amountToTurnIn = 1;
                    }

                    if(amountTurnedIn + amount >= amountToTurnIn)
                    {
                        amountLeft = (amountTurnedIn + amount) - amountToTurnIn;
                        amountTurnedIn = amountToTurnIn;
                    } else
                    {
                        amountTurnedIn += amount;
                    }

                    itemsTurnedInForPlayer.get(entityPlayer).put(itemName, amountTurnedIn);

                    return amountLeft;
                }
            } else if(metadata > 0)
            {
                String itemAmount = "";
                if(itemToTurnIn.contains("/"))
                {
                    String[] itemMetaAndAmount = itemToTurnIn.split("/", 2);
                    itemToTurnIn = itemMetaAndAmount[0];
                    itemAmount = itemMetaAndAmount[1];
                }

                if(itemToTurnIn.equalsIgnoreCase(itemName + " " + metadata))
                {
                    int amountTurnedIn;

                    if(itemsTurnedInForPlayer.get(entityPlayer).containsKey(itemName))
                    {
                        amountTurnedIn = itemsTurnedInForPlayer.get(entityPlayer).get(itemName);
                        itemsTurnedInForPlayer.get(entityPlayer).remove(itemName);
                    } else
                    {
                        amountTurnedIn = 0;
                    }

                    int amountToTurnIn;
                    int amountLeft = 0;

                    if(!itemAmount.equals(""))
                    {
                        amountToTurnIn = Integer.parseInt(itemAmount);
                    } else
                    {
                        amountToTurnIn = 1;
                    }

                    if(amountTurnedIn + amount >= amountToTurnIn)
                    {
                        amountLeft = (amountTurnedIn + amount) - amountToTurnIn;
                        amountTurnedIn = amountToTurnIn;
                    } else
                    {
                        amountTurnedIn += amount;
                    }

                    itemsTurnedInForPlayer.get(entityPlayer).put(itemName + " " + metadata, amountTurnedIn);

                    return amountLeft;
                }
            }
        }

        return amount;
    }

    public static int addItemTurnedIn(String itemName, int amount)
    {
        return addItemTurnedIn(itemName, 0, amount);
    }

    public static int addItemTurnedIn(String itemName, int metadata, int amount)
    {
        if(ConfigCore.itemsToTurnInForNextTier[currentTier].contains(","))
        {
            String[] items = ConfigCore.itemsToTurnInForNextTier[currentTier].split(",");
            for(int i = 0; i < items.length; i++)
            {
                String itemToTurnIn = items[i];

                if(metadata <= 0)
                {
                    String itemAmount = "";
                    if(itemToTurnIn.contains("/"))
                    {
                        String[] itemAndAmount = itemToTurnIn.split("/", 2);
                        itemToTurnIn = itemAndAmount[0];
                        itemAmount = itemAndAmount[1];
                    }

                    if(itemToTurnIn.equalsIgnoreCase(itemName))
                    {
                        int amountTurnedIn;

                        if(itemsTurnedIn.containsKey(itemName))
                        {
                            amountTurnedIn = itemsTurnedIn.get(itemName);
                            itemsTurnedIn.remove(itemName);
                        } else
                        {
                            amountTurnedIn = 0;
                        }

                        int amountToTurnIn;
                        int amountLeft = 0;

                        if(!itemAmount.equals(""))
                        {
                            amountToTurnIn = Integer.parseInt(itemAmount);
                        } else
                        {
                            amountToTurnIn = 1;
                        }

                        if(amountTurnedIn + amount >= amountToTurnIn)
                        {
                            amountLeft = (amountTurnedIn + amount) - amountToTurnIn;
                            amountTurnedIn = amountToTurnIn;
                        } else
                        {
                            amountTurnedIn += amount;
                        }

                        itemsTurnedIn.put(itemName, amountTurnedIn);

                        return amountLeft;
                    }
                } else if(metadata > 0)
                {
                    String itemAmount = "";
                    if(itemToTurnIn.contains("/"))
                    {
                        String[] itemMetaAndAmount = itemToTurnIn.split("/", 2);
                        itemToTurnIn = itemMetaAndAmount[0];
                        itemAmount = itemMetaAndAmount[1];
                    }

                    if(itemToTurnIn.equalsIgnoreCase(itemName + " " + metadata))
                    {
                        int amountTurnedIn;

                        if(itemsTurnedIn.containsKey(itemName))
                        {
                            amountTurnedIn = itemsTurnedIn.get(itemName);
                            itemsTurnedIn.remove(itemName);
                        } else
                        {
                            amountTurnedIn = 0;
                        }

                        int amountToTurnIn;
                        int amountLeft = 0;

                        if(!itemAmount.equals(""))
                        {
                            amountToTurnIn = Integer.parseInt(itemAmount);
                        } else
                        {
                            amountToTurnIn = 1;
                        }

                        if(amountTurnedIn + amount >= amountToTurnIn)
                        {
                            amountLeft = (amountTurnedIn + amount) - amountToTurnIn;
                            amountTurnedIn = amountToTurnIn;
                        } else
                        {
                            amountTurnedIn += amount;
                        }

                        itemsTurnedIn.put(itemName + " " + metadata, amountTurnedIn);

                        return amountLeft;
                    }
                }
            }
        } else if(!ConfigCore.itemsToTurnInForNextTier[currentTier].equals("") && !ConfigCore.itemsToTurnInForNextTier[currentTier].contains(","))
        {
            String itemToTurnIn = ConfigCore.itemsToTurnInForNextTier[currentTier];

            if(metadata <= 0)
            {
                String itemAmount = "";
                if(itemToTurnIn.contains("/"))
                {
                    String[] itemAndAmount = itemToTurnIn.split("/", 2);
                    itemToTurnIn = itemAndAmount[0];
                    itemAmount = itemAndAmount[1];
                }

                if(itemToTurnIn.equalsIgnoreCase(itemName))
                {
                    int amountTurnedIn;

                    if(itemsTurnedIn.containsKey(itemName))
                    {
                        amountTurnedIn = itemsTurnedIn.get(itemName);
                        itemsTurnedIn.remove(itemName);
                    } else
                    {
                        amountTurnedIn = 0;
                    }

                    int amountToTurnIn;
                    int amountLeft = 0;

                    if(!itemAmount.equals(""))
                    {
                        amountToTurnIn = Integer.parseInt(itemAmount);
                    } else
                    {
                        amountToTurnIn = 1;
                    }

                    if(amountTurnedIn + amount >= amountToTurnIn)
                    {
                        amountLeft = (amountTurnedIn + amount) - amountToTurnIn;
                        amountTurnedIn = amountToTurnIn;
                    } else
                    {
                        amountTurnedIn += amount;
                    }

                    itemsTurnedIn.put(itemName, amountTurnedIn);

                    return amountLeft;
                }
            } else if(metadata > 0)
            {
                String itemAmount = "";
                if(itemToTurnIn.contains("/"))
                {
                    String[] itemMetaAndAmount = itemToTurnIn.split("/", 2);
                    itemToTurnIn = itemMetaAndAmount[0];
                    itemAmount = itemMetaAndAmount[1];
                }

                if(itemToTurnIn.equalsIgnoreCase(itemName + " " + metadata))
                {
                    int amountTurnedIn;

                    if(itemsTurnedIn.containsKey(itemName))
                    {
                        amountTurnedIn = itemsTurnedIn.get(itemName);
                        itemsTurnedIn.remove(itemName);
                    } else
                    {
                        amountTurnedIn = 0;
                    }

                    int amountToTurnIn;
                    int amountLeft = 0;

                    if(!itemAmount.equals(""))
                    {
                        amountToTurnIn = Integer.parseInt(itemAmount);
                    } else
                    {
                        amountToTurnIn = 1;
                    }

                    if(amountTurnedIn + amount >= amountToTurnIn)
                    {
                        amountLeft = (amountTurnedIn + amount) - amountToTurnIn;
                        amountTurnedIn = amountToTurnIn;
                    } else
                    {
                        amountTurnedIn += amount;
                    }

                    itemsTurnedIn.put(itemName + " " + metadata, amountTurnedIn);

                    return amountLeft;
                }
            }
        }

        return amount;
    }

    public static void clearItemsTurnedInForPlayer(EntityPlayer entityPlayer)
    {
        if(ConfigCore.tiersAreServerWide || entityPlayer == null)
        {
            clearItemsTurnedIn();
        }

        itemsTurnedInForPlayer.get(entityPlayer).clear();
    }

    public static void clearItemsTurnedIn()
    {
        itemsTurnedIn.clear();
    }
}