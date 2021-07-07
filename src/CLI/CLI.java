package CLI;

import question.Question;
import types.QuestionType;
import types.TextType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Para o CLI é preciso:
 * - módulo de IO, para dar output (mediante o tipo de pergunta) e recolher respostas
 * - interface para que o programa compilado possa interagir com o CLI
 **/
public class CLI {

    private static String reportName;

    private static final Map<String, Boolean> groupAllowMoveMap = new HashMap<>();
    private static final List<QuestionType> questions = new ArrayList<>();

    private static QuestionType current = null;

    public static void main(String[] args) {
        /*
        groupAllowMoveMap must be set at the beginning of the script and updated when new questions are created.
        When questions are created they need to be added to the questions set.
        When "ans" is called, current should be set to the current question, on end it should be set to null.
         */
    }

    public static QuestionType registerQ(QuestionType q) {
        questions.add(q);
        return q;
    }

    public static void ask(QuestionType questionType) {
        current = questionType;
        final Scanner sc = new Scanner(System.in);

        System.out.println(current.property("present"));

        System.out.println(current.getData().getPresentation());

        while (true) {
            System.out.println("Answer: ");
            long time = System.currentTimeMillis();
            String line = sc.nextLine();
            time = time - System.currentTimeMillis();

            if (line.equals("--fwd")) {
                if (!canMove(current)) {
                    System.err.println("You cannot move!");
                    continue;
                }
                forward();
                continue;
            } else if (line.equals("--bck")) {
                if (!canMove(current)) {
                    System.err.println("You cannot move!");
                    continue;
                }
                backward();
                continue;
            } else if (line.startsWith("--")) {
                System.err.println("Unrecognized command");
                continue;
            }

            final List<String> depIds = current.getData().getDependencies();

            final List<String> allIds = questions.stream().map(QuestionType::getData).map(Question::getId).collect(Collectors.toList());
            if (!(allIds.containsAll(depIds) &&
                    questions.stream()
                            .map(QuestionType::getData)
                            .filter(q -> depIds.contains(q.getId()))
                            .allMatch(q -> q.getUserAnswer() != null))) {
                System.err.println("You don't meet all dependencies to answer this question!");
                continue;
            }

            if (!current.getData().answer(new TextType(line, null), time)) {
                System.err.println("Invalid answer!");
                continue;
            }

            break;
        }
    }

    private static void backward() {
        int currI = questions.indexOf(current);

        for (int i = (currI + 1) % questions.size(); i != currI; i = (i - 1) % questions.size())
            for (String gp : current.getData().getGroups()) {
                final QuestionType q = questions.get(i);
                if (q.getData().getGroups().contains(gp)) {
                    current = q;
                    break;
                }
            }
    }

    private static void forward() {
        int currI = questions.indexOf(current);

        for (int i = (currI + 1) % questions.size(); i != currI; i = (i + 1) % questions.size())
            for (String gp : current.getData().getGroups()) {
                final QuestionType q = questions.get(i);
                if (q.getData().getGroups().contains(gp)) {
                    current = q;
                    break;
                }
            }
    }

    private static boolean canMove(QuestionType question) {
        return question.getData().getGroups().stream().allMatch(groupAllowMoveMap::get);
    }

    public static void report() throws IOException {

        final FileWriter fw = new FileWriter(reportName);

        questions.stream()
                .map(QuestionType::getData)
                .filter(question -> question.getUserAnswer() != null)
                .forEach(question -> {
                    try {
                        fw.append(question.getReport());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        fw.close();

    }

    public static long getGroupElapsedTime(String group) {
        return questions.stream()
                .map(QuestionType::getData)
                .filter(q -> q.getGroups().contains(group))
                .mapToLong(Question::getElapsed)
                .sum();
    }

    private static List<String> buildStrList(String... ss) {
        final List<String> built = new ArrayList<>();

        for (int i = 0; i < ss.length; i++)
            built.add(ss[i]);

        return built;
    }

}
