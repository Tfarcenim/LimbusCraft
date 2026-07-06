package tfar.limbuscraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import tfar.limbuscraft.LimbusCraft;

@Mod(value = LimbusCraft.MOD_ID,dist = Dist.CLIENT)
public class LimbusCraftClientNeoForge {

    public LimbusCraftClientNeoForge(IEventBus bus) {
        bus.addListener(this::registerOverlay);
        NeoForge.EVENT_BUS.addListener(this::renderOverlayEvent);
    }

    void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH,LimbusCraft.id("sanity"),LimbusGuiLayers.SANITY);
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH,LimbusCraft.id("health"),LimbusGuiLayers.HEALTH_);
    }

    void renderOverlayEvent(RenderGuiLayerEvent.Pre event) {
        if (event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH)) {
            event.setCanceled(true);
        }
    }
}
