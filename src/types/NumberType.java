package types;

/**
 * Stores a floating point number.
 * Also implements all possible operations with number types.
 *
 * @author João Felisberto
 */
public class NumberType extends Type<Double> {
    public NumberType(double data, String name) {
        super(Types.NUMBER, data, name);
    }

    public NumberType(String data, String name) {
        super(Types.NUMBER, Double.valueOf(data), name);
    }

    public NumberType add(TextType text) {
        return new NumberType(getData() + Double.parseDouble(text.getData()), null);
    }

    public NumberType add(NumberType num) {
        return new NumberType(getData() + num.getData(), null);
    }

    public NumberType sub(NumberType num) {
        return new NumberType(getData() - num.getData(), null);
    }

    public NumberType sub(TextType text) {
        return new NumberType(getData() - Double.parseDouble(text.getData()), null);
    }

    public NumberType mul(NumberType num) {
        return new NumberType(getData() * num.getData(), null);
    }

    public NumberType eq(NumberType num) {
        return getData().equals(num.getData()) ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType eq(TextType num) {
        return getData() == num.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType eq(ListType<?> num) {
        return getData() == num.getData().size() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ne(NumberType num) {
        return !getData().equals(num.getData()) ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ne(TextType num) {
        return getData() != num.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ne(ListType<?> num) {
        return getData() != num.getData().size() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType lt(NumberType num) {
        return getData() < num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType lt(TextType num) {
        return getData() < num.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType lt(ListType<?> num) {
        return getData() < num.getData().size() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType gt(NumberType num) {
        return getData() > num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType gt(TextType num) {
        return getData() > num.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType gt(ListType<?> num) {
        return getData() > num.getData().size() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType le(NumberType num) {
        return getData() <= num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType le(TextType num) {
        return getData() <= num.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType le(ListType<?> num) {
        return getData() <= num.getData().size() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ge(NumberType num) {
        return getData() >= num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ge(TextType num) {
        return getData() >= num.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ge(ListType<?> num) {
        return getData() >= num.getData().size() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType and(NumberType num) {
        return (getData() == 1 && num.getData() == 1) ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType or(NumberType num) {
        return (getData() == 1 || num.getData() == 1) ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType postInc() {
        setData(getData()+1);
        return new NumberType(getData()-1, null);
    }

    public NumberType preInc() {
        setData(getData()+1);
        return new NumberType(getData(), null);
    }

    public NumberType postDec() {
        setData(getData()-1);
        return new NumberType(getData()+1, null);
    }

    public NumberType preDec() {
        setData(getData()-1);
        return new NumberType(getData(), null);
    }
}
