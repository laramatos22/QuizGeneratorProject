package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aux.CQuizLexer;
import aux.CQuizParser;
import aux.QuestionLister;
import aux.SemanticCheck;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.stringtemplate.v4.ST;
import question.Question;

public class QuizMain {
   public static void main(String[] args) {
      try {
         // create a CharStream that reads from standard input:
         CharStream input = CharStreams.fromStream(System.in);
         // create a lexer that feeds off of input CharStream:
         QuizLexer lexer = new QuizLexer(input);
         // create a buffer of tokens pulled from the lexer:
         CommonTokenStream tokens = new CommonTokenStream(lexer);
         // create a parser that feeds off the tokens buffer:
         QuizParser parser = new QuizParser(tokens);
         // replace error listener:
         //parser.removeErrorListeners(); // remove ConsoleErrorListener
         //parser.addErrorListener(new ErrorHandlingListener());
         // begin parsing at main rule:
         ParseTree tree = parser.main();
         if (parser.getNumberOfSyntaxErrors() == 0) {
            QDBVisitor visitor0 = new QDBVisitor();
            visitor0.visit(tree);

            final List<Question> qs = new ArrayList<>();
            visitor0.getFiles().forEach(f -> qs.addAll(invokeAux(f)));

            SemanticAnalysis visitor1 = new SemanticAnalysis(qs);
            visitor1.visit(tree);

            Compiler c = new Compiler(qs);
            final ST res = c.visit(tree);
            System.out.println(res.render());
         }
      }
      catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }
      catch(RecognitionException e) {
         e.printStackTrace();
         System.exit(1);
      }
   }

   private static List<Question> invokeAux(String fName) {
      try {
         // create a CharStream that reads from file:
         CharStream input = CharStreams.fromFileName(fName);
         // create a lexer that feeds off of input CharStream:
         CQuizLexer lexer = new CQuizLexer(input);
         // create a buffer of tokens pulled from the lexer:
         CommonTokenStream tokens = new CommonTokenStream(lexer);
         // create a parser that feeds off the tokens buffer:
         CQuizParser parser = new CQuizParser(tokens);
         // replace error listener:
         //parser.removeErrorListeners(); // remove ConsoleErrorListener
         //parser.addErrorListener(new ErrorHandlingListener());
         // begin parsing at main rule:
         ParseTree tree = parser.main();
         if (parser.getNumberOfSyntaxErrors() == 0) {
            // print LISP-style tree:
            // System.out.println(tree.toStringTree(parser));

            // First list all questions
            QuestionLister visitor0 = new QuestionLister();
            visitor0.visit(tree);

            // Then perform analysis on those questions
            SemanticCheck visitor1 = new SemanticCheck(visitor0.getQuestionIds());
            visitor1.visit(tree);

            return visitor1.getQuestions();
         }
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      } catch (RecognitionException e) {
         e.printStackTrace();
         System.exit(1);
      }
      return new ArrayList<>();
   }
}
