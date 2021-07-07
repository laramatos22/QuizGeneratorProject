package aux;

import java.util.ArrayList;
import java.util.List;

public class QuestionLister extends CQuizBaseVisitor<String> {

   final List<String> questionIds = new ArrayList<>();

   @Override public String visitMain(CQuizParser.MainContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitMultMatch(CQuizParser.MultMatchContext ctx) {
      questionIds.add(ctx.question.getText());
      return visitChildren(ctx);
   }

   @Override public String visitShort(CQuizParser.ShortContext ctx) {
      questionIds.add(ctx.question.getText());
      return visitChildren(ctx);
   }

   @Override public String visitNumeric(CQuizParser.NumericContext ctx) {
      questionIds.add(ctx.question.getText());
      return visitChildren(ctx);
   }

   @Override public String visitLong(CQuizParser.LongContext ctx) {
      questionIds.add(ctx.question.getText());
      return visitChildren(ctx);
   }

   @Override public String visitTuple(CQuizParser.TupleContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitList(CQuizParser.ListContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitTupElem(CQuizParser.TupElemContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitTupLst(CQuizParser.TupLstContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitLstWrd(CQuizParser.LstWrdContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitLstNum(CQuizParser.LstNumContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitLstTup(CQuizParser.LstTupContext ctx) {
      return visitChildren(ctx);
   }

   public List<String> getQuestionIds() {
      return questionIds;
   }
}
