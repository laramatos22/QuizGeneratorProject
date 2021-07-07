package types;

/**
 * @author João Felisberto
 */
public enum Types {
    QUESTION("question"), NUMBER("num"), TEXT("text"), LIST("list"), TIME("time");

    private final String id;

    Types(String id) {
        this.id = id;
    }

    public static Types get(String type) {

        switch (type) {
            case "question":
                return QUESTION;
            case "num":
                return NUMBER;
            case "text":
                return TEXT;
            case "list":
                return LIST;
            case "time":
                return TIME;
            default:
                return null;
        }
    }

    public String getId() {
        return id;
    }
}
