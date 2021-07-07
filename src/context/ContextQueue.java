package context;

import types.Type;

import java.util.Stack;

/**
 * Helps the management of variable contexts.
 *
 * @author João Felisberto
 */
public class ContextQueue {
    private final Stack<Integer> contexts = new Stack<>();

    private ContextNode last = null;
    private int size = 0;

    /**
     * Returns the variable which name is name.
     * Searches in all accessible contexts.
     *
     * @param name The name to be searched for.
     * @return The variable which name is name, null if there is none.
     */
    public Type<?> getVariable(String name) {
        if (name == null)
            return null;

        if (last == null)
            return null;

        ContextNode tmp = last;
        while (tmp != null) {
            if (tmp.getVariable().getName().equals(name))
                return tmp.getVariable();

            tmp = tmp.getPrevious();
        }

        return null;
    }


    /**
     * Adds a variable to the current context.
     * If var is null it will not do anything, since it makes no sense to add null.
     *
     * @param var The variable to be added
     * @param <T> The variable data's type
     */
    public <T extends Type<?>> void registerVar(T var) {
        if (var == null)
            return;

        if (last == null || !last.contains(var.getName()))
            add(var);
    }

    /**
     * Creates a new context
     */
    public void pushContext() {
        contexts.push(size);
    }

    /**
     * Deletes the current context.
     * All variables in the current context are lost at the eyes of the program.
     */
    public void popContext() {
        if (contexts.empty())
            return;

        final int newSize = contexts.pop();

        while (size != newSize) {
            final ContextNode tmp = last;
            last = last.getPrevious();

            tmp.setPrevious(null);

            size--;
        }
    }

    private <T extends Type<?>> void add(T var) {
        final ContextNode tmp = last;
        last = new ContextNode(var);
        last.setPrevious(tmp);
        size++;
    }

    public int getSize() {
        return size;
    }
}
