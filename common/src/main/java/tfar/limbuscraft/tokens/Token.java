package tfar.limbuscraft.tokens;

import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class Token {
    private final String id;
    private final TokenType type;

    public Token(String id, TokenType type) {
        this.id = id;
        this.type = type;
    }

    public String id() {
        return id;
    }

    public TokenType type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Token) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "Token[" +
                "id=" + id + ", " +
                "type=" + type + ']';
    }

    public void tick(LivingEntity entity,TokenInstance tokenInstance) {

    }

    public boolean shouldTick(long combatTimer) {
        return false;
    }
}
