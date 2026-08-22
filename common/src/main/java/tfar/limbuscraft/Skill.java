package tfar.limbuscraft;

public class Skill {
    public final int basePower;
    public final int coinCount;
    public final int coinPower;
    public final Target target;

    public enum Target {
        SINGLE,AOE
    }

    public Skill(int basePower, int coinCount, int coinPower, Target target) {
        this.basePower = basePower;
        this.coinCount = coinCount;
        this.coinPower = coinPower;
        this.target = target;
    }
}
