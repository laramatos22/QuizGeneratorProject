package types;

/**
 * Represents a text variable and implements all possible operations.
 * Operating with numbers will convert the numbers into strings and then operate between strings.
 * Multiplication by a number n will replicate the string n times, just like in python
 * Comparing with a number will compare the string size with the number, comparing with string will compare the string sizes
 *
 * @author João Felisberto
 */
public class TextType extends Type<String> {
    public TextType(String data, String name) {
        super(Types.TEXT, data, name);
    }

    public TextType add(TextType text) {
        return new TextType(getData() + text.getData(), null);
    }

    public TextType add(NumberType num) {
        return new TextType(getData() + num.getData(), null);
    }

    public TextType mul(NumberType num) {
        final StringBuilder sb = new StringBuilder(getData());

        for (int i = 0; i < num.getData(); i++)
            sb.append(getData());

        return new TextType(sb.toString(), null);
    }

    public NumberType eq(NumberType number) {
        return getData().length() == (number.getData()) ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ne(NumberType number) {
        return getData().length() != (number.getData()) ? new NumberType(0, null) : new NumberType(1, null);
    }

    public NumberType lt(NumberType number) {
        return getData().length() < number.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType gt(NumberType number) {
        return getData().length() > number.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType le(NumberType number) {
        return getData().length() <= number.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ge(NumberType number) {
        return getData().length() >= number.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType eq(TextType text) {
        return getData().equals(text.getData()) ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ne(TextType text) {
        return getData().equals(text.getData()) ? new NumberType(0, null) : new NumberType(1, null);
    }

    public NumberType lt(TextType text) {
        return getData().length() < text.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType gt(TextType text) {
        return getData().length() > text.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType le(TextType text) {
        return getData().length() <= text.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ge(TextType text) {
        return getData().length() >= text.getData().length() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public TextType subString(int[] indexes) {
        return new TextType(getData().substring(indexes[0], indexes[2]), null);
    }
}
