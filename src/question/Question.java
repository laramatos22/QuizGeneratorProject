package question;

import types.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Question {
    private final String id;
    private String question;
    private QuestionTypes type;
    private List<String> dependencies;
    private List<String> groups;
    private List<String> qans;
    private List<?> correctList; // only for not long questions
    private String correctString; // only long questions
    private String presentation;
    private String report;
    private long time = 0;

    private TextType userAns = null;

   public Question(String id,
                   String q,
                   QuestionTypes type,
                   List<String> groups,
                   List<String> depend,
                   List<String> ans,
                   Object correct) {
       this(id, q, type, depend, groups, ans,
               correct instanceof List ? (List<?>) correct : null,
               correct instanceof String ? (String) correct : null,
               null,
               null
               );
   }

    public Question(String id, String question,
                    QuestionTypes type,
                    List<String> dependencies,
                    List<String> groups,
                    List<String> qans,
                    List<?> correctList,
                    String correctString,
                    String presentation,
                    String report) {
        this.id = id;
        this.question = question;
        this.type = type;
        this.dependencies = dependencies;
        this.groups = groups;
        this.qans = qans;
        this.correctList = correctList;
        this.correctString = correctString;
        this.presentation = presentation != null ? presentation : "Q: " + question + "?";
        this.report = report != null ? report :
                "Q: " + question + "?\n"
                        + "A: " + (userAns == null ? "" : userAns.getData());
    }

    /**
     * Stores an answer if it is valid.
     * A valid answer may not be correct. Answering a text to a numeric question will return false,
     * but answering "1" when the correct answer is "2" will return true
     *
     * @param ans  The answer to be validated and stored
     * @param time The time the user took to answer the question
     * @return true if it is valid, false otherwise
     */
    public boolean answer(TextType ans, long time) {
        switch (type) {
            case LONG:
                this.userAns = ans;
                return true;
            case SHORT:
                if (ans.getData().split(" ").length > ((correctList.get(0) instanceof Integer) ? (int) correctList.get(0) : (int) correctList.get(1)))
                    return false;
                this.userAns = ans;
                this.time += time;
                return true;
            case NUMERIC:
                try {
                    Cast.textToNumber(ans);
                    userAns = ans;
                    this.time += time;
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case MATCHING:
                // Matching to be answered like "A1-B1 A2-B2 ..."
                final String[] pairs = ans.getData().split(" ");

                for (String pair : pairs) {
                    final String[] elems = pair.split("-");

                    if (elems.length != 2)
                        return false;

                    if (!((List<String>) correctList.get(0)).contains(elems[0]) || !((List<String>) correctList.get(1)).contains(elems[1]))
                        return false;
                }

                userAns = ans;
                this.time += time;
                return true;
            case MULTIPLE_ANS:
                final String[] choices = ans.getData().split(" ");

                if (!qans.containsAll(Arrays.asList(choices)))
                    return false;

                userAns = ans;
                this.time += time;
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks whether the answer given to this question is correct or not
     *
     * @return true if the answer is correct, false otherwise
     */
    public boolean validate() {
        if (userAns == null)
            return false;

        switch (type) {
            case LONG:
                final Pattern pattern = Pattern.compile(correctString);
                return pattern.matcher(userAns.getData()).matches();
            case SHORT:
                final String regex = correctList.get(0) instanceof String ? (String) correctList.get(0) : (String) correctList.get(1);
                final int limit = correctList.get(0) instanceof Integer ? (int) correctList.get(0) : (int) correctList.get(1);

                if (userAns.getData().split(" ").length > limit)
                    return false;

                final Pattern pattern2 = Pattern.compile(regex);
                return pattern2.matcher(userAns.getData()).matches();
            case MULTIPLE_ANS:
            case MATCHING:
                final List<String> toValidate = Arrays.asList(userAns.getData().split(" "));
                return toValidate.containsAll(correctList) && toValidate.size() == correctList.size(); // <-- possible error
            case NUMERIC:
                final double ans = Double.parseDouble(userAns.getData());
                for (Object o : correctList) {
                    if (o instanceof List) {
                        final List<NumberType> interval = (List<NumberType>) o;
                        if (interval.get(0).getData() <= ans && ans <= interval.get(1).getData())
                            return true;
                    } else if (o instanceof NumberType) {
                        if (((NumberType) o).getData() == ans)
                            return true;
                    } else
                        return false;
                }

                return false;
        }

        return false;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionTypes getType() {
        return type;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getQans() {
        return qans;
    }

    public void setQans(List<String> qans) {
        this.qans = qans;
    }

    public List<?> getCorrectList() {
        return correctList;
    }

    public void setCorrectList(List<String> correctList) {
        this.correctList = correctList;
    }

    public String getCorrectString() {
        return correctString;
    }

    public void setCorrectString(String correctString) {
        this.correctString = correctString;
    }

    public TextType getUserAnswer() {
        return this.userAns;
    }

    public String getPresentation() {
        return presentation;
    }

    public String getReport() {
        System.out.println("report null? " + (report == null));
        return report;
    }

    public long getElapsed() {
        return time;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }
}
