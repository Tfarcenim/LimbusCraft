package tfar.limbuscraft.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.world.LimbusTableMenu;

public class LimbusTableScreen extends AbstractContainerScreen<LimbusTableMenu> {

    private static final ResourceLocation CRAFTING_TABLE_LOCATION = LimbusCraft.id("textures/gui/mirror.png");

    private Tab selectedTab = Tab.MAIN;

    public enum Tab {
        MAIN,SECONDARY;
    }

    public LimbusTableScreen(LimbusTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    Button tab0;
    Button tab1;

    Button button;
    Button[] buttons = new Button[6];


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

        button.visible = selectedTab == Tab.MAIN;
        addRenderableWidget(button);

        for (int i = 0; i < 6;i++) {
            buttons[i] = Button.builder(Component.literal(i+""),b ->{})
                    .bounds(leftPos+145,topPos+6+12 * i,20,12).build();

            buttons[i].visible = selectedTab == Tab.SECONDARY;
            addRenderableWidget(buttons[i]);
        }
    }

    private void sendButtonClick(LimbusTableMenu.ButtonUsed pageData) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, pageData.ordinal());
    }

    void setVisibility() {
        button.visible = selectedTab == Tab.MAIN;
        for (int i = 0; i < 6;i++) {
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
        guiGraphics.blit(CRAFTING_TABLE_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);

    }
}
