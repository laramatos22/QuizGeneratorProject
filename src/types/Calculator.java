package types;

import java.util.List;

/**
 * @author João Felisberto
 */
public interface Calculator {

    static NumberType eq(Type<?> a, Type<?> b) {
        if (a.getType() == Types.QUESTION || b.getType() == Types.QUESTION)
            return null;

        if (a.getType() == Types.NUMBER)
            switch (b.getType()) {
                case NUMBER:
                    return ((NumberType) a).eq((NumberType) b);
                case TEXT:
                    return ((NumberType) a).eq((TextType) b);
                case LIST:
                    return ((NumberType) a).eq((ListType<?>) b);
                default:
                    return null;
            }

        if (a.getType() == Types.TEXT)
            switch (b.getType()) {
                case NUMBER:
                    return ((TextType) a).eq((NumberType) b);
                case TEXT:
                    return ((TextType) a).eq((TextType) b);
                default:
                    return null;
            }

        if (a.getType() == Types.LIST && b.getType() == Types.NUMBER)
            return ((ListType<?>) a).eq((NumberType) b);

        return null;
    }

    static NumberType ne(Type<?> a, Type<?> b) {
        if (a.getType() == Types.QUESTION || b.getType() == Types.QUESTION)
            return null;

        if (a.getType() == Types.NUMBER)
            switch (b.getType()) {
                case NUMBER:
                    return ((NumberType) a).ne((NumberType) b);
                case TEXT:
                    return ((NumberType) a).ne((TextType) b);
                case LIST:
                    return ((NumberType) a).ne((ListType<?>) b);
                default:
                    return null;
            }

        if (a.getType() == Types.TEXT)
            switch (b.getType()) {
                case NUMBER:
                    return ((TextType) a).ne((NumberType) b);
                case TEXT:
                    return ((TextType) a).ne((TextType) b);
                default:
                    return null;
            }

        if (a.getType() == Types.LIST && b.getType() == Types.NUMBER)
            return ((ListType<?>) a).ne((NumberType) b);

        return null;
    }

    static NumberType gt(Type<?> a, Type<?> b) {
        if (a.getType() == Types.QUESTION || b.getType() == Types.QUESTION)
            return null;

        if (a.getType() == Types.NUMBER)
            switch (b.getType()) {
                case NUMBER:
                    return ((NumberType) a).gt((NumberType) b);
                case TEXT:
                    return ((NumberType) a).gt((TextType) b);
                case LIST:
                    return ((NumberType) a).gt((ListType<?>) b);
                default:
                    return null;
            }

        if (a.getType() == Types.TEXT)
            switch (b.getType()) {
                case NUMBER:
                    return ((TextType) a).gt((NumberType) b);
                case TEXT:
                    return ((TextType) a).gt((TextType) b);
                default:
                    return null;
            }

        if (a.getType() == Types.LIST && b.getType() == Types.NUMBER)
            return ((ListType<?>) a).gt((NumberType) b);

        return null;
    }

    static NumberType lt(Type<?> a, Type<?> b) {
        if (a.getType() == Types.QUESTION || b.getType() == Types.QUESTION)
            return null;

        if (a.getType() == Types.NUMBER)
            switch (b.getType()) {
                case NUMBER:
                    return ((NumberType) a).lt((NumberType) b);
                case TEXT:
                    return ((NumberType) a).lt((TextType) b);
                case LIST:
                    return ((NumberType) a).lt((ListType<?>) b);
                default:
                    return null;
            }

        if (a.getType() == Types.TEXT)
            switch (b.getType()) {
                case NUMBER:
                    return ((TextType) a).lt((NumberType) b);
                case TEXT:
                    return ((TextType) a).lt((TextType) b);
                default:
                    return null;
            }

        if (a.getType() == Types.LIST && b.getType() == Types.NUMBER)
            return ((ListType<?>) a).lt((NumberType) b);

        return null;
    }

    static NumberType ge(Type<?> a, Type<?> b) {
        if (a.getType() == Types.QUESTION || b.getType() == Types.QUESTION)
            return null;

        if (a.getType() == Types.NUMBER)
            switch (b.getType()) {
                case NUMBER:
                    return ((NumberType) a).ge((NumberType) b);
                case TEXT:
                    return ((NumberType) a).ge((TextType) b);
                case LIST:
                    return ((NumberType) a).ge((ListType<?>) b);
                default:
                    return null;
            }

        if (a.getType() == Types.TEXT)
            switch (b.getType()) {
                case NUMBER:
                    return ((TextType) a).ge((NumberType) b);
                case TEXT:
                    return ((TextType) a).ge((TextType) b);
                default:
                    return null;
            }

        if (a.getType() == Types.LIST && b.getType() == Types.NUMBER)
            return ((ListType<?>) a).ge((NumberType) b);

        return null;
    }

