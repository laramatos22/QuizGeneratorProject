package main;

import java.util.ArrayList;
import java.util.List;

public class QDBVisitor extends QuizBaseVisitor<String> {

   private final List<String> files = new ArrayList<>();

   public List<String> getFiles() {
      return files;
   }

   @Override public String visitMain(QuizParser.MainContext ctx) {
//      return visitChildren(ctx);
      ctx.qdb().forEach(this::visit);
      return null;
   }

   @Override public String visitStatQuestionStat(QuizParser.StatQuestionStatContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitStatVariableDecl(QuizParser.StatVariableDeclContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitStatMcall(QuizParser.StatMcallContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitStatPropertyStat(QuizParser.StatPropertyStatContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitStatFluxControl(QuizParser.StatFluxControlContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitStatExp(QuizParser.StatExpContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitQdb(QuizParser.QdbContext ctx) {
//      return visitChildren(ctx);
      final String fName = ctx.String().getText();
      files.add(fName.substring(1, fName.length()-1));
      return null;
   }

   @Override public String visitQuestion(QuizParser.QuestionContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitVarDecl(QuizParser.VarDeclContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitMcall(QuizParser.McallContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitQans(QuizParser.QansContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitCorNotLong(QuizParser.CorNotLongContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitCorLong(QuizParser.CorLongContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitPropertyAcess(QuizParser.PropertyAcessContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitFluxCtrl(QuizParser.FluxCtrlContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitIf_(QuizParser.If_Context ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitElsif(QuizParser.ElsifContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitElse_(QuizParser.Else_Context ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitWhile_(QuizParser.While_Context ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitDowhile(QuizParser.DowhileContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitFor_(QuizParser.For_Context ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitForeach(QuizParser.ForeachContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitMulti(QuizParser.MultiContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitSingle(QuizParser.SingleContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpNum(QuizParser.ExpNumContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpLogicAndOr(QuizParser.ExpLogicAndOrContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpIndex(QuizParser.ExpIndexContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpMcall(QuizParser.ExpMcallContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpPreInc(QuizParser.ExpPreIncContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpMulDiv(QuizParser.ExpMulDivContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpPostInc(QuizParser.ExpPostIncContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpUnary(QuizParser.ExpUnaryContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpAddSub(QuizParser.ExpAddSubContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpComp(QuizParser.ExpCompContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpPAccess(QuizParser.ExpPAccessContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpListTuple(QuizParser.ExpListTupleContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpParenthesis(QuizParser.ExpParenthesisContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpVarAccess(QuizParser.ExpVarAccessContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpDecl(QuizParser.ExpDeclContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitExpString(QuizParser.ExpStringContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitList(QuizParser.ListContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitTuple(QuizParser.TupleContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitLstTuple(QuizParser.LstTupleContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitLstNum(QuizParser.LstNumContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitLstVar(QuizParser.LstVarContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitLstStr(QuizParser.LstStrContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitSingleIndex(QuizParser.SingleIndexContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitRangeIndex(QuizParser.RangeIndexContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitStepIndex(QuizParser.StepIndexContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitTime(QuizParser.TimeContext ctx) {
      return visitChildren(ctx);
   }
}
