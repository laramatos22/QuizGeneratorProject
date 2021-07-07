package types;

/**
 * @author João Felisberto
 */
public class TimeType extends Type<Integer> {

    private final TimeModifiers mod;

    /**
     * Creates a variable object with all data it contains
     *
     * @param data The data it stores
     * @param name The variable name
     * @param mod  The time modifier. This will
     */
    public TimeType(Integer data, TimeModifiers mod, String name) {
        super(Types.TIME, data, name);
        this.mod = mod;
    }

    public TimeType add(TimeType type) {
        final TimeType less = mod.getModifier() > type.mod.getModifier() ? type : this;
        final TimeType more = mod.getModifier() <= type.mod.getModifier() ? type : this;

        return new TimeType(less.getData() + (more.getData()*(more.getMod().getModifier()/less.getMod().getModifier())),
                less.getMod(), null);
    }

    public TimeType sub(TimeType type) {
        final TimeType less = mod.getModifier() > type.mod.getModifier() ? type : this;
        final TimeType more = mod.getModifier() <= type.mod.getModifier() ? type : this;

        return new TimeType(less.getData() - (more.getData()*(more.getMod().getModifier()/less.getMod().getModifier())),
                less.getMod(), null);
    }

    public TimeModifiers getMod() {
        return mod;
    }
}
