package tfar.limbuscraft.healthbar;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import tfar.limbuscraft.Color;


public class Health extends BarOverlayImpl {

  private double playerHealth = 0;
  private long healthUpdateCounter = 0;
  private double lastPlayerHealth = 0;

  public static final BarInfo INFO = BarInfo.createSimpleVanilla("health",
          player -> true,LivingEntity::getHealth, LivingEntity::getMaxHealth);

  public Health(BarSettings settings) {
    super(INFO,settings);
  }

  public static final Codec<Health> CODEC = RecordCodecBuilder.create(
          objectInstance -> codecStart(objectInstance).apply(objectInstance,Health::new)
  );

  @Override
  public Codec<? extends BarOverlayImpl> codec() {
    return CODEC;
  }

  @Override
  public void renderBar(Gui gui, GuiGraphics graphics, Player player, int vOffset) {
    int updateCounter = gui.getGuiTicks();

    double health = player.getHealth();
    double barWidth = getBarWidth(player);
    boolean highlight = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3 % 2 == 1;

    //player is damaged and resistant
    if (health < playerHealth && player.invulnerableTime > 0) {
      healthUpdateCounter = updateCounter + 20;
      lastPlayerHealth = playerHealth;
    } else if (health > playerHealth && player.invulnerableTime > 0) {
      healthUpdateCounter = updateCounter + 10;
      /* lastPlayerHealth = playerHealth;*/
    }
    playerHealth = health;
    double displayHealth = health + (lastPlayerHealth - health) * ((double) player.invulnerableTime / player.invulnerableDuration);

    int xStart = graphics.guiWidth() / 2 + getHOffset();
    int yStart = graphics.guiHeight() - vOffset;
    double maxHealth = player.getMaxHealth();


      //Bar background
   // renderBarBackground(graphics,player,screenWidth,screenHeight,vOffset,highlight);

    double f = xStart + (getSide() == BarSide.RIGHT ? WIDTH - barWidth : 0);

    //is the bar changing
    //Pass 1, draw bar portion
    //interpolate the bar
    if (displayHealth != health) {
      //reset to white
      if (displayHealth > health) {
        //draw interpolation
        double w = BarOverlayImpl.getWidth(displayHealth, maxHealth);
        double off = getSide() == BarSide.RIGHT ? w - barWidth : 0;
        //draw interpolation
        renderPartialBar(Color.WHITE,graphics, (int) (f + 2 - off), yStart + 2,w);
        //Health is increasing, IDK what to do here
      } else {/*
                  f = xStart + getWidth(health, maxHealth);
                  drawTexturedModalRect(f, yStart + 1, 1, 10, getWidth(health - displayHealth, maxHealth), 7, general.style, true, true);*/
      }
    }
    //draw portion of bar based on health remaining
   // Color primary = getBarSettings().colorProvider().getColor(player, ,0);

    renderSimpleBar(getBarSettings().colorProvider().getColor(player,barInfo.getRatio(player),0), graphics, player,vOffset,highlight);

    HealthEffect effect = HealthEffect.getHealthEffect(player);

    //renderPartialBar(primary,graphics,f + 2, yStart + 2, barWidth);
    if (effect == HealthEffect.POISON) {
      //draw poison overlay
      RenderSystem.setShaderColor(0, .5f, 0, .5f);
      //graphics.blitSprite(getIconRL(),9,9,f + 1, yStart + 1, 1, 36, barWidth, 7);
    }
  }

  @Override
  public void renderIcon(GuiGraphics graphics, Player player, int vOffset) {
    int xStart = graphics.guiWidth() / 2 + getIconOffset();
    int yStart = graphics.guiHeight() - vOffset;
    boolean hardcore = player.level().getLevelData().isHardcore();
    //Draw health icon
    //heart background
    ResourceLocation container = Gui.HeartType.CONTAINER.getSprite(hardcore,false,false);
    graphics.blitSprite(container,xStart, yStart, 9, 9);
    //heart

    Gui.HeartType type = Gui.HeartType.forPlayer(player);

    graphics.blitSprite(type.getSprite(hardcore,false,false),xStart, yStart,9 ,9);
  }
}