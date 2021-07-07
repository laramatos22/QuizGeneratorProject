package types;

import java.util.ArrayList;
import java.util.List;

/**
 * @author João Felisberto
 */
public interface Cast {

    /**
     * Preforms the cast of any variable to the destination type if possible
     *
     * @param from The original variable
     * @param to   The destination type
     * @return A new variable with the result of the type cast of {@code from}.
     */
    static Type<?> cast(Type<?> from, Types to) {
        if (from == null) {
            System.err.println("Impossible cast");
            return null;
        }

        final Types original = from.getType();

        if (original == to)
            switch (to) {
                case LIST:
                    return new ListType<>(((ListType<?>) from).getData(), null);
                case NUMBER:
                    return new NumberType(((NumberType) from).getData(), null);
                case TEXT:
                    return new TextType(((TextType) from).getData(), null);
                case QUESTION:
                    return new QuestionType(((QuestionType) from).getData(), null);
            }

        if (original == Types.TEXT) {
            switch (to) {
                case LIST:
                    return textToList((TextType) from);
                case NUMBER:
                    return textToNumber((TextType) from);
                default:
                    return null;
            }
        }

        if (original == Types.NUMBER) {
            switch (to) {
                case TEXT:
                    return numToText((NumberType) from);
                default:
                    return null;
            }
        }

        return null;
    }

    static NumberType textToNumber(TextType var) throws NumberFormatException {
//        System.out.println("I am called for " + var.getName() + ": " + var.getData());
        return new NumberType(Double.parseDouble(var.getData()), null);
    }

    static ListType<TextType> textToList(TextType var) throws NumberFormatException {
        final List<TextType> data = new ArrayList<>();

        final char[] letters = var.getData().toCharArray();
        for (char letter : letters)
            data.add(new TextType(String.valueOf(letter), null));

        return new ListType<>(data, var.getName());
    }

    static TextType numToText(NumberType var) {
        return new TextType(String.valueOf(var.getData()), null);
    }
}
