package io.github.Theray070696.tiersystem.client.gui.inventory;

import cpw.mods.fml.common.registry.GameData;
import io.github.Theray070696.tiersystem.block.tile.TileTierDeliverySystem;
import io.github.Theray070696.tiersystem.configuration.ConfigCore;
import io.github.Theray070696.tiersystem.inventory.ContainerTierDeliverySystem;
import io.github.Theray070696.tiersystem.lib.Textures;
import io.github.Theray070696.tiersystem.tier.TierHandler;
import io.github.Theray070696.tiersystem.util.ArrayUtilities;
import io.github.Theray070696.tiersystem.util.LogHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Theray on 10/20/14.
 */
@SuppressWarnings({"unused", "unchecked"})
public class GuiTierDeliverySystem extends GuiContainer
{

    private TileTierDeliverySystem tierDeliverySystem;

    private int pageCount = 1;
    private int currentPage = 1;
    private Map<Integer, List<ItemStack>> itemStacksOnPage = new HashMap<Integer, List<ItemStack>>();
    private Map<Integer, List<ItemStack>> itemStacksOnPageOld = new HashMap<Integer, List<ItemStack>>();
    private boolean hasRunPageCheck = false;

    public GuiTierDeliverySystem(ContainerTierDeliverySystem container)
    {
        super(container);
        this.tierDeliverySystem = container.tierDeliverySystem;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = this.tierDeliverySystem.hasCustomInventoryName() ? this.tierDeliverySystem.getInventoryName() : I18n.format(this.tierDeliverySystem.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if(button.id == 0)
        {
            this.currentPage--;
            this.hasRunPageCheck = false;
        } else if(button.id == 1)
        {
            this.currentPage++;
            this.hasRunPageCheck = false;
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Textures.tierDeliverySystemGuiTexture);
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    public void updateScreen()
    {
        super.updateScreen();

        if(ConfigCore.itemsToTurnInForNextTier.length > 0)
        {
            if(this.itemStacksOnPage.get(1) == null)
            {
                this.itemStacksOnPage.put(1, new ArrayList<ItemStack>());
            }

            if(ConfigCore.itemsToTurnInForNextTier[TierHandler.getCurrentTierForPlayer(this.mc.thePlayer)].contains(","))
            {
                String[] itemsToTurnIn = ConfigCore.itemsToTurnInForNextTier[TierHandler.getCurrentTierForPlayer(this.mc.thePlayer)].split(",");

                for(int i = 0; i < itemsToTurnIn.length; i++)
                {
                    String itemToTurnIn = itemsToTurnIn[i];
                    String metadataString = "";
                    String amountToTurnIn = "";

                    if(itemToTurnIn.contains(" "))
                    {
                        String[] itemAndMetadata = itemToTurnIn.split(" ", 2);

                        itemToTurnIn = itemAndMetadata[0];
                        metadataString = itemAndMetadata[1];
                    }

                    if(!itemToTurnIn.equals("") && itemToTurnIn.contains("/"))
                    {
                        String[] itemAndAmount = itemToTurnIn.split("/", 2);

                        itemToTurnIn = itemAndAmount[0];
                        amountToTurnIn = itemAndAmount[1];
                    }

                    if(!metadataString.equals("") && metadataString.contains("/"))
                    {
                        String[] metadataAndAmount = metadataString.split("/", 2);

                        metadataString = metadataAndAmount[0];
                        amountToTurnIn = metadataAndAmount[1];
                    }

                    int metadata;
                    int amount;

                    if(!metadataString.equals(""))
                    {
                        metadata = Integer.parseInt(metadataString);
                    } else
                    {
                        metadata = 0;
                    }

                    if(!amountToTurnIn.equals(""))
                    {
                        amount = Integer.parseInt(amountToTurnIn);
                    } else
                    {
                        amount = 1;
                    }

                    Map<String, Integer> itemsTurnedIn;

                    if(TierHandler.getItemsTurnedInForPlayer(this.mc.thePlayer) != null)
                    {
                        itemsTurnedIn = TierHandler.getItemsTurnedInForPlayer(this.mc.thePlayer);
                    } else
                    {
                        itemsTurnedIn = null;
                    }

                    String[] itemsTurnedInString;
                    int[] amountTurnedInInt;

                    if(itemsTurnedIn != null)
                    {
                        if(itemsTurnedIn.keySet().toArray().length > 0 && itemsTurnedIn.values().toArray().length > 0)
                        {
                            itemsTurnedInString = ArrayUtilities.objectArrayToStringArray(itemsTurnedIn.keySet().toArray());
                            amountTurnedInInt = ArrayUtilities.objectArrayToIntArray(itemsTurnedIn.values().toArray());
                        } else
                        {
                            itemsTurnedInString = null;
                            amountTurnedInInt = null;
                        }
                    } else
                    {
                        itemsTurnedInString = null;
                        amountTurnedInInt = null;
                    }

                    if(itemsTurnedInString != null && amountTurnedInInt != null && itemsTurnedIn != null && itemsTurnedInString.length == amountTurnedInInt.length && !itemsTurnedIn.isEmpty())
                    {
                        for(int j = 0; j < itemsTurnedInString.length; j++)
                        {
                            if(itemToTurnIn.equalsIgnoreCase(itemsTurnedInString[j]) && amountTurnedInInt[j] >= amount)
                            {
                                continue;
                            } else if((itemToTurnIn + " " + metadata).equalsIgnoreCase(itemsTurnedInString[j]) && amountTurnedInInt[j] < amount)
                            {
                                int amount1 = amount - amountTurnedInInt[j];

                                ItemStack item = new ItemStack(GameData.getItemRegistry().getObject(itemToTurnIn), amount1, metadata);

                                if(this.itemStacksOnPage.get(this.pageCount).size() >= 8)
                                {
                                    this.pageCount += 1;
                                    this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                                }

                                boolean stackAlreadyOnPage = false;

                                for(int k = 1; k <= this.pageCount; k++)
                                {
                                    if(!stackAlreadyOnPage && this.itemStacksOnPage.get(k) != null)
                                    {
                                        for(int l = 0; l < this.itemStacksOnPage.get(k).size(); l++)
                                        {
                                            if(!stackAlreadyOnPage && this.itemStacksOnPage.get(k).get(l).isItemEqual(item))
                                            {
                                                stackAlreadyOnPage = true;

                                                break;
                                            }
                                        }
                                    }
                                }

                                if(!stackAlreadyOnPage)
                                {
                                    if(this.itemStacksOnPage.get(this.pageCount) != null)
                                    {
                                        this.itemStacksOnPage.get(this.pageCount).add(item);
                                    } else
                                    {
                                        this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                                    }
                                }

                                break;
                            } else if(itemToTurnIn.equalsIgnoreCase(itemsTurnedInString[j]) && amountTurnedInInt[j] < amount)
                            {
                                int amount1 = amount - amountTurnedInInt[j];

                                ItemStack item = new ItemStack(GameData.getItemRegistry().getObject(itemToTurnIn), amount1);

                                if(this.itemStacksOnPage.get(this.pageCount).size() >= 8)
                                {
                                    this.pageCount += 1;
                                    this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                                }

                                boolean stackAlreadyOnPage = false;

                                for(int k = 1; k <= this.pageCount; k++)
                                {
                                    if(!stackAlreadyOnPage && this.itemStacksOnPage.get(k) != null)
                                    {
                                        for(int l = 0; l < this.itemStacksOnPage.get(k).size(); l++)
                                        {
                                            if(!stackAlreadyOnPage && this.itemStacksOnPage.get(k).get(l).isItemEqual(item))
                                            {
                                                stackAlreadyOnPage = true;

                                                break;
                                            }
                                        }
                                    }
                                }

                                if(!stackAlreadyOnPage)
                                {
                                    if(this.itemStacksOnPage.get(this.pageCount) != null)
                                    {
                                        this.itemStacksOnPage.get(this.pageCount).add(item);
                                    } else
                                    {
                                        this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                                    }
                                }

                                break;
                            } else if(!itemsTurnedInString[j].contains(itemToTurnIn) || itemsTurnedIn == null || amountTurnedInInt == null || itemsTurnedInString == null || itemsTurnedIn.isEmpty())
                            {
                                ItemStack item = new ItemStack(GameData.getItemRegistry().getObject(itemToTurnIn), amount);

                                if(this.itemStacksOnPage.get(this.pageCount).size() >= 8)
                                {
                                    this.pageCount += 1;
                                    this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                                }

                                boolean stackAlreadyOnPage = false;

                                for(int k = 1; k <= this.pageCount; k++)
                                {
                                    if(!stackAlreadyOnPage && this.itemStacksOnPage.get(k) != null)
                                    {
                                        for(int l = 0; l < this.itemStacksOnPage.get(k).size(); l++)
                                        {
                                            if(!stackAlreadyOnPage && this.itemStacksOnPage.get(k).get(l).isItemEqual(item))
                                            {
                                                stackAlreadyOnPage = true;

                                                break;
                                            }
                                        }
                                    }
                                }

                                if(!stackAlreadyOnPage)
                                {
                                    if(this.itemStacksOnPage.get(this.pageCount) != null)
                                    {
                                        this.itemStacksOnPage.get(this.pageCount).add(item);
                                    } else
                                    {
                                        this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                                    }
                                }

                                break;
                            }
                        }
                    } else if(itemsTurnedIn == null || amountTurnedInInt == null || itemsTurnedInString == null || itemsTurnedIn.isEmpty())
                    {
                        ItemStack item = new ItemStack(GameData.getItemRegistry().getObject(itemToTurnIn), amount);

                        if(this.itemStacksOnPage.get(this.pageCount).size() >= 8)
                        {
                            this.pageCount += 1;
                            this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                        }

                        boolean stackAlreadyOnPage = false;

                        for(int j = 1; j <= this.pageCount; j++)
                        {
                            if(!stackAlreadyOnPage && this.itemStacksOnPage.get(j) != null)
                            {
                                for(int k = 0; k < this.itemStacksOnPage.get(j).size(); k++)
                                {
                                    if(!stackAlreadyOnPage && this.itemStacksOnPage.get(j).get(k).isItemEqual(item))
                                    {
                                        stackAlreadyOnPage = true;

                                        break;
                                    }
                                }
                            }
                        }

                        if(!stackAlreadyOnPage)
                        {
                            if(this.itemStacksOnPage.get(this.pageCount) != null)
                            {
                                this.itemStacksOnPage.get(this.pageCount).add(item);
                            } else
                            {
                                this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                            }
                        }
                    }
                }
            } else if(!ConfigCore.itemsToTurnInForNextTier[TierHandler.getCurrentTierForPlayer(this.mc.thePlayer)].equals("") && !ConfigCore.itemsToTurnInForNextTier[TierHandler.getCurrentTierForPlayer(this.mc.thePlayer)].contains(","))
            {
                String itemToTurnIn = ConfigCore.itemsToTurnInForNextTier[TierHandler.getCurrentTierForPlayer(this.mc.thePlayer)];
                String metadataString = "";
                String amountToTurnIn = "";

                if(itemToTurnIn.contains(" "))
                {
                    String[] itemAndMetadata = itemToTurnIn.split(" ", 2);

                    itemToTurnIn = itemAndMetadata[0];
                    metadataString = itemAndMetadata[1];
                }

                if(!itemToTurnIn.equals("") && itemToTurnIn.contains("/"))
                {
                    String[] itemAndAmount = itemToTurnIn.split("/", 2);

                    itemToTurnIn = itemAndAmount[0];
                    amountToTurnIn = itemAndAmount[1];
                }

                if(!metadataString.equals("") && metadataString.contains("/"))
                {
                    String[] metadataAndAmount = metadataString.split("/", 2);

                    metadataString = metadataAndAmount[0];
                    amountToTurnIn = metadataAndAmount[1];
                }

                int metadata;
                int amount;

                if(!metadataString.equals(""))
                {
                    metadata = Integer.parseInt(metadataString);
                } else
                {
                    metadata = 0;
                }

                if(!amountToTurnIn.equals(""))
                {
                    amount = Integer.parseInt(amountToTurnIn);
                } else
                {
                    amount = 1;
                }

                Map<String, Integer> itemsTurnedIn;

                if(TierHandler.getItemsTurnedInForPlayer(this.mc.thePlayer) != null)
                {
                    itemsTurnedIn = TierHandler.getItemsTurnedInForPlayer(this.mc.thePlayer);
                } else
                {
                    itemsTurnedIn = null;
                }

                String[] itemsTurnedInString;
                int[] amountTurnedInInt;

                if(itemsTurnedIn != null)
                {
                    if(itemsTurnedIn.keySet().toArray().length > 0 && itemsTurnedIn.values().toArray().length > 0)
                    {
                        itemsTurnedInString = ArrayUtilities.objectArrayToStringArray(itemsTurnedIn.keySet().toArray());
                        amountTurnedInInt = ArrayUtilities.objectArrayToIntArray(itemsTurnedIn.values().toArray());
                    } else
                    {
                        itemsTurnedInString = null;
                        amountTurnedInInt = null;
                    }
                } else
                {
                    itemsTurnedInString = null;
                    amountTurnedInInt = null;
                }

                if(itemsTurnedInString != null && amountTurnedInInt != null && itemsTurnedIn != null && itemsTurnedInString.length == amountTurnedInInt.length && !itemsTurnedIn.isEmpty())
                {
                    for(int i = 0; i < itemsTurnedInString.length; i++)
                    {
                        if(itemToTurnIn.equalsIgnoreCase(itemsTurnedInString[i]) && amountTurnedInInt[i] >= amount)
                        {
                            continue;
                        } else if((itemToTurnIn + " " + metadata).equalsIgnoreCase(itemsTurnedInString[i]) && amountTurnedInInt[i] < amount)
                        {
                            int amount1 = amount - amountTurnedInInt[i];

                            ItemStack item = new ItemStack(GameData.getItemRegistry().getObject(itemToTurnIn), amount1, metadata);

                            if(this.itemStacksOnPage.get(this.pageCount).size() >= 8)
                            {
                                this.pageCount += 1;
                                this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                            }

                            boolean stackAlreadyOnPage = false;

                            for(int j = 1; j <= this.pageCount; j++)
                            {
                                if(!stackAlreadyOnPage && this.itemStacksOnPage.get(j) != null)
                                {
                                    for(int k = 0; k < this.itemStacksOnPage.get(j).size(); k++)
                                    {
                                        if(!stackAlreadyOnPage && this.itemStacksOnPage.get(j).get(k).isItemEqual(item))
                                        {
                                            stackAlreadyOnPage = true;

                                            break;
                                        }
                                    }
                                }
                            }

                            if(!stackAlreadyOnPage)
                            {
                                if(this.itemStacksOnPage.get(this.pageCount) != null)
                                {
                                    this.itemStacksOnPage.get(this.pageCount).add(item);
                                } else
                                {
                                    this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                                }
                            }

                            break;
                        } else if(itemToTurnIn.equalsIgnoreCase(itemsTurnedInString[i]) && amountTurnedInInt[i] < amount)
                        {
                            int amount1 = amount - amountTurnedInInt[i];

                            ItemStack item = new ItemStack(GameData.getItemRegistry().getObject(itemToTurnIn), amount1);

                            if(this.itemStacksOnPage.get(this.pageCount).size() >= 8)
                            {
                                this.pageCount += 1;
                                this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                            }

                            boolean stackAlreadyOnPage = false;

                            for(int j = 1; j <= this.pageCount; j++)
                            {
                                if(!stackAlreadyOnPage && this.itemStacksOnPage.get(j) != null)
                                {
                                    for(int k = 0; k < this.itemStacksOnPage.get(j).size(); k++)
                                    {
                                        if(!stackAlreadyOnPage && this.itemStacksOnPage.get(j).get(k).isItemEqual(item))
                                        {
                                            stackAlreadyOnPage = true;

                                            break;
                                        }
                                    }
                                }
                            }

                            if(!stackAlreadyOnPage)
                            {
                                if(this.itemStacksOnPage.get(this.pageCount) != null)
                                {
                                    this.itemStacksOnPage.get(this.pageCount).add(item);
                                }
                            }

                            break;
                        } else if(!itemsTurnedInString[i].contains(itemToTurnIn) || itemsTurnedInString == null || amountTurnedInInt == null || itemsTurnedIn == null || itemsTurnedIn.isEmpty())
                        {
                            ItemStack item = new ItemStack(GameData.getItemRegistry().getObject(itemToTurnIn), amount);

                            if(this.itemStacksOnPage.get(this.pageCount).size() >= 8)
                            {
                                this.pageCount += 1;
                                this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                            }

                            boolean stackAlreadyOnPage = false;

                            for(int j = 1; j <= this.pageCount; j++)
                            {
                                if(!stackAlreadyOnPage && this.itemStacksOnPage.get(j) != null)
                                {
                                    for(int k = 0; k < this.itemStacksOnPage.get(j).size(); k++)
                                    {
                                        if(!stackAlreadyOnPage && this.itemStacksOnPage.get(j).get(k).isItemEqual(item))
                                        {
                                            stackAlreadyOnPage = true;

                                            break;
                                        }
                                    }
                                }
                            }

                            if(!stackAlreadyOnPage)
                            {
                                if(this.itemStacksOnPage.get(this.pageCount) != null)
                                {
                                    this.itemStacksOnPage.get(this.pageCount).add(item);
                                } else
                                {
                                    this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                                }
                            }

                            break;
                        }
                    }
                } else if(itemsTurnedIn == null || amountTurnedInInt == null || itemsTurnedInString == null || itemsTurnedIn.isEmpty())
                {
                    ItemStack item = new ItemStack(GameData.getItemRegistry().getObject(itemToTurnIn), amount);

                    if(this.itemStacksOnPage.get(this.pageCount).size() >= 8)
                    {
                        this.pageCount += 1;
                        this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                    }

                    boolean stackAlreadyOnPage = false;

                    for(int i = 1; i <= this.pageCount; i++)
                    {
                        if(!stackAlreadyOnPage && this.itemStacksOnPage.get(i) != null)
                        {
                            for(int j = 0; j < this.itemStacksOnPage.get(i).size(); j++)
                            {
                                if(!stackAlreadyOnPage && this.itemStacksOnPage.get(i).get(j).isItemEqual(item))
                                {
                                    stackAlreadyOnPage = true;

                                    break;
                                }
                            }
                        }
                    }

                    if(!stackAlreadyOnPage)
                    {
                        if(this.itemStacksOnPage.get(this.pageCount) != null)
                        {
                            this.itemStacksOnPage.get(this.pageCount).add(item);
                        } else
                        {
                            this.itemStacksOnPage.put(this.pageCount, new ArrayList<ItemStack>());
                        }
                    }
                }
            }

            if(this.itemStacksOnPage.get(this.currentPage) != null && !ArrayUtilities.haveSameElements(this.itemStacksOnPage.get(this.currentPage), this.itemStacksOnPageOld.get(this.currentPage)))
            {
                int itemsLeftXPos = 101;
                int itemsLeftYPos = 22;

                for(int i = 0; i < this.itemStacksOnPage.get(this.currentPage).size(); i++)
                {
                    ItemStack itemStack = this.itemStacksOnPage.get(this.currentPage).get(i).copy();

                    if(itemStack != null)
                    {
                        itemRender.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemStack, itemsLeftXPos, itemsLeftYPos);
                        itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemStack, itemsLeftXPos, itemsLeftYPos);

                        if(itemsLeftXPos + 16 >= 161)
                        {
                            LogHelper.info("x1 " + itemsLeftXPos);
                            LogHelper.info("y1 " + itemsLeftYPos);

                            itemsLeftXPos = 101;
                            itemsLeftYPos += 16;

                            LogHelper.info("x2 " + itemsLeftXPos);
                            LogHelper.info("y2 " + itemsLeftYPos);
                        } else
                        {
                            LogHelper.info("x1 " + itemsLeftXPos);
                            itemsLeftXPos += 16;
                            LogHelper.info("x2 " + itemsLeftXPos);
                        }
                    }
                }
            } else if(this.itemStacksOnPage.get(this.currentPage) == null)
            {
                this.itemStacksOnPage.put(this.currentPage, new ArrayList<ItemStack>());
            }

            if(!this.hasRunPageCheck)
            {
                this.buttonList.clear();

                int x = (this.width - xSize) / 2;
                int y = (this.height - ySize) / 2;

                GuiButton previousPageButton = new GuiButton(0, x + 120, y + 60, 15, 15, "<");
                GuiButton nextPageButton = new GuiButton(1, x + 140, y + 60, 15, 15, ">");

                if(this.currentPage <= 1)
                {
                    previousPageButton.enabled = false;
                }

                if(this.currentPage >= this.pageCount)
                {
                    nextPageButton.enabled = false;
                }

                this.buttonList.add(previousPageButton);
                this.buttonList.add(nextPageButton);

                this.hasRunPageCheck = true;
            }

            this.itemStacksOnPageOld = this.itemStacksOnPage;
        }
    }
}