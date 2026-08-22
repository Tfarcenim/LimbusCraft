package tfar.limbuscraft.client;

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
    protected void init() {
        super.init();

        ResourceLocation sprite = LimbusCraft.id("tab");

        Button tab0 = SpriteIconButton.builder(Component.empty(),b -> {},true)
                .size(26, 32)
                .sprite(sprite,26,64)
                .build();

        tab0.setX(leftPos);
        addRenderableWidget(tab0);

        Button tab1 = SpriteIconButton.builder(Component.empty(),b -> {},true)
                .size(26, 32)
                .sprite(sprite,26,64)
                .build();

        tab1.setX(leftPos+26);
        addRenderableWidget(tab1);
    }

    public static class TabButton extends SpriteIconButton.CenteredIcon {
        protected static final ResourceLocation TAB = LimbusCraft.id("tab");
        protected static final ResourceLocation TAB_SELECTED = LimbusCraft.id("tab_selected");
        protected TabButton(int p_295914_, int p_294852_, Component p_295609_, int p_294922_, int p_296462_, ResourceLocation p_295554_, OnPress p_294427_, @Nullable Button.CreateNarration p_330653_) {
            super(p_295914_, p_294852_, p_295609_, p_294922_, p_296462_, p_295554_, p_294427_, p_330653_);
        }

        @Override
        public void renderWidget(GuiGraphics p_295402_, int p_295733_, int p_294839_, float p_296191_) {
            super.renderWidget(p_295402_, p_295733_, p_294839_, p_296191_);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CRAFTING_TABLE_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);

    }
}