    static NumberType le(Type<?> a, Type<?> b) {
        if (a.getType() == Types.QUESTION || b.getType() == Types.QUESTION)
            return null;

        if (a.getType() == Types.NUMBER)
            switch (b.getType()) {
                case NUMBER:
                    return ((NumberType) a).le((NumberType) b);
                case TEXT:
                    return ((NumberType) a).le((TextType) b);
                case LIST:
                    return ((NumberType) a).le((ListType<?>) b);
                default:
                    return null;
            }

        if (a.getType() == Types.TEXT)
            switch (b.getType()) {
                case NUMBER:
                    return ((TextType) a).le((NumberType) b);
                case TEXT:
                    return ((TextType) a).le((TextType) b);
                default:
                    return null;
            }

        if (a.getType() == Types.LIST && b.getType() == Types.NUMBER)
            return ((ListType<?>) a).le((NumberType) b);

        return null;
    }

    static Type<?> and(Type<?> a, Type<?> b) {
        if (a.getType() == Types.NUMBER && b.getType() == Types.NUMBER)
            return ((NumberType) a).and((NumberType) b);

        return add(a, b);
    }

    static NumberType or(Type<?> a, Type<?> b) {
        if (a.getType() == Types.NUMBER && b.getType() == Types.NUMBER)
            return ((NumberType) a).or((NumberType) b);


        return null;
    }

    static Type<?> add(Type<?> a, Type<?> b) {
        if (a.getType() == Types.QUESTION || b.getType() == Types.QUESTION)
            return null;

        if (a.getType() == Types.NUMBER || b.getType() == Types.NUMBER) {
            final NumberType num = a.getType() == Types.NUMBER ? (NumberType) a : (NumberType) b;
            final Type<?> other = a.getType() == Types.NUMBER ? b : a;

            if (other.getType() == Types.LIST && ((ListType<?>) other).getDataType() != Types.NUMBER)
                return null;

            switch (other.getType()) {
                case TEXT:
                    return num.add((TextType) other);
                case NUMBER:
                    return num.add((NumberType) other);
                case LIST:
                    return ((ListType<?>) other).add(num);
                default:
                    return null;
            }
        }

        if (a.getType() == Types.TEXT || b.getType() == Types.TEXT) {
            final TextType txt = a.getType() == Types.TEXT ? (TextType) a : (TextType) b;
            final Type<?> other = a.getType() == Types.TEXT ? b : a;

            if (other.getType() == Types.LIST && ((ListType<?>) other).getDataType() != Types.TEXT)
                return null;

            switch (other.getType()) {
                case TEXT:
                    return txt.add((TextType) other);
                case LIST:
                    return ((ListType<?>) other).add(txt);
                default:
                    return null;
            }
        }

        final ListType<?> l1 = (ListType<?>) a;
        final ListType<?> l2 = (ListType<?>) b;

//        if (l1.getDataType() != l2.getDataType())
//            return null;

        final List finalData = l1.getData();
        finalData.addAll(l2.getData());

        return new ListType(finalData, null);
    }

    static Type<?> sub(Type<?> a, Type<?> b) {
        if (a.getType() == Types.QUESTION || b.getType() == Types.QUESTION)
            return null;

        if (a.getType() == Types.NUMBER && b.getType() == Types.NUMBER)
            return ((NumberType) a).sub(((NumberType) b));

        if (a.getType() == Types.LIST || b.getType() == Types.LIST) {
            final ListType<?> lst = a.getType() == Types.LIST ? (ListType<?>) a : (ListType<?>) b;
            final Type<?> other = a.getType() == Types.LIST ? (ListType<?>) b : (ListType<?>) a;


            final List data = lst.getData();
            data.remove(other);

            return new ListType<>(data, null);
        }

        return null;
    }

    static Type<?> mul(Type<?> a, Type<?> b) {
        if (a.getType() == Types.NUMBER && b.getType() == Types.NUMBER)
            return ((NumberType) a).mul(((NumberType) b));

        if (a.getType() == Types.NUMBER && b.getType() == Types.TEXT ||
                a.getType() == Types.TEXT && b.getType() == Types.NUMBER) {
            final TextType txt = a.getType() == Types.TEXT ? (TextType) a : (TextType) b;
            final NumberType num = a.getType() == Types.NUMBER ? (NumberType) a : (NumberType) b;

            return txt.mul(num);
        }

        return null;
    }

    static Type<?> div(Type<?> a, Type<?> b) {
        if (a.getType() == Types.NUMBER && b.getType() == Types.NUMBER)
            return ((NumberType) a).mul(((NumberType) b));
        
        return null;
    }

    static Type<?> invert(Type<?> a) {
        if (a.getType() == Types.QUESTION) {
            System.err.println("Incompatible types: cannot invert '" + a.getName() + "', it is 'question'.");
        }

        switch (a.getType()) {
            case NUMBER:
                return new NumberType(-((NumberType) a).getData(), null);
            case TEXT:
                return new TextType(new StringBuilder(((TextType) a).getData()).reverse().toString(), null);
            case LIST:
                return ((ListType<?>) a).getReversed();
            default:
                return null;
        }
    }
}
