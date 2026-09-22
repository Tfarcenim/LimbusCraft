package tfar.limbuscraft.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.LimbusPlayerUpgrade;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.world.LimbusTableMenu;

import java.util.List;
import java.util.Map;

public class LimbusTableScreen extends AbstractContainerScreen<LimbusTableMenu> {

    private static final ResourceLocation BACKGROUND = LimbusCraft.id("background");
    private static final ResourceLocation INVENTORY_SLOTS = LimbusCraft.id("inventory_slots");
    private static final ResourceLocation LARGE_SLOT = LimbusCraft.id("large_slot");

    private Tab selectedTab = Tab.MAIN;

    public enum Tab {
        MAIN,SECONDARY;
    }

    public LimbusTableScreen(LimbusTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageHeight = 200;
        inventoryLabelY = menu.inventoryY - 11;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        switch (selectedTab) {
            case MAIN -> {
            guiGraphics.drawString(this.font, Component.literal("Slot"),
                    32, 20, 0x404040, false);
            guiGraphics.drawString(this.font, Component.literal("" + menu.dataSlot.get()),
                    40, 40, 0x404040, false);
            }

            case SECONDARY -> {
                int i = 0;
                List<LimbusPlayerUpgrade> ordered = LimbusPlayerUpgrade.ordered();
                Map<LimbusPlayerUpgrade, LimbusPlayerUpgrade.Instance> instances = DataAttachmentUtil.getPlayerUpgrades(minecraft.player);


                for (int j = 0; j < ordered.size(); j++) {
                    LimbusPlayerUpgrade value = ordered.get(j);

                    LimbusPlayerUpgrade.Instance instance = instances.get(value);

                    int curLevel = instance != null ? instance.levels() : 0;

                    int y = 20 + i * 16;

                    guiGraphics.drawString(this.font, Component.translatable(value.attribute().value().getDescriptionId()),
                            6, y, 0x404040, false);


                    guiGraphics.drawString(this.font, Component.literal("level: " + curLevel),
                            80, y, 0x404040, false);

                    guiGraphics.drawString(this.font, Component.literal("+" + value.factor()),
                            145, y, 0x404040, false);
                    i++;
                }
            }
        }
    }

    Button tab0;
    Button tab1;

    Button button;

    Button buttonLeft;
    Button buttonRight;

    static int buttonCount = 5;

    Button[] buttons = new Button[buttonCount];


    @Override
    protected void init() {
        super.init();

        int a = 28;

        tab0 = new TabButton(leftPos,topPos-a,26,32,Component.empty(),b -> {selectedTab = Tab.MAIN;
            setVisibility();},Tab.MAIN);
        addRenderableWidget(tab0);

        tab1 = new TabButton(leftPos+28,topPos-a,26,32,Component.empty(),b -> {selectedTab = Tab.SECONDARY;
            setVisibility();},Tab.SECONDARY);
        addRenderableWidget(tab1);

        button = Button.builder(Component.empty(),b -> sendButtonClick(LimbusTableMenu.ButtonUsed.TRANSFORM))
                .bounds(leftPos+78,topPos+56,20,20).build();

        buttonLeft = Button.builder(Component.literal("<"),b -> sendButtonClick(LimbusTableMenu.ButtonUsed.LEFT_SLOT))
                .bounds(leftPos+16,topPos+36,16,16).build();

        buttonRight = Button.builder(Component.literal(">"),b -> sendButtonClick(LimbusTableMenu.ButtonUsed.RIGHT_SLOT))
                .bounds(leftPos+52,topPos+36,16,16).build();

        addRenderableWidget(buttonLeft);
        addRenderableWidget(buttonRight);

        addRenderableWidget(button);

        for (int i = 0; i < buttons.length;i++) {

            int finalI = i;
            buttons[i] = Button.builder(Component.literal("+"), b -> upgradeStat(b, finalI))
                    .bounds(leftPos+imageWidth - 50,topPos+16+16 * i,16,14).build();

            addRenderableWidget(buttons[i]);
        }

        setVisibility();
    }

    void upgradeStat(Button b,int stat) {
        sendButtonClick(LimbusTableMenu.ButtonUsed.values()[stat]);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        Map<LimbusPlayerUpgrade, LimbusPlayerUpgrade.Instance> instances = DataAttachmentUtil.getPlayerUpgrades(minecraft.player);
        for (int i = 0; i < buttons.length; i++) {
            Button b = buttons[i];
            LimbusPlayerUpgrade.Instance instance = instances.get(LimbusPlayerUpgrade.byIndex(i));
            b.setTooltip(Tooltip.create(Component.literal("Cost:" + (LimbusPlayerUpgrade.getNextCost(instance != null ?
                    instance.levels():0)))));
        }
    }

    private void sendButtonClick(LimbusTableMenu.ButtonUsed pageData) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, pageData.ordinal());
    }

    void setVisibility() {
        button.visible = buttonLeft.visible = buttonRight.visible = selectedTab == Tab.MAIN;
        for (int i = 0; i < buttons.length;i++) {
            buttons[i].visible = selectedTab == Tab.SECONDARY;
        }
    }

    public class TabButton extends Button {


        protected final Tab tab;

        protected static final ResourceLocation TAB = LimbusCraft.id("tab");
        protected static final ResourceLocation TAB_SELECTED = LimbusCraft.id("tab_selected");

        protected TabButton(int x, int y, int width, int height, Component message, OnPress onPress, Tab tab) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
            this.tab = tab;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            Minecraft minecraft = Minecraft.getInstance();
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            int i = this.getX();
            int j = this.getY();
            ResourceLocation sprite = this.tab == selectedTab ?  TAB_SELECTED : TAB;
            guiGraphics.blitSprite(sprite, i, j,0, this.width, this.height);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blitSprite(BACKGROUND,leftPos,topPos,
                imageWidth,imageHeight);
        guiGraphics.blitSprite(INVENTORY_SLOTS,leftPos+menu.inventoryX-1,topPos+menu.inventoryY-1,
                162,76);
        if (selectedTab == Tab.MAIN) {
            guiGraphics.blitSprite(LARGE_SLOT,leftPos+80-5,topPos+35-5, 26,26);
        }
    }
}
