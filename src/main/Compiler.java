package main;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import question.Question;
import question.QuestionTypes;

import java.util.List;

public class Compiler extends QuizBaseVisitor<ST> {
    private final STGroup templates = new STGroupFile("src/main/Compiler/ST.stg");
    private final List<Question> qs;

    private int qCnt = 0;

    public Compiler(List<Question> qs) {
        this.qs = qs;
    }

    @Override
    public ST visitMain(QuizParser.MainContext ctx) {
        final ST main = templates.getInstanceOf("main");

        qs.forEach(q ->
                main.add("qdb", buildQuestionTemplate(q).render())
        );

        ctx.stat().forEach(s ->
                main.add("stat", visit(s).render())
        );

        return main;
    }

    private ST buildQuestionTemplate(Question q) {
        final ST template = new ST("registerQ(new Question(<id>, <question>, <type>, <dependencies>, <groups>, <qans>, <correctList>, <correctString>, <presentation>, <report>), null)");
        template.add("id", q.getId());
        template.add("question", q.getQuestion());
        template.add("type", q.getType());
        template.add("dependencies", buildListStringTemplate(q.getDependencies()).render());
        template.add("groups", buildListStringTemplate(q.getGroups()).render());
        template.add("qans", buildListStringTemplate(q.getQans()).render());
        template.add("correctList", buildListStringTemplate(q.getCorrectList()).render());
        template.add("correctString", q.getCorrectString());
        template.add("presentation", q.getPresentation());
        template.add("report", q.getReport());

        return template;
    }

    private ST buildListStringTemplate(List<?> l) {
        final ST template = new ST("buildStrList(<param; separator=\", \">)");

        l.forEach(elem ->
                template.add("param",
                        elem instanceof List ? buildListStringTemplate((List<Object>) elem).render() : elem));

        return template;
    }

    @Override
    public ST visitStatQuestionStat(QuizParser.StatQuestionStatContext ctx) {
        return visit(ctx.question());
    }

    @Override
    public ST visitStatVariableDecl(QuizParser.StatVariableDeclContext ctx) {
        return visit(ctx.varDecl());
    }

    @Override
    public ST visitStatMcall(QuizParser.StatMcallContext ctx) {
        return visit(ctx.mcall());
    }

    @Override
    public ST visitStatPropertyStat(QuizParser.StatPropertyStatContext ctx) {
        return visit(ctx.propertyAcess());
    }

    @Override
    public ST visitStatFluxControl(QuizParser.StatFluxControlContext ctx) {
        return visit(ctx.fluxCtrl());
    }

