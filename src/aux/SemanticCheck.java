package aux;

import question.Question;
import question.QuestionTypes;
import types.ListType;
import types.NumberType;
import types.TextType;
import types.Type;

import java.util.ArrayList;
import java.util.List;

public class SemanticCheck extends CQuizBaseVisitor<Object> {

    private final List<Question> questions = new ArrayList<>();
    private final List<String> questionIds;

    public List<Question> getQuestions() {
        return questions;
    }

    public SemanticCheck(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    @Override
    public Object visitMultMatch(CQuizParser.MultMatchContext ctx) {
        
        /* 	##### Comments to ignore errors #####
        ctx.possible == null ? null : (List<String>) ((ListType<?>) visit(ctx.possible)).getData();
        if (ctx.possible.size() != 4 ){
        	final QuestionTypes type = QuestionTypes.MATCHING;
        }else{
        	final QuestionTypes type = QuestionTypes.MULTIPLE_ANS;
        }
       	*/
       	final QuestionTypes type = QuestionTypes.MULTIPLE_ANS;
        

        return new Question(
                null,
                ctx.question == null ? null : ctx.question.getText(),
                type,
                ctx.l2 == null ? null : (List<String>) ((ListType<?>) visit(ctx.l2)).getData(),
                ctx.l1 == null ? null : (List<String>) ((ListType<?>) visit(ctx.l1)).getData(),
                ctx.possible == null ? null : (List<String>) ((ListType<?>) visit(ctx.possible)).getData(),
                //ctx.correct == null || ctx.correct instanceof String ? null : (List<String>) ctx.correct,
                //ctx.correct == null || ctx.correct instanceof List ? null : (String) ctx.correct,
                null,
                null,
                null, null);
    }

    @Override
    public Object visitShort(CQuizParser.ShortContext ctx) {
        
        final QuestionTypes type = QuestionTypes.SHORT;

        return new Question(
                null,
                ctx.question == null ? null : ctx.question.getText(),
                type,
                ctx.l2 == null ? null : (List<String>) ((ListType<?>) visit(ctx.l2)).getData(),
                ctx.l1 == null ? null : (List<String>) ((ListType<?>) visit(ctx.l1)).getData(),
                null,
                //ctx.validator == null || ctx.validator instanceof String ? null : (List<String>) ctx.validator,
                //ctx.validator == null || ctx.validator instanceof List ? null : (String) ctx.validator,
                null,
                null,
                null, null);
    }

    @Override
    public Object visitNumeric(CQuizParser.NumericContext ctx) {
        
        final QuestionTypes type = QuestionTypes.NUMERIC;

        return new Question(
                null,
                ctx.question == null ? null : ctx.question.getText(),
                type,
                ctx.l2 == null ? null : (List<String>) ((ListType<?>) visit(ctx.l2)).getData(),
                ctx.l1 == null ? null : (List<String>) ((ListType<?>) visit(ctx.l1)).getData(),
                null,
                //ctx.validator == null || ctx.validator instanceof String ? null : (List<String>) ctx.validator,
                //ctx.validator == null || ctx.validator instanceof List ? null : (String) ctx.validator,
                null,
                null,
                null, null);
    }

    @Override
    public Object visitLong(CQuizParser.LongContext ctx) {
        
        final QuestionTypes type = QuestionTypes.LONG;

        return new Question(
                null,
                ctx.question == null ? null : ctx.question.getText(),
                type,
                ctx.l2 == null ? null : (List<String>) ((ListType<?>) visit(ctx.l2)).getData(),
                ctx.l1 == null ? null : (List<String>) ((ListType<?>) visit(ctx.l1)).getData(),
                null,
                //ctx.validator == null || ctx.validator instanceof String ? null : (List<String>) ctx.validator,
                //ctx.validator == null || ctx.validator instanceof List ? null : (String) ctx.validator,
                null,
                null,
                null, null);
    }

    @Override
    public Object visitTuple(CQuizParser.TupleContext ctx) {
//        return visitChildren(ctx);
        final List<Type<?>> data = new ArrayList<>();

        ctx.tuplelem().forEach(lstelemContext -> data.add((Type<?>) visit(lstelemContext)));

        return new ListType<>(data, null);
    }

    @Override
    public Object visitList(CQuizParser.ListContext ctx) {
//        return visitChildren(ctx);
        final List<Type<?>> data = new ArrayList<>();

        ctx.lstelem().forEach(lstelemContext -> data.add((Type<?>) visit(lstelemContext)));

        return new ListType<>(data, null);
    }

    @Override
    public Object visitTupElem(CQuizParser.TupElemContext ctx) {
//        return visitChildren(ctx);
        return visit(ctx.lstelem());
    }

    @Override
    public Object visitTupLst(CQuizParser.TupLstContext ctx) {
//        return visitChildren(ctx);
        return visit(ctx.list());
    }

    @Override
    public Object visitLstWrd(CQuizParser.LstWrdContext ctx) {
//        return visitChildren(ctx);
        return new TextType(
                ctx.getText().substring(1, ctx.getText().length() - 1),
                null
        );
    }

    @Override
    public Object visitLstNum(CQuizParser.LstNumContext ctx) {
//        return visitChildren(ctx);
        try {
            return new NumberType(
                    Double.parseDouble(ctx.getText()),
                    null
            );
        } catch (NumberFormatException e) {
            System.err.println("Malformed number for '" + ctx.getText() + "'");
            return new NumberType(-1, null);
        }
    }

    @Override
    public Object visitLstTup(CQuizParser.LstTupContext ctx) {
//        return visitChildren(ctx);
        return visit(ctx.tuple());
    }

    /**
     * Determines whether each list is a dependency list or groups list.
     * In case one of them is null, in it's place will be returned an empty list.
     * @param l1 a list
     * @param l2 another list
     * @return an array of type [dependencies, groups]
     */
    private List<String>[] resolveDependenciesAndGroups(List<String> l1, List<String> l2) {

        if (l1 == null && l2 == null)
            return new List[] {new ArrayList<String>(), new ArrayList<String>()};

        if (l1 == null)
            return questionIds.containsAll(l2) ?
                    new List[] {l2, new ArrayList<String>()} :
                    new List[] {new ArrayList<String>(), l2};

        if (l2 == null)
            return questionIds.containsAll(l2) ?
                    new List[] {l2, new ArrayList<String>()} :
                    new List[] {new ArrayList<String>(), l2};

        final List<String> dependencies = questionIds.containsAll(l1) ?
                l1 :
                questionIds.containsAll(l2) ?
                        l2 :
                        null;

        if (dependencies == null) {
            System.err.println();
        }

        return null;
    }
}
