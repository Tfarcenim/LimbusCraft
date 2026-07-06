package tfar.limbuscraft.healthbar;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.player.Player;
import tfar.limbuscraft.Color;

public interface ColorProvider {

    Codec<ColorProvider> CODEC = Codec.STRING.xmap(ColorProviderSerializers.MAP::get, ColorProviderSerializer::name)
            .dispatch(ColorProvider::getSerializer, c -> c.codec());

    //note, this CAN go above 1
    Color getColor(Player player, float ratio, int layer);
    ColorProviderSerializer<?> getSerializer();
}
