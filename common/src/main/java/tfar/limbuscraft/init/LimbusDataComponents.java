package tfar.limbuscraft.init;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import tfar.limbuscraft.LimbusCraft;

import java.util.function.UnaryOperator;

public class LimbusDataComponents {


    public static final DataComponentType<Unit> LIMBUS_WEAPON = register(
            "limbus_weapon", p_344188_ -> p_344188_.persistent(Unit.CODEC)
                    .networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, LimbusCraft.id(name), builder.apply(DataComponentType.builder()).build());
    }

    public static void init() {

    }

}
