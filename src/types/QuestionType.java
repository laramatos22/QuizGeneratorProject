package types;

import question.Question;

import java.util.stream.Collectors;

/**
 * @author João Felisberto
 */
public class QuestionType extends Type<Question> {

    private TextType lstPresent = new TextType("", null);
    private TextType lstReport = new TextType("", null);

    public QuestionType(Question data, String name) {
        super(Types.QUESTION, data, name);
    }

    public Type<?> property(String property) {
        switch (property) {
            case "id":
                return new TextType(getData().getId(), null);
            case "question":
                return new TextType(getData().getQuestion(), null);
            case "type":
                return new TextType(getData().getType().name(), null);
            case "dependencies":
                return new ListType<>(
                        getData().getDependencies().stream()
                                .map(q -> new TextType(/*q.getId()*/q, null))
                                .collect(Collectors.toList()),
                        null);
            case "groups":
                return new ListType<>(
                        getData().getGroups().stream()
                                .map(s -> new TextType(s, null))
                                .collect(Collectors.toList()),
                        null);
            case "possibleAns":
                return new ListType<>(
                        getData().getQans().stream()
                                .map(s -> new TextType(s, null))
                                .collect(Collectors.toList()),
                        null);
            case "correct":
                final Question q = getData();
                if (q.getCorrectList() != null)
                    return new ListType<>(
                            q.getCorrectList().stream()
                                    .map(s -> new TextType((String) s, null))
                                    .collect(Collectors.toList()),
                            null);
                else
                    return new TextType(q.getCorrectString(), null);
            case "ans":
                return getData().getUserAnswer();
                default:
                return null;
            case "present":
            case "presentation":
                return new TextType(getData().getPresentation(), null);
            case "report":
                return new TextType(getData().getReport(), null);
            case "lstPresent":
            case "lstPresentation":
                return lstPresent;
            case "lstReport":
                return lstReport;
        }
    }
}
