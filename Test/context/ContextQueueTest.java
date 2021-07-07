package context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import types.NumberType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ContextQueueTest {

    @Test
    void registerVar() {
        final ContextQueue ctxq = new ContextQueue();

        final NumberType a = new NumberType(1, "a");
        final NumberType b = new NumberType(2, "b");

        // int the beginning there are no variables
        assertEquals(0, ctxq.getSize());

        // registering with last = null
        ctxq.registerVar(a);
        assertEquals(1, ctxq.getSize());

        // registering with last != null
        ctxq.registerVar(b);
        assertEquals(2, ctxq.getSize());

        // add null
        ctxq.registerVar(null);
        assertEquals(2, ctxq.getSize());
    }

    @Test
    void getVariable() {
        final ContextQueue ctxq = new ContextQueue();

        final String var1 = "a";
        final NumberType a = new NumberType(1, var1);
        assertNull(ctxq.getVariable(var1));

        ctxq.registerVar(a);

        assertEquals(a, ctxq.getVariable(var1));
    }

    @Test
    void popNoContext() {
        final ContextQueue ctxq = new ContextQueue();

        // pop no context
        ctxq.popContext();
    }

    @Test
    void emptyContext() {
        final ContextQueue ctxq = new ContextQueue();

        final String var1 = "a";
        final NumberType a = new NumberType(1, var1);

        // create an empty context
        ctxq.registerVar(a);
        final int expected = ctxq.getSize();

        ctxq.pushContext();
        ctxq.popContext();

        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertEquals(expected, ctxq.getSize());
    }

    @Test
    void emptyContextInMidle() {
        final ContextQueue ctxq = new ContextQueue();

        final String var1 = "a";
        final NumberType a = new NumberType(1, var1);
        final String var2 = "b";
        final NumberType b = new NumberType(2, var2);
        final String var3 = "c";
        final NumberType c = new NumberType(2, var3);

        ctxq.registerVar(a);

        ctxq.pushContext();
        ctxq.pushContext();

        ctxq.registerVar(b);
        ctxq.registerVar(c);

        ctxq.pushContext();
        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertNotNull(ctxq.getVariable(var2));
        Assertions.assertNotNull(ctxq.getVariable(var3));

        ctxq.pushContext();
        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertNotNull(ctxq.getVariable(var2));
        Assertions.assertNotNull(ctxq.getVariable(var3));

        ctxq.popContext();
        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertNotNull(ctxq.getVariable(var2));
        Assertions.assertNotNull(ctxq.getVariable(var3));

        ctxq.popContext();
        ctxq.popContext();
        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertNull(ctxq.getVariable(var2));
        Assertions.assertNull(ctxq.getVariable(var3));
    }

    @Test
    void populatedContext() {
        final ContextQueue ctxq = new ContextQueue();

        final String var1 = "a";
        final NumberType a = new NumberType(1, var1);
        final String var2 = "b";
        final NumberType b = new NumberType(2, var2);

        ctxq.registerVar(a);

        // populate a context
        ctxq.pushContext();
        ctxq.registerVar(b);

        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertNotNull(ctxq.getVariable(var2));

        ctxq.popContext();
        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertNull(ctxq.getVariable(var2));
    }

    @Test
    public void registerWithSameName() {
        final ContextQueue ctxq = new ContextQueue();

        final String var1 = "a";
        final NumberType a = new NumberType(1, var1);
        final NumberType b = new NumberType(2, var1);

        ctxq.registerVar(a);
        Assertions.assertEquals(1, ctxq.getSize());

        ctxq.registerVar(b);
        Assertions.assertEquals(1, ctxq.getSize());
    }

    @Test
    public void moreVarsPerContext() {
        final ContextQueue ctxq = new ContextQueue();

        final String var1 = "a";
        final NumberType a = new NumberType(1, var1);
        final String var2 = "b";
        final NumberType b = new NumberType(2, var2);
        final String var3 = "c";
        final NumberType c = new NumberType(3, var3);
        final String var4 = "d";
        final NumberType d = new NumberType(4, var4);

        ctxq.registerVar(a);
        ctxq.registerVar(c);

        // populate a context
        ctxq.pushContext();
        ctxq.registerVar(b);
        ctxq.registerVar(d);

        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertNotNull(ctxq.getVariable(var2));
        Assertions.assertNotNull(ctxq.getVariable(var3));
        Assertions.assertNotNull(ctxq.getVariable(var4));

        ctxq.popContext();
        Assertions.assertNotNull(ctxq.getVariable(var1));
        Assertions.assertNotNull(ctxq.getVariable(var3));
        Assertions.assertNull(ctxq.getVariable(var2));
        Assertions.assertNull(ctxq.getVariable(var4));
    }
}