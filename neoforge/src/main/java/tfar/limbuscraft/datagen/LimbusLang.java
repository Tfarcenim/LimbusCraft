package tfar.limbuscraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.neoforge.common.data.LanguageProvider;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.block.LimbusTableBlock;

public class LimbusLang extends LanguageProvider {
    public LimbusLang(PackOutput output) {
        super(output, LimbusCraft.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addTextComponent(LimbusTableBlock.CONTAINER_TITLE,"Mirror");
    }

    protected void addTextComponent(MutableComponent component, String text) {
        ComponentContents contents = component.getContents();
        if (contents instanceof TranslatableContents translatableContents) {
            add(translatableContents.getKey(),text);
        } else {
            throw new UnsupportedOperationException(component +" is not translatable");
        }
    }

}
