package types;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the logic behind calling a method
 *
 * @author João Felisberto
 */
public interface Methods {
    static Type<?> call(String method, Type<?> parameter) {
        switch (method) {
            case "ask":
//                return ask((QuestionType) parameter);
                return parameter;
            case "ans":
                if (parameter == null) {
                    System.err.println("Invalid type: question does not exist");
                    return new TextType("", null);
                }

                return ((QuestionType) parameter).getData().getUserAnswer();
            case "byID":
                final ListType<?> l1 = (ListType<?>) parameter;
                return byID(l1);
            case "byTheme":
                final ListType<?> l2 = (ListType<?>) parameter;
                return byTheme(l2);
            case "shuffle":
                switch (parameter.getType()) {
                    case LIST:
                        Collections.shuffle(((ListType<?>) parameter).getData());
                        break;
                    case QUESTION:
                        Collections.shuffle(((QuestionType) parameter).getData().getQans());
                }
                return parameter;
            case "validate":
                if (parameter.getType() != Types.QUESTION)
                    return null;
                return validate((QuestionType) parameter);
            case "report":
                if (parameter == null)
                    return new TextType("", null);

                if (parameter.getType() != Types.QUESTION)
                    return new TextType("", null);

                final TextType report = new TextType(((QuestionType) parameter).getData().getReport(), null);
                print(report);
                return report;
            case "print":
//                System.out.println(((TextType) parameter).getData());
                print(parameter);
                return parameter;
            case "allowMove":
            case "denyMove":
                return parameter;
            default:
                return null;
        }
    }

    static ListType<QuestionType> byTheme(ListType<?> parameter) {
        final List<QuestionType> qs = parameter.getData().get(0) instanceof ListType ?
                (List<QuestionType>) ((ListType<?>) parameter.getData().get(0)).getData() :
                (List<QuestionType>) ((ListType<?>) parameter.getData().get(1)).getData();
        final String theme = parameter.getData().get(0) instanceof TextType ?
                ((TextType) parameter.getData().get(0)).getData() :
                ((TextType) parameter.getData().get(1)).getData();

        return new ListType<>(qs.stream().filter(questionType -> questionType.getData().getGroups().contains(theme)).collect(Collectors.toList()), null);
    }

    static QuestionType byID(ListType<?> parameter) { //[list, text]
        final List<QuestionType> qs = parameter.getData().get(0) instanceof ListType ?
                (List<QuestionType>) ((ListType<?>) parameter.getData().get(0)).getData() :
                (List<QuestionType>) ((ListType<?>) parameter.getData().get(1)).getData();
        final String id = parameter.getData().get(0) instanceof TextType ?
                ((TextType) parameter.getData().get(0)).getData() :
                ((TextType) parameter.getData().get(1)).getData();

        return qs.stream().filter(q -> q.getData().getId().equals(id)).collect(Collectors.toList()).get(0);
    }

    /* TODO: CLI should add checks for thes emethods and generate code accordingly
    static TextType ask(QuestionType question) {
        return new TextType("sample", "sample");
    }

    static TextType ans(QuestionType question) {
        return new TextType("sample", "sample");
    }
     */

    static NumberType validate(QuestionType q) {
        return q.getData().validate() ? new NumberType(1, null) : new NumberType(0, null);
    }

    static void print(Type<?> parameter) {
        switch (parameter.getType()) {
            case TEXT:
                System.out.print(((TextType) parameter).getData());
                break;
            case NUMBER:
                System.out.print(((NumberType) parameter).getData());
                break;
            case TIME:
                System.out.print(((TimeType) parameter).getData() + ((TimeType) parameter).getMod().name());
                break;
            case LIST:
                System.out.print("[");
                List<?> data = ((ListType<?>) parameter).getData();
                for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
                    final Type<?> v = (Type<?>) data.get(i);

                    print(v);

                    if (i != data.size() - 1)
                        System.out.print(",");
                }
                System.out.print("]");
                break;
            default:
                break;
        }
    }

    static TextType report(String name) {
        return new TextType("sample", name);
    }

}
