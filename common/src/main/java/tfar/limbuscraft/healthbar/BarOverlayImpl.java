package tfar.limbuscraft.healthbar;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2i;

import tfar.limbuscraft.Color;
import tfar.limbuscraft.LimbusCraft;

import java.util.Optional;

public abstract class BarOverlayImpl implements BarOverlay {

    //maximum width the bar can be
    public static final int WIDTH = 77;
    public static final int HEIGHT = 5;
    public static final int BAR_U = 2;
    public static final int BAR_V = 11;
    public static final ResourceLocation BACKGROUND = LimbusCraft.id("background");
    public static final ResourceLocation BACKGROUND_FLASH = LimbusCraft.id("background_flash");
    public static final ResourceLocation BAR = LimbusCraft.id("bar");

    public static final ResourceLocation GUI_ICONS_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/icons.png");
    private final BarSettings barSettings;

    protected final boolean dependenciesMet;
    protected boolean errored;

    protected final BarInfo barInfo;

    /**
     */
    protected static <T extends BarOverlayImpl> Products.P1<RecordCodecBuilder.Mu<T>, BarSettings> codecStart(
            RecordCodecBuilder.Instance<T> instance) {
        return instance.group(BarSettings.CODEC.fieldOf("bar_settings").forGetter(BarOverlayImpl::getBarSettings));
    }


    protected BarOverlayImpl(BarInfo barInfo, BarSettings barSettings) {
        this.barInfo = barInfo;
        this.barSettings = barSettings;
        dependenciesMet = barInfo.checkDependencies();
    }

    public static int getWidth(double d1, double d2) {
      double ratio = WIDTH * d1 / d2;
      return (int)Math.ceil(ratio);
    }

    public BarSettings getBarSettings() {
        return barSettings;
    }

    public final boolean shouldRender(Player player) {
        return canRender() && barInfo.shouldRender().test(player);
    }

    public final boolean canRender() {
        return !errored() && barSettings.enabled() && dependenciesMet;
    }

    @Override
    public final BarSide getSide() {
        return barSettings.side();
    }

    @Override
    public boolean render(Gui gui, GuiGraphics graphics, Player player, int vOffset) {
        if (shouldRender(player)) {
            //gui.setupOverlayRenderState(true, false);
            renderBar(gui, graphics, player, vOffset);
            renderBarDecorations(gui, graphics, player, vOffset);
            Color.reset();//don't leak colors
            if (barSettings.show_text()) {
                renderText(graphics, player, vOffset);
            }
            if (barSettings.show_icon()) {
                renderIcon(graphics, player,  vOffset);
            }
            return true;
        } return false;
    }

    public void renderBar(Gui gui, GuiGraphics graphics, Player player,  int vOffset) {
        renderSimpleBar(barSettings.colorProvider().getColor(player,barInfo.getRatio(player),0),graphics, player, vOffset);
    }

    public void renderBarDecorations(Gui gui, GuiGraphics graphics, Player player, int vOffset) {

    }

    public void renderText(GuiGraphics graphics, Player player,int vOffset) {
        int text = (int)barInfo.numerator().getValue(player);
        int xStart = graphics.guiWidth() / 2 + getIconOffset();
        int yStart = graphics.guiHeight() - vOffset;
        textHelper(graphics,xStart,yStart,text,barSettings.colorProvider().getColor(player,barInfo.getRatio(player) , 0).colorToText());
    }

    public void renderIcon(GuiGraphics graphics, Player player, int vOffset) {
        renderSimpleIcon(graphics, vOffset);
    }

    public void renderSimpleIcon(GuiGraphics graphics, int vOffset) {
        int xStart = graphics.guiWidth() / 2 + getIconOffset();
        int yStart = graphics.guiHeight() - vOffset;

        for (int i = 0; i < barInfo.icon_data().uvs().size(); i++) {
            Vector2i uv = barInfo.icon_data().uvs().get(i);
            graphics.blitSprite(getIconRL(),xStart, yStart, uv.x, uv.y);
        }
    }

    public int getHOffset() {
        return switch (getSide()){
            case LEFT -> -91;
            case RIGHT -> 10;
        };

    }

    public int getIconOffset() {
        return switch (getSide()) {
            case LEFT -> -101;
            case RIGHT -> 92;
        };
    }

    protected void renderBarBackground(GuiGraphics graphics, Player player, int vOffset) {
        renderBarBackground(graphics, player, vOffset,false);
    }

    public void renderFlashBarBackground(GuiGraphics graphics, Player player, int vOffset) {
        renderBarBackground(graphics, player, vOffset,true);
    }