    @Override
    public ST visitStatExp(QuizParser.StatExpContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public ST visitQdb(QuizParser.QdbContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ST visitQuestion(QuizParser.QuestionContext ctx) {
        final ST question = templates.getInstanceOf("question");

        if (ctx.id != null)
            question.add("id", ctx.id.getText());
        else
            question.add("id", qCnt++);

        if (ctx.q != null)
            question.add("q", ctx.q.getText());
        if (ctx.type != null)
            question.add("type", QuestionTypes.valueOf(ctx.type.getText()));
        if (ctx.gp != null)
            question.add("groups", visit(ctx.gp).render());
        if (ctx.dep != null)
            question.add("depend", visit(ctx.dep).render());
        if (ctx.ans != null)
            question.add("ans", visit(ctx.ans).render());
        if (ctx.corr != null)
            question.add("correct", visit(ctx.corr).render());
        return question;
    }

    @Override
    public ST visitVarDecl(QuizParser.VarDeclContext ctx) {
        if (ctx.type == null) {
            final ST reassign = templates.getInstanceOf("declReassign");

            reassign.add("name", visit(ctx.propertyAcess()).render());
            reassign.add("val", visit(
                    ctx.question() != null ? ctx.question() :
                            ctx.exp() != null ? ctx.exp() :
                                    ctx.time()
            ).render());

            return reassign;
        }

        final ST assign;

        switch (ctx.type.getText()) {
            case "time":
                assign = templates.getInstanceOf("declTime");
                assign.add("val", visit(ctx.time()).render());
                break;
            case "num":
                assign = templates.getInstanceOf("declNum");
                assign.add("val", visit(ctx.exp()).render());
                break;
            case "text":
                assign = templates.getInstanceOf("declText");
                assign.add("val", visit(ctx.exp()).render());
                break;
            case "list":
                assign = templates.getInstanceOf("declList");
                assign.add("val", visit(ctx.exp()).render());
                break;
            case "question":
                assign = templates.getInstanceOf("declQuestion");
                assign.add("val", visit(ctx.question()).render());
                break;
            default:
                return null;
        }

        assign.add("name", visit(ctx.propertyAcess()).render());
        return assign;
    }

    @Override
    public ST visitMcall(QuizParser.McallContext ctx) {
        final ST mcall = templates.getInstanceOf("mcall");

        mcall.add("method", ctx.ID().getText());
        mcall.add("param", visit(ctx.exp()).render());

        return mcall;
    }

    @Override
    public ST visitQans(QuizParser.QansContext ctx) {
        return visit(ctx.list());
    }

    @Override
    public ST visitCorNotLong(QuizParser.CorNotLongContext ctx) {
        return visit(ctx.list());
    }

    @Override
    public ST visitCorLong(QuizParser.CorLongContext ctx) {
        final ST template;

        if (ctx.String() != null) {
            template = templates.getInstanceOf("text");
            template.add("text", ctx.String().getText());
        } else if (ctx.String() != null) {
            template = templates.getInstanceOf("id");
            template.add("varName", ctx.ID().getText());
        } else {
            return null;
        }

        return template;
    }

    @Override
    public ST visitPropertyAcess(QuizParser.PropertyAcessContext ctx) {
        final ST propertyAccess = templates.getInstanceOf("propertyAccess");

        final String[] properties = ctx.getText().split("[.]");
        propertyAccess.add("obj", properties[0]);

        for (int i = 1; i < properties.length; i++)
            propertyAccess.add("property", properties[i]);

        return propertyAccess;
    }

    @Override
    public ST visitFluxCtrl(QuizParser.FluxCtrlContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ST visitIf_(QuizParser.If_Context ctx) {
        final ST ifStat = templates.getInstanceOf("if");

        ifStat.add("exp", visit(ctx.exp()).render());
        ifStat.add("stat", visit(ctx.loopStat()).render());

        try {
            ctx.elsif().forEach(elsifContext ->
                    ifStat.add("elsif", visit(elsifContext).render()
                    ));
        } catch (IndexOutOfBoundsException | NullPointerException ignored) {
        }

        if (ctx.else_() != null)
            ifStat.add("els", visit(ctx.else_()).render());

        return ifStat;
    }

    @Override
    public ST visitElsif(QuizParser.ElsifContext ctx) {
        final ST elsif = templates.getInstanceOf("elsif");

        elsif.add("exp", visit(ctx.exp()).render());
        elsif.add("stat", visit(ctx.loopStat()).render());

        return elsif;
    }

    @Override
    public ST visitElse_(QuizParser.Else_Context ctx) {
        final ST els = templates.getInstanceOf("else");

        els.add("stat", visit(ctx.loopStat()).render());

        return els;
    }

    @Override
    public ST visitWhile_(QuizParser.While_Context ctx) {
        final ST whl = templates.getInstanceOf("while");

        whl.add("exp", visit(ctx.exp()).render());
        whl.add("stat", visit(ctx.loopStat()).render());

        return whl;
    }

    @Override
    public ST visitDowhile(QuizParser.DowhileContext ctx) {
        final ST dowhile = templates.getInstanceOf("dowhile");

        dowhile.add("exp", visit(ctx.exp()).render());
        dowhile.add("stat", visit(ctx.loopStat()).render());

        return dowhile;
    }

    @Override
    public ST visitFor_(QuizParser.For_Context ctx) {
        final ST template = templates.getInstanceOf("for");

        if (ctx.init != null)
            template.add("exp1", visit(ctx.init).render());

        if (ctx.loop != null)
            template.add("exp1", visit(ctx.loop).render());

        if (ctx.fin != null)
            template.add("exp1", visit(ctx.fin).render());

        template.add("stat", visit(ctx.loopStat()).render());

        return template;
    }

    @Override
    public ST visitForeach(QuizParser.ForeachContext ctx) {
        // TODO: 6/20/21 this
        return super.visitForeach(ctx);
    }

    @Override
    public ST visitMulti(QuizParser.MultiContext ctx) {
        final ST template = new ST("<stat; separator=\";\n\">");

        ctx.stat().forEach(statContext ->
                template.add("stat", visit(statContext).render()
                ));

        return template;
    }

    @Override
    public ST visitSingle(QuizParser.SingleContext ctx) {
        return super.visit(ctx.stat());
    }

    @Override
    public ST visitExpNum(QuizParser.ExpNumContext ctx) {
        final ST num = templates.getInstanceOf("num");
        num.add("n", Double.parseDouble(ctx.Num().getText()));

        return num;
    }

    @Override
    public ST visitExpLogicAndOr(QuizParser.ExpLogicAndOrContext ctx) {
        final ST op;

        switch (ctx.op.getText()) {
            case "&&":
                op = templates.getInstanceOf("expAnd");
                break;
            case "||":
                op = templates.getInstanceOf("expOr");
                break;
            default:
                return null;
        }

        op.add("a", visit(ctx.exp(0)).render());
        op.add("b", visit(ctx.exp(1)).render());

        return op;
    }

    @Override
    public ST visitExpIndex(QuizParser.ExpIndexContext ctx) {
        final ST template = new ST("<exp><index>");
        template.add("exp", visit(ctx.exp()).render());
        template.add("index", visit(ctx.listIndex()).render());

        return template;
    }

    @Override
    public ST visitExpMcall(QuizParser.ExpMcallContext ctx) {
        return visit(ctx.mcall());
    }

    @Override
    public ST visitExpPreInc(QuizParser.ExpPreIncContext ctx) {
        final ST op;

        switch (ctx.op.getText()) {
            case "++":
                op = templates.getInstanceOf("expPreInc");
                break;
            case "--":
                op = templates.getInstanceOf("expPreDec");
                break;
            default:
                return null;
        }

        op.add("a", visit(ctx.exp()).render());

        return op;
    }

    @Override
    public ST visitExpMulDiv(QuizParser.ExpMulDivContext ctx) {
        final ST op;

        switch (ctx.op.getText()) {
            case "*":
                op = templates.getInstanceOf("expMult");
                break;
            case "/":
                op = templates.getInstanceOf("expDiv");
                break;
            default:
                return null;
        }

        op.add("a", visit(ctx.exp(0)).render());
        op.add("b", visit(ctx.exp(1)).render());

        return op;
    }

    @Override
    public ST visitExpPostInc(QuizParser.ExpPostIncContext ctx) {
        final ST op;

        switch (ctx.op.getText()) {
            case "++":
                op = templates.getInstanceOf("expPostInc");
                break;
            case "--":
                op = templates.getInstanceOf("expPostDec");
                break;
            default:
                return null;
        }

        op.add("a", visit(ctx.exp()).render());

        return op;
    }

    @Override
    public ST visitExpUnary(QuizParser.ExpUnaryContext ctx) {
        if (ctx.op.getText().equals("+"))
            return visit(ctx.exp());

        final ST op = templates.getInstanceOf("expInvert");

        op.add("exp", visit(ctx.exp()).render());

        return op;
    }

    @Override
    public ST visitExpAddSub(QuizParser.ExpAddSubContext ctx) {
        final ST op;

        switch (ctx.op.getText()) {
            case "+":
                op = templates.getInstanceOf("expAdd");
                break;
            case "-":
                op = templates.getInstanceOf("expSub");
                break;
            default:
                return null;
        }

        op.add("a", visit(ctx.exp(0)).render());
        op.add("b", visit(ctx.exp(1)).render());

        return op;
    }

    @Override
    public ST visitExpComp(QuizParser.ExpCompContext ctx) {
        final ST cmp;
        switch (ctx.op.getText()) {
            case "==":
                cmp = templates.getInstanceOf("expEq");
                break;
            case "<":
                cmp = templates.getInstanceOf("expLt");
                break;
            case ">":
                cmp = templates.getInstanceOf("expGt");
                break;
            case "<=":
                cmp = templates.getInstanceOf("expLe");
                break;
            case ">=":
                cmp = templates.getInstanceOf("expGe");
                break;
            default:
                return null;
        }

        cmp.add("a", visit(ctx.exp(0)).render());
        cmp.add("b", visit(ctx.exp(1)).render());

        return cmp;
    }

    @Override
    public ST visitExpPAccess(QuizParser.ExpPAccessContext ctx) {
        return visit(ctx.propertyAcess());
    }

    @Override
    public ST visitExpListTuple(QuizParser.ExpListTupleContext ctx) {
        return ctx.list() == null ? visit(ctx.tuple()) : visit(ctx.list());
    }

    @Override
    public ST visitExpParenthesis(QuizParser.ExpParenthesisContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public ST visitExpVarAccess(QuizParser.ExpVarAccessContext ctx) {
        final ST id = templates.getInstanceOf("id");
        final String text = ctx.ID().getText();
        id.add("varName", text);

        return id;
    }

    @Override
    public ST visitExpDecl(QuizParser.ExpDeclContext ctx) {
        return visit(ctx.varDecl());
    }

    @Override
    public ST visitExpString(QuizParser.ExpStringContext ctx) {
        final ST text = templates.getInstanceOf("text");
        text.add("text", ctx.String().getText());

        return text;
    }

    @Override
    public ST visitList(QuizParser.ListContext ctx) {
        final ST tuple = templates.getInstanceOf("tuple");

        ctx.lstElem().forEach(expContext ->
                tuple.add("elem", visit(expContext).render()
                ));

        return tuple;
    }

    @Override
    public ST visitTuple(QuizParser.TupleContext ctx) {
        final ST tuple = templates.getInstanceOf("tuple");

        ctx.exp().forEach(expContext ->
                tuple.add("elem", visit(expContext).render()
                ));

        return tuple;
    }

    @Override
    public ST visitLstTuple(QuizParser.LstTupleContext ctx) {
        final ST id = templates.getInstanceOf("id");
        id.add("varName", visit(ctx.tuple()).render());

        return id;
    }

    @Override
    public ST visitLstNum(QuizParser.LstNumContext ctx) {
        final ST num = templates.getInstanceOf("num");
        num.add("n", Double.parseDouble(ctx.Num().getText()));

        return num;
    }

    @Override
    public ST visitLstVar(QuizParser.LstVarContext ctx) {
        final ST id = templates.getInstanceOf("id");
        final String text = ctx.ID().getText();
        id.add("varName", text);

        return id;
    }

    @Override
    public ST visitLstStr(QuizParser.LstStrContext ctx) {
        final ST text = templates.getInstanceOf("text");
        text.add("text", ctx.String().getText());

        return text;
    }

    @Override
    public ST visitSingleIndex(QuizParser.SingleIndexContext ctx) {
        final ST index = templates.getInstanceOf("index");
        final String i = visit(ctx.i).render();
        index.add("low", i);
        index.add("high", i);
        index.add("step", 0);

        return index;
    }

    @Override
    public ST visitRangeIndex(QuizParser.RangeIndexContext ctx) {
        final ST index = templates.getInstanceOf("index");
        index.add("low", visit(ctx.low).render());
        index.add("high", visit(ctx.high).render());
        index.add("step", 0);

        return index;
    }

    @Override
    public ST visitStepIndex(QuizParser.StepIndexContext ctx) {
        final ST index = templates.getInstanceOf("index");
        index.add("low", visit(ctx.low).render());
        index.add("high", visit(ctx.high).render());
        index.add("step", visit(ctx.step).render());

        return index;
    }

    @Override
    public ST visitTime(QuizParser.TimeContext ctx) {
        final ST time = templates.getInstanceOf("time");
        time.add("exp", visit(ctx.exp()).render());
        time.add("mod", ctx.mod.getText());

        return time;
    }
}
