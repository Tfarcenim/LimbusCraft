package tfar.limbuscraft.mixin;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttachmentType.class)
public interface AttachmentTypeAccessor<T> {
    @Accessor
    @Nullable
    IAttachmentSerializer<?, T> getSerializer();
}