    protected void renderBarBackground(GuiGraphics graphics, Player player, int vOffset,boolean flash) {
        double barWidth = getBarWidth(player);
        int xStart = graphics.guiWidth() / 2 + getHOffset();
        if (isFitted() && getSide() == BarSide.RIGHT) {
            xStart += WIDTH - barWidth;
        }
        int yStart = graphics.guiHeight() - vOffset;

        if (isFitted()) {
            drawScaledBarBackground(graphics, barWidth, xStart, yStart + 1,flash);
        } else renderFullBarBackground(graphics, xStart, yStart,flash);
    }

    private void drawScaledBarBackground(GuiGraphics stack, double barWidth, int x, int y, boolean flash) {
        ResourceLocation texture = flash ? BACKGROUND_FLASH : BACKGROUND;
        switch (getSide()) {
            case LEFT -> {
                stack.blitSprite(texture,81,9,x, y - 1, 0, flash ? 18 : 0, (int) (barWidth + 2), 9);
                stack.blitSprite(texture,81,9, (int) (x + barWidth + 2), y - 1, WIDTH + 2, flash ? 18 : 0, 2, 9);
            }
            case RIGHT -> {
                stack.blitSprite(texture,81,9,x, y - 1, 0, flash ? 18 : 0, (int) (barWidth + 2), 9);
                stack.blitSprite(texture,81,9, (int) (x + barWidth + 2), y-1, WIDTH + 2, flash ? 18 : 0, 2, 9);
            }
        }
    }
    public void textHelper(GuiGraphics graphics,int xStart,int yStart,double stat, int color) {
        int i1 = (int) Math.floor(stat);
        int i2 = barSettings.show_icon() ? 1 : 0;

        switch (getSide()) {
            case LEFT -> {
                int i3 = Minecraft.getInstance().font.width(i1 + "");
                graphics.drawString(Minecraft.getInstance().font, i1 + "", xStart - 9 * i2 - i3 + 5, yStart + 1, color);
            }
            case RIGHT -> {
                graphics.drawString(Minecraft.getInstance().font, i1 + "", xStart + 9 * i2, yStart + 1, color);
            }
        }
    }

    protected int getXStartBar(int screenWidth,int barWidth){
        int xStart = screenWidth / 2 + getHOffset();
        if (getSide() == BarSide.RIGHT) xStart += (WIDTH - barWidth);
        return xStart;
    }

    private void renderFullBarBackground(GuiGraphics matrices, int xStart, int yStart, boolean flash) {
        ResourceLocation texture = flash ? BACKGROUND_FLASH : BACKGROUND;
        matrices.blitSprite(texture,81,9, 0, 0,xStart, yStart, WIDTH + 4, 9);

    }

    public void renderFullBar(Color color,GuiGraphics matrices, int xStart, int yStart) {
        renderPartialBar(color,matrices,xStart,yStart,WIDTH);
    }

    protected void renderSimpleBar(Color color, GuiGraphics graphics, Player player, int vOffset) {
        renderSimpleBar(color,graphics,player,vOffset,false);
    }

    protected void renderSimpleBar(Color color, GuiGraphics graphics, Player player, int vOffset,boolean highlight) {
        int barWidth = getBarWidth(player);
        int xStart = getXStartBar(graphics.guiWidth(),barWidth);
        int yStart = graphics.guiHeight() - vOffset;

        //Bar background
        renderBarBackground(graphics,player,vOffset,highlight);
        //draw portion of bar based on feathers amount
        renderPartialBar(color,graphics,xStart+2,yStart+2,barWidth);
    }

    public void renderPartialBar(Color color,GuiGraphics matrices, int xStart, int yStart,double barWidth) {
        color.color2Gl();
        matrices.blitSprite(BAR,WIDTH,HEIGHT, 0,0,xStart, yStart, (int) barWidth, HEIGHT);
    }

    public ResourceLocation getIconRL() {
        return barSettings.icon();
    }

    public final int getBarWidth(Player player) {
        return (int) Math.ceil(WIDTH* barInfo.getRatio(player));
    }

    @FunctionalInterface
    public interface Numerator {
        float getValue(Player player);
    }

    @FunctionalInterface
    public interface Denominator {
        float getValue(Player player);
    }

    @Override
    public boolean dependenciesMet() {
        return dependenciesMet;
    }

    protected static Denominator fixed(float value) {
        return p -> value;
    }

    public final boolean isFitted() {
        return barSettings.fitted();
    }
    @Override
    public final String name() {
        return barInfo.name();
    }

    public final boolean errored() {
        return errored;
    }

    public void setErrored() {
        this.errored = true;
    }

    @Override
    public final Optional<ResourceLocation> disablesOverlay() {
        return barSettings.disablesOverlay();
    }
}
