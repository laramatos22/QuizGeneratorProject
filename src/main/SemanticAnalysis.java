package main;

import context.ContextQueue;
import question.Question;
import question.QuestionTypes;
import types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Preforms semantic analysis.
 * <p>
 * Error codes:
 * -1 - unexpected error
 * 1 - variable not initialized
 * 2 - incompatible types
 * 3 - nonexistent type
 * 4 - impossible to cast
 * 5 - variable already initialized
 *
 * @author João Felisberto
 */
public class SemanticAnalysis extends QuizBaseVisitor<Object> {

    private final ContextQueue variables = new ContextQueue();

    public SemanticAnalysis(List<Question> qs) {
        qs.forEach(q-> variables.registerVar(new QuestionType(q, null)));
    }

    @Override
    public Object visitMain(QuizParser.MainContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitStatQuestionStat(QuizParser.StatQuestionStatContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitStatVariableDecl(QuizParser.StatVariableDeclContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitStatMcall(QuizParser.StatMcallContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitStatPropertyStat(QuizParser.StatPropertyStatContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitStatFluxControl(QuizParser.StatFluxControlContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitStatExp(QuizParser.StatExpContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public Object visitQdb(QuizParser.QdbContext ctx) {
        // TODO: 6/1/21 Complete when aux lang is done
        final ListType<?> l = new ListType<>(new ArrayList<>(), ctx.ID().getText());

        variables.registerVar(l);

        return l;
    }

    @Override
    public Object visitQuestion(QuizParser.QuestionContext ctx) {

        final Object qCorrect = ctx.corr == null ? null : ((Object[]) visit(ctx.qcorrect()))[1];

        final QuestionTypes type;
        switch (ctx.type.getText()) {
            case "multiple":
                type = QuestionTypes.MULTIPLE_ANS;
                break;
            case "match":
                type = QuestionTypes.MATCHING;
                break;
            case "short":
                type = QuestionTypes.SHORT;
                break;
            case "numeric":
                type = QuestionTypes.NUMERIC;
                break;
            case "long":
                type = QuestionTypes.LONG;
                break;
            default:
                return null;
        }

        return new QuestionType(new Question(
                null,
                ctx.q == null ? null : ctx.q.getText(),
                type,
                ctx.dep == null ? null : (List<String>) ((ListType<?>) visit(ctx.dep)).getData(),
                ctx.gp == null ? null : (List<String>) ((ListType<?>) visit(ctx.gp)).getData(),
                ctx.ans == null ? null : (List<String>) ((ListType<?>) visit(ctx.ans)).getData(),
                qCorrect == null || qCorrect instanceof String ? null : (List<String>) qCorrect,
                qCorrect == null || qCorrect instanceof List ? null : (String) qCorrect,
                null, null),
                null);
    }

    @Override
    public Object visitVarDecl(QuizParser.VarDeclContext ctx) {

        // reassignment
        if (ctx.type == null) {

            final Type var = (Type<?>) visit(ctx.propertyAcess());

            // if it was already declared
            if (var == null) {
                return null;
            }

            // wrong type
            try {
                final Type newVar = Cast.cast((Type<?>) visit(ctx.exp()), var.getType());

                if (newVar == null) {
                    System.err.println("Wrong type: cannot cast '" + ctx.exp().getText() + "' into '" + var.getType().name() + "'");
                    return null;
                }

                var.setData(newVar.getData());
                return newVar;
            } catch (NumberFormatException e) {
                System.err.println("Wrong type: cannot cast '" + ctx.exp().getText() + "' into '" + var.getType().name() + "'");
                return null;
            }
        }

        // initialization

        // check if type is valid
        switch (ctx.type.getText()) {
            case "num":
            case "text":
            case "list":
            case "question":
            case "time":
                break;
            default:
                return null;
        }

        final Type<?> var = (Type<?>) visit(ctx.propertyAcess());

        // it was already declared
        if (var != null) {
            return null;
        }

        // if exp type is not the variable type
        try {
            /*
            final Type<?> newVari = ctx.question() == null ?
                    Cast.cast((Type<?>) visit(ctx.exp()), Types.get(ctx.type.getText())) :
                    ctx.time() == null ? (QuestionType) visit(ctx.question()) :
                            (TimeType) visit(ctx.time());
*/
            final Type<?> newVar;
            try {
                newVar = ctx.question() == null ? (
                        ctx.time() == null ?
                                Cast.cast((Type<?>) visit(ctx.exp()), Types.get(ctx.type.getText())) :
                                (TimeType) visit(ctx.time())
                ) :
                        (QuestionType) visit(ctx.question());
            } catch (IndexOutOfBoundsException e) {
                System.err.println("ID not found");
                return null;
            }

            if (newVar == null) {
                System.err.println("Wrong type: cannot cast '" + ctx.exp().getText() + "' into '" + ctx.type.getText() + "'");
                return null;
            }

            newVar.setName(ctx.name.getText());
            variables.registerVar(newVar);
            return newVar;
        } catch (NumberFormatException e) {
            System.err.println("Wrong type: cannot cast '" + ctx.exp().getText() + "' into '" + ctx.type.getText() + "'");
            return null;
        }
    }

    @Override
    public Object visitMcall(QuizParser.McallContext ctx) {
        final String foo = ctx.ID().getText();

        final Object visit = ctx.exp() == null ? null : visit(ctx.exp());
        final Type<?> param = (Type<?>) visit;

        Type<?> res = Methods.call(foo, param);

        return res;
    }

    @Override
    public Object visitQans(QuizParser.QansContext ctx) {
        final ListType<?> ans = (ListType<?>) visit(ctx.list());

//        for (Type<?> elem : ans.getData())
//            if (elem.getType() != Types.TEXT && elem.getType() != Types.NUMBER && elem.getType() != Types.LIST)
//                return null;

        return ans;
    }

    @Override
    public Object visitCorNotLong(QuizParser.CorNotLongContext ctx) {
        if (ctx.list() == null)
            return null;

        final ListType<?> list = (ListType<?>) visit(ctx.list());
        final List<?> correct = list.getData() == null ? new ArrayList<>() : list.getData();

        if (correct.size() == 0)
            return new Object[]{null, correct}; // <--

        for (Object o : correct) {
            if (o == null)
                continue;
            final Type<?> t = (Type<?>) o;

            if (t.getType() != Types.NUMBER &&
                    !(t.getType() == Types.LIST && t.getType() == Types.NUMBER))
                break;

            return new Object[]{QuestionTypes.NUMERIC, correct};
        }

        // Multiple choice or Match of only 1 element
        if (correct.size() == 1) {
            if (correct.get(0) == null)
                return new Object[]{null, new ArrayList<>()};

            if (((Type<?>) correct.get(0)).getType() == Types.LIST) { //Match
                final ListType<?> e = (ListType<?>) correct.get(0);
                for (Type<?> pair : e.getData()) {
                    if (pair.getType() != Types.LIST) {
                        return new Object[]{null, new ArrayList<>()};
                    }

                    final ListType<?> lst = (ListType<?>) pair;
                    if (lst.getData().size() != 2) {
                        return new Object[]{null, new ArrayList<>()};
                    }

                    if (lst.getData().get(0).getType() != Types.TEXT || lst.getData().get(1).getType() != Types.TEXT) {
                        return new Object[]{null, new ArrayList<>()};
                    }

                    return new Object[]{QuestionTypes.MATCHING, correct};
                }
            } else if (correct.get(0) instanceof TextType)
                return new Object[]{QuestionTypes.MATCHING, correct};
            else {
                return new Object[]{null, new ArrayList<>()};
            }
        }

        // Short
        if ((correct.get(0) instanceof NumberType && correct.get(1) instanceof TextType) ||
                (correct.get(0) instanceof TextType && correct.get(1) instanceof NumberType))
            return new Object[]{QuestionTypes.SHORT, correct};

        final Types listType = ((Type<?>) correct.get(0)).getType();
        if (listType != Types.TEXT && listType != Types.LIST)
            return new Object[]{null, new ArrayList<>()};

        for (Object o : correct) {
            final Type<?> var = (Type<?>) o;

            if (var.getType() != listType)
                return new Object[]{null, new ArrayList<>()};
        }

        if (listType == Types.LIST)
            for (Object o : correct) {
                final ListType<?> var = (ListType<?>) o;

                if (var.getData().size() != 2 &&
                        var.getData().get(0).getType() != Types.TEXT &&
                        var.getData().get(1).getType() != Types.TEXT)

                    return new Object[]{null, new ArrayList<>()};
            }

        return new Object[]{listType, correct};
    }

    @Override
    public Object visitCorLong(QuizParser.CorLongContext ctx) {
        try {
            final TextType validator = ctx.ID() == null ?
                    (TextType) variables.getVariable(ctx.String().getText()) :
                    (TextType) variables.getVariable(ctx.ID().getText());

            return new Object[]{Types.TEXT, validator};
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public Object visitPropertyAcess(QuizParser.PropertyAcessContext ctx) {

        final String[] properties = ctx.getText().split("[.]");

        Type<?> obj = variables.getVariable(
                properties.length == 0 ? ctx.getText() : properties[0]);
        if (properties.length == 1)
            return obj;

        for (int i = 1; i < properties.length; i++) {
            final String property = properties[i];

            if (obj == null) {
                return null;
            }

            final Type<?> accessed;

            outer:
            switch (obj.getType()) {
                case LIST:
                    switch (property) {
                        case "length":
                        case "len":
                            accessed = new NumberType(((ListType<?>) obj).getData().size(), null);
                            break outer;
                        default:
                            accessed = null;
                            break outer;
                    }
                case QUESTION:
                    switch (property) {
                        case "id":
                            accessed = new TextType(((QuestionType) obj).getData().getId(), null);
                            break outer;
                        case "question":
                            accessed = new TextType(((QuestionType) obj).getData().getQuestion(), null);
                            break outer;
                        case "type":
                            accessed = new TextType(((QuestionType) obj).getData().getType().name(), null);
                            break outer;
                        case "dependencies":
                            accessed = new ListType<>(
                                    ((QuestionType) obj).getData().getDependencies().stream()
                                            .map(q -> new TextType(q, null))
                                            .collect(Collectors.toList()),
                                    null);
                            break outer;
                        case "groups":
                            accessed = new ListType<>(
                                    ((QuestionType) obj).getData().getGroups().stream()
                                            .map(s -> new TextType(s, null))
                                            .collect(Collectors.toList()),
                                    null);
                            break outer;
                        case "possibileAns":
                            accessed = new ListType<>(
                                    ((QuestionType) obj).getData().getQans().stream()
                                            .map(s -> new TextType(s, null))
                                            .collect(Collectors.toList()),
                                    null);
                            break outer;
                        case "correct":
                            final Question q = ((QuestionType) obj).getData();
                            if (q.getCorrectList() != null)
                                accessed = new ListType<>(
                                        q.getCorrectList().stream()
                                                .map(s -> new TextType((String) s, null))
                                                .collect(Collectors.toList()),
                                        null);
                            else
                                accessed = new TextType(q.getCorrectString(), null);

                            break outer;
                        case "ans":
                            accessed = ((QuestionType) obj).getData().getUserAnswer();
                            break outer;
                        default:
                            accessed = null;
                            break outer;
                    }
                default:
                    accessed = null;
                    break;
            }

            obj = accessed;
        }

        return obj;
    }

    @Override
    public Object visitFluxCtrl(QuizParser.FluxCtrlContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitIf_(QuizParser.If_Context ctx) {
        if (!(visit(ctx.exp()) instanceof NumberType)) {
            return null;
        }

        variables.pushContext();

        // variables declared on the if statement will be accessible on every else if and else statement
        visit(ctx.loopStat());

        if (ctx.elsif() != null)
            ctx.elsif().forEach(this::visit);

        if (ctx.else_() != null)
            visit(ctx.else_());

        // remove the variables declared on the if statement
        variables.popContext();

        return null;
    }

    @Override
    public Object visitElsif(QuizParser.ElsifContext ctx) {
        if (!(visit(ctx.exp()) instanceof NumberType)) {
            return null;
        }

        variables.pushContext();

        // variables declared on the if statement will be accessible on every else if and else statement
        visit(ctx.loopStat());

        // remove the variables declared on the if statement
        variables.popContext();

        return null;
    }

    @Override
    public Object visitElse_(QuizParser.Else_Context ctx) {
        variables.pushContext();

        // variables declared on the if statement will be accessible on every else if and else statement
        visit(ctx.loopStat());

        // remove the variables declared on the if statement
        variables.popContext();

        return null;
    }

    @Override
    public Object visitWhile_(QuizParser.While_Context ctx) {

        if (!(visit(ctx.exp()) instanceof NumberType)) {
            return null;
        }

        variables.pushContext();

        // variables declared on the if statement will be accessible on every else if and else statement
        visit(ctx.loopStat());

        // remove the variables declared on the if statement
        variables.popContext();

        return null;
    }

    @Override
    public Object visitDowhile(QuizParser.DowhileContext ctx) {
        variables.pushContext();

        // variables declared on the if statement will be accessible on every else if and else statement
        visit(ctx.loopStat());

        if (!(visit(ctx.exp()) instanceof NumberType)) {
            return null;
        }

        // remove the variables declared on the if statement
        variables.popContext();

        return null;
    }

    @Override
    public Object visitFor_(QuizParser.For_Context ctx) {
        variables.pushContext();

        visit((ctx.exp(0)));

        visit(ctx.exp(1));

        visit(ctx.loopStat());

        visit(ctx.exp(2));

        variables.popContext();
        return null;
    }

    @Override
    public Object visitForeach(QuizParser.ForeachContext ctx) {

        // iterator cannot exist in context
        if (variables.getVariable(ctx.iterator.getText()) != null) {
            return null;
        }

//        variables.pushContext();

        /*
		list
		| tuple
		| propertyAcess
		| String
		| Num
		| ID
         */

        final List contents;
        if (ctx.list() != null)
            contents = ((ListType<?>) visit(ctx.list())).getData();
        else if (ctx.tuple() != null)
            contents = ((ListType<?>) visit(ctx.tuple())).getData();
        else if (ctx.String() != null) {
            contents = new ArrayList<>();
            final char[] chars = ctx.String().getText().toCharArray();
            for (char c : chars)
                contents.add(new TextType(String.valueOf(c), null));
        } else if (ctx.Num() != null) {
            contents = new ArrayList<>();
            final int limit = Integer.parseInt(ctx.Num().getText());
            for (int i = (limit < 0 ? limit : 0); i < (limit < 0 ? 0 : limit); i++)
                contents.add(new NumberType(i, null));
        } else { // ID
            final Type<?> var = variables.getVariable(ctx.ID(1).getText());
            if (var == null) {
                return null;
            }

            switch (var.getType()) {
                case QUESTION:
                    System.err.println("Cannot iterate over question");
                    return null;
                case NUMBER:
                    contents = new ArrayList<>();
                    final int limit = (int) Math.abs(((NumberType) var).getData());
                    for (int i = (limit < 0 ? limit : 0); i < (limit < 0 ? 0 : limit); i++)
                        contents.add(new NumberType(i, null));
                    break;
                case TEXT:
                    contents = new ArrayList<>();
                    final char[] chars = ((TextType) var).getData().toCharArray();
                    for (char c : chars)
                        contents.add(new TextType(String.valueOf(c), null));
                    break;
                case LIST:
                    contents = ((ListType<?>) var).getData();
                    break;
                default:
                    return null;
            }
        }

        for (Object content : contents) {
            if (!(content instanceof Type<?>))
                return null;

            variables.pushContext();

            final String name = ctx.iterator.getText();
            final Type<?> i;
            switch (((Type<?>) content).getType()) {
                case TEXT:
                    i = new TextType(((TextType) content).getData(), name);
                    break;
                case LIST:
                    i = new ListType<>(((ListType<?>) content).getData(), name);
                    break;
                case NUMBER:
                    i = new NumberType(((NumberType) content).getData(), name);
                    break;
                case QUESTION:
                    i = new QuestionType(((QuestionType) content).getData(), name);
                    break;
                default:
                    return null;
            }

            variables.registerVar(i);

            visit(ctx.loopStat());

            variables.popContext();
        }

        variables.popContext();
        return null;
    }

    @Override
    public Object visitMulti(QuizParser.MultiContext ctx) {
        variables.pushContext();

        ctx.stat().forEach(this::visit);

        variables.popContext();

        return null;
    }

    @Override
    public Object visitSingle(QuizParser.SingleContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitExpVarAccess(QuizParser.ExpVarAccessContext ctx) {
        final Type<?> res = variables.getVariable(ctx.ID().getText());

        return res;
    }

    @Override
    public Object visitExpNum(QuizParser.ExpNumContext ctx) {
        return new NumberType(ctx.Num().getText(), null);
    }

    @Override
    public Object visitExpLogicAndOr(QuizParser.ExpLogicAndOrContext ctx) {
        final Type<?> a = (Type<?>) visit(ctx.exp(0));
        final Type<?> b = (Type<?>) visit(ctx.exp(1));


        if (a == null || b == null) {
// ->            System.err.println("At least one variable was not correctly initialized");
            return null;
        }

        switch (ctx.op.getText()) {
            case "&&":
                return Calculator.and(a, b);
            case "||":
                return Calculator.or(a, b);
            default:
                return null;
        }
    }

    @Override
    public Object visitExpIndex(QuizParser.ExpIndexContext ctx) {
        final Type<?> a = (Type<?>) visit(ctx.exp());
        final int[] indexes = (int[]) visit(ctx.listIndex());

        if (a.getType() != Types.TEXT && a.getType() != Types.LIST) {
            System.err.println("Incompatible types: cannot index unless it is 'text' or 'list'");
        }

        switch (a.getType()) {
            case TEXT:
                return ((TextType) a).subString(indexes);
            case LIST:
                return ((ListType<?>) a).subList(indexes);
            default:
                return null;
        }
    }

    @Override
    public Object visitExpMcall(QuizParser.ExpMcallContext ctx) {
        return visit(ctx.mcall());
    }

    @Override
    public Object visitExpPreInc(QuizParser.ExpPreIncContext ctx) {
        final Type<?> a = (Type<?>) visit(ctx.exp());

        if (a.getType() != Types.NUMBER) {
            System.err.println("Incompatible types: cannot increment or decrement '" + a.getName() + "', is not 'num'.");
        }

        switch (ctx.op.getText()) {
            case "++":
                ((NumberType) a).add(new NumberType(1, null));
            case "--":
                ((NumberType) a).sub(new NumberType(1, null));
        }

        return a;
    }

    @Override
    public Object visitExpMulDiv(QuizParser.ExpMulDivContext ctx) {
        switch (ctx.op.getText()) {
            case "*":
                return Calculator.mul(((Type<?>) visit(ctx.exp(0))), ((Type<?>) visit(ctx.exp(1))));
            case "/":
                return Calculator.div(((Type<?>) visit(ctx.exp(0))), ((Type<?>) visit(ctx.exp(1))));
            default:
                return null;
        }
    }

    @Override
    public Object visitExpPostInc(QuizParser.ExpPostIncContext ctx) {
        final Type<?> a = (Type<?>) visit(ctx.exp());

        if (a.getType() != Types.NUMBER) {
            System.err.println("Incompatible types: cannot increment or decrement '" + a.getName() + "', is not 'num'.");
        }

        switch (ctx.op.getText()) {
            case "++":
                ((NumberType) a).add(new NumberType(1, null));
            case "--":
                ((NumberType) a).sub(new NumberType(1, null));
        }

        // Since the operation is made with the non incremented value, we need a new variable with the right value
        switch (ctx.op.getText()) {
            case "++":
                return (new NumberType(((NumberType) a).getData(), null)).sub(new NumberType(1, null));
            case "--":
                return (new NumberType(((NumberType) a).getData(), null)).add(new NumberType(1, null));
            default:
                return null;
        }
    }

    @Override
    public Object visitExpUnary(QuizParser.ExpUnaryContext ctx) {
        final Type<?> a = (Type<?>) visit(ctx.exp());

        if (ctx.op.getText().equals("+"))
            return a;

        return Calculator.invert(a);
    }

    @Override
    public Object visitExpAddSub(QuizParser.ExpAddSubContext ctx) {
        final Type<?> a = (Type<?>) visit(ctx.exp(0));
        final Type<?> b = (Type<?>) visit(ctx.exp(1));

        try {
            switch (ctx.op.getText()) {
                case "+":
                    return Calculator.add(a, b);
                case "-":
                    return Calculator.sub(a, b);
                default:
                    throw new NullPointerException();
            }
        } catch (NumberFormatException e1) {
            System.err.println("Impossible cast: cannot cast '" +
                    (a.getType() == Types.TEXT ? a.getName() : b.getName()) +
                    "' to 'num'");
        } catch (NullPointerException e2) {
            System.err.println("Incompatible types: cannot add or subtract '" + a.getName() + "' and '" + b.getName() + "'.");
        }

        return null;
    }

    @Override
    public Object visitExpComp(QuizParser.ExpCompContext ctx) {
        final Type<?> a = (Type<?>) visit(ctx.exp(0));
        final Type<?> b = (Type<?>) visit(ctx.exp(1));

        try {
            switch (ctx.op.getText()) {
                case "==":
                    return Calculator.eq(a, b);
                case "<":
                    return Calculator.lt(a, b);
                case ">":
                    return Calculator.gt(a, b);
                case "<=":
                    return Calculator.le(a, b);
                case ">=":
                    return Calculator.ge(a, b);
                default:
                    throw new NullPointerException();
            }
        } catch (NumberFormatException e1) {
            System.err.println("Impossible cast: cannot cast '" +
                    (a.getType() == Types.TEXT ? a.getName() : b.getName()) +
                    "' to 'num'");
        } catch (NullPointerException e2) {
            if (a == null | b == null)
                System.err.println("Incompatible types: cannot compare");
            else
                System.err.println("Incompatible types: cannot compare '" + a.getName() + "' and '" + b.getName() + "'.");
        }

        return null;
    }

    @Override
    public Object visitExpPAccess(QuizParser.ExpPAccessContext ctx) {
        return visit(ctx.propertyAcess());
    }

    @Override
    public Object visitExpListTuple(QuizParser.ExpListTupleContext ctx) {
        return ctx.list() == null ? visit(ctx.tuple()) : visit(ctx.list());
    }

    @Override
    public Object visitExpParenthesis(QuizParser.ExpParenthesisContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public Object visitExpDecl(QuizParser.ExpDeclContext ctx) {
        return visit(ctx.varDecl());
    }

    @Override
    public Object visitExpString(QuizParser.ExpStringContext ctx) {
        return new TextType(ctx.String().getText().substring(1, ctx.String().getText().length() - 1), null);
    }

    @Override
    public Object visitList(QuizParser.ListContext ctx) {
/*
        if (ctx.tuple() != null)
            return visit(ctx.tuple());
*/
        return new ListType<>(ctx.lstElem().stream()
                .map(lstElemContext -> ((Type<?>) visit(lstElemContext)))
                .collect(Collectors.toList()),
                null);

    }

    @Override
    public Object visitTuple(QuizParser.TupleContext ctx) {
        return new ListType<Type<?>>(
                ctx.exp().stream()
                        .map(this::visit)
                        .map(o -> (Type<?>) o)
                        .collect(Collectors.toList()),
                null);
    }

    @Override
    public Object visitLstTuple(QuizParser.LstTupleContext ctx) {
        return visit(ctx.tuple());
    }

    @Override
    public Object visitLstNum(QuizParser.LstNumContext ctx) {
        try {
            return new NumberType(Double.parseDouble(ctx.getText()), null);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number: " + ctx.getText());
            return null;
        }
    }

    @Override
    public Object visitLstVar(QuizParser.LstVarContext ctx) {
        return variables.getVariable(ctx.getText());
    }

    @Override
    public Object visitLstStr(QuizParser.LstStrContext ctx) {
        return new TextType(ctx.String().getText().substring(1, ctx.String().getText().length() - 1), null);
    }

    @Override
    public Object visitSingleIndex(QuizParser.SingleIndexContext ctx) {
        try {
            return new int[]{
                    ctx.exp() == null ? 0 : ((((NumberType) Cast.cast((Type<?>) visit(ctx.exp()), Types.NUMBER)).getData().intValue())),
                    ctx.exp() == null ? 0 : (((NumberType) Cast.cast((Type<?>) visit(ctx.exp()), Types.NUMBER)).getData().intValue()),
                    0};
        } catch (NumberFormatException e1) {
            System.err.println("Incompatible types: cannot use floating point numbers for indexing");
        } catch (NullPointerException | ClassCastException e2) {
            System.err.println("Incompatible types: indexing variable type must be 'num'");
        }

        return null;
    }

    @Override
    public Object visitRangeIndex(QuizParser.RangeIndexContext ctx) {
//        return visitChildren(ctx);

        try {
            return new int[]{
                    ctx.low == null ? 0 : (((NumberType) Cast.cast((Type<?>) visit(ctx.low), Types.NUMBER)).getData().intValue()),
                    ctx.high == null ? -1 : (((NumberType) Cast.cast((Type<?>) visit(ctx.high), Types.NUMBER)).getData().intValue()),
                    0};
        } catch (NumberFormatException e) {
            System.err.println("Incompatible types: cannot use floating point numbers for indexing");
        } catch (NullPointerException e) {
            System.err.println("Incompatible types: indexing variable type must be 'num'");
        }

        return null;
    }

    @Override
    public Object visitStepIndex(QuizParser.StepIndexContext ctx) {
        try {
            return new int[]{
                    ctx.low == null ? 0 : (((NumberType) Cast.cast((Type<?>) visit(ctx.low), Types.NUMBER)).getData().intValue()),
                    ctx.high == null ? -1 : (((NumberType) Cast.cast((Type<?>) visit(ctx.high), Types.NUMBER)).getData().intValue()),
                    ctx.step == null ? 0 : (((NumberType) Cast.cast((Type<?>) visit(ctx.step), Types.NUMBER)).getData().intValue())
            };
        } catch (NumberFormatException e) {
            System.err.println("Incompatible types: cannot use floating point numbers for indexing");
        } catch (NullPointerException e) {
            System.err.println("Incompatible types: indexing variable type must be 'num'");
        }

        return null;
    }

    @Override
    public Object visitTime(QuizParser.TimeContext ctx) {
        final Type<?> time = (Type<?>) visit(ctx.exp());

        if (time.getType() != Types.NUMBER) {
            System.err.println("Incompatible types: expression for time must be a number");
            return null;
        }

        switch (ctx.mod.getText()) {
            case "s":
                return new TimeType((int) ((double) time.getData()), TimeModifiers.SECONDS, null);
            case "m":
                return new TimeType((int) ((double) time.getData()), TimeModifiers.MINUTES, null);
            case "h":
                return new TimeType((int) ((double) time.getData()), TimeModifiers.HOURS, null);
            default:
                System.err.println("Unknown time constant '" + ctx.mod.getText() + "', possible values are 's', 'm' and 'h'");
                return null;
        }
    }
}
