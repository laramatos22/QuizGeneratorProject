package types;

/**
 * Handles time multipliers. Useful to convert any time type (for instance, 2m) into seconds for internal use
 *
 * @author João Felisberto
 */
public enum TimeModifiers {
    SECONDS(1), MINUTES(60), HOURS(60*60);

    private final int modifier;

    TimeModifiers(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }
}
