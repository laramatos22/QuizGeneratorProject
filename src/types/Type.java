package types;

/**
 * Represents the data a variable can contain.
 *
 * @author João Felisberto
 */
public abstract class Type<T> {
    private final Types type;
    private T data;
    private String name;

    /**
     * Creates a variable object with all data it contains
     *
     * @param type The variable data type
     * @param data The data it stores
     * @param name The variable name
     */
    public Type(Types type, T data, String name) {
        this.type = type;
        this.data = data;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Types getType() {
        return type;
    }

    public T getData() {
        return data;
    }

    /**
     * Sets a new name IF the variable has no name.
     * @param name new name of the variable
     */
    public void setName(String name) {
        if (this.name == null)
            this.name = name;
    }

    public void setData(T data) {
        this.data = data;
    }
}
