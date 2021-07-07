package types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a list of same type variables
 * <p>
 * Addition appends the element to the end of the list, if it can be casted to the list type
 * Subtracting will remove, if present, the element(s) it is subtracting.
 * Subtracting a list will proceed as above for all list elements.
 * <p>
 * Logic AND operation behaves as addition, logic OR behaves as subtraction, for ease of use.
 *
 * @author João Felisberto
 */
public class ListType<T extends Type<?>> extends Type<List<T>> {

    private TextType presentation = null;

    /**
     * Creates a variable object with all data it contains
     *
     * @param data The data it stores
     * @param name The variable name
     */
    public ListType(List<T> data, String name) {
        super(Types.LIST, data, name);
    }

    public ListType(String name, T... data) {
        super(Types.LIST, Arrays.asList(data), name);
    }

    public NumberType eq(NumberType num) {
        return getData().size() == num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ne(NumberType num) {
        return getData().size() != num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType gt(NumberType num) {
        return getData().size() > num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType lt(NumberType num) {
        return getData().size() < num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType ge(NumberType num) {
        return getData().size() >= num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public NumberType le(NumberType num) {
        return getData().size() <= num.getData() ? new NumberType(1, null) : new NumberType(0, null);
    }

    public ListType<?> add(ListType<?> l1) {
        final List data = getData();

        data.addAll(l1.getData());

        return new ListType<>(data, null);
    }

    public ListType<?> add(Type<?> l1) {
        final List data = getData();

        data.add(l1.getData());

        return new ListType<>(data, null);
    }

    public ListType<T> getReversed() {
        final List<T> newL = new ArrayList<>();
        newL.addAll(getData());

        Collections.reverse(newL);

        return new ListType<T>(newL, null);
    }

    public ListType<?> subList(int[] indexes) {
        if (indexes[0] == indexes[1]) {
            final List<T> data = new ArrayList<>();
            data.add(getData().get(indexes[0]));
            return new ListType<>(data, null);
        }

        final List<T> data = getData().subList(
                indexes[0] >= 0 ? indexes[0] : getData().size() + indexes[0],
                indexes[1] >= 0 ? indexes[1] : getData().size() + indexes[1]);

        if (indexes[2] != 0) {
            final List<T> newData = new ArrayList<>();

            if (indexes[2] > 0) {
                newData.add(data.get(0));
                for (int i = indexes[2]; i < getData().size(); i += indexes[2])
                    newData.add(data.get(i));
            } else {
                newData.add(data.get(data.size() - 1));
                for (int i = data.size() - indexes[2]; i > 0; i -= indexes[2])
                    newData.add(data.get(i));
            }

            return new ListType<>(newData, null);
        }

        return new ListType<>(data, null);
    }

    public Type<?> property(String property) {
        switch (property) {
            case "length":
            case "len":
                return new NumberType(getData().size(), null);
            case "present":
            case "presentation":
               return presentation == null ? getDefaultPresentation() : presentation;
            default:
                return null;
        }
    }

    private TextType getDefaultPresentation() {
        final StringBuilder b = new StringBuilder("[");
        List<?> data = getData();
        for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
            final Type<?> v = (Type<?>) data.get(i);

            b.append(v.getData().toString());

            if (i != data.size() - 1)
                b.append(", ");
        }
        b.append("]");

        return new TextType(b.toString(), null);
    }

    public Types getDataType() {

        if (getData().size() == 0)
            return null;

        final Types t = getData().get(0).getType();

        for (T data : getData())
            if (data.getType() != t)
                return null;

        return t;
    }
}
