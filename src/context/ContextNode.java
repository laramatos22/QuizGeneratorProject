package context;

import types.Type;

/**
 * Stores one variable and a reference to another.
 * This works much like a linked list node, however it links to a previous node so it is easier to pop contexts
 *
 * @author João Felisberto
 */
class ContextNode {
    private final Type<?> variable;
    private ContextNode previous;

    ContextNode(Type<?> variable) {
        this.variable = variable;
        this.previous = null;
    }

    /**
     * Tells whether any of the nodes contains a variable with the name varName
     *
     * @param varName The name of the variable to be tested
     * @return true if any of the nodes linked to this contain a variable with the same name as var, false otherwise
     */
    public boolean contains(String varName) {
        if (variable.getName().equals(varName))
            return true;

        if (previous == null)
            return variable.getName().equals(varName);

        return previous.contains(varName);
    }

    public Type<?> getVariable() {
        return variable;
    }

    public ContextNode getPrevious() {
        return previous;
    }

    public void setPrevious(ContextNode previous) {
        this.previous = previous;
    }
}
