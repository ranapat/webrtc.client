package org.ranapat.webrtc.example.ui.publishable;

import android.content.Context;

import org.junit.Test;

import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.ApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParameterizedMessageTest {

    @Test
    public void shouldHandleIntegersCase1() {
        final Context context = mock(Context.class);

        InstanceFactory.set(ApplicationContext.class, context);

        when(context.getString(1)).thenReturn("result");

        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage(1);
        assertEquals("result", parameterizedMessage.getMessage());
        assertEquals(0, parameterizedMessage.parameters.length);

        InstanceFactory.remove(ApplicationContext.class);
    }

    @Test
    public void shouldHandleIntegersCase2() {
        final Context context = mock(Context.class);

        InstanceFactory.set(ApplicationContext.class, context);

        when(context.getString(1)).thenReturn("result %s");

        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage(1, new Object[]{"something"});
        assertEquals("result something", parameterizedMessage.getMessage());
        assertEquals(1, parameterizedMessage.parameters.length);

        InstanceFactory.remove(ApplicationContext.class);
    }

    @Test
    public void shouldHandleIntegersCase3() {
        final Context context = mock(Context.class);

        InstanceFactory.set(ApplicationContext.class, context);

        when(context.getString(1)).thenReturn("result");

        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage(2);
        assertEquals("", parameterizedMessage.getMessage());
        assertEquals(0, parameterizedMessage.parameters.length);

        InstanceFactory.remove(ApplicationContext.class);
    }

    @Test
    public void shouldHandleIntegersCase4() {
        final Context context = mock(Context.class);

        InstanceFactory.set(ApplicationContext.class, context);

        when(context.getString(1)).thenReturn("result %s");

        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage(2, new Object[]{"something"});
        assertEquals("", parameterizedMessage.getMessage());
        assertEquals(1, parameterizedMessage.parameters.length);

        InstanceFactory.remove(ApplicationContext.class);
    }

    @Test
    public void shouldHandleIntegersCase5() {
        final Context context = mock(Context.class);

        InstanceFactory.set(ApplicationContext.class, context);

        when(context.getString(1)).thenReturn(null);

        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage(1);
        assertEquals("", parameterizedMessage.getMessage());
        assertEquals(0, parameterizedMessage.parameters.length);

        InstanceFactory.remove(ApplicationContext.class);
    }

    @Test
    public void shouldHandleIntegersCase6() {
        final Context context = mock(Context.class);

        InstanceFactory.set(ApplicationContext.class, context);

        when(context.getString(1)).thenReturn(null);

        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage(1, new Object[]{"something"});
        assertEquals("", parameterizedMessage.getMessage());
        assertEquals(1, parameterizedMessage.parameters.length);

        InstanceFactory.remove(ApplicationContext.class);
    }

    @Test
    public void shouldHandleStringsCase1() {
        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage("result");
        assertEquals("result", parameterizedMessage.getMessage());
        assertEquals(0, parameterizedMessage.parameters.length);
    }

    @Test
    public void shouldHandleStringsCase2() {
        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage("result %s", new Object[]{"something"});
        assertEquals("result something", parameterizedMessage.getMessage());
        assertEquals(1, parameterizedMessage.parameters.length);
    }

    @Test
    public void shouldHandleStringsCase3() {
        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage(null);
        assertEquals("", parameterizedMessage.getMessage());
        assertEquals(0, parameterizedMessage.parameters.length);
    }

    @Test
    public void shouldHandleStringsCase4() {
        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage(null, new Object[]{"something"});
        assertEquals("", parameterizedMessage.getMessage());
        assertEquals(1, parameterizedMessage.parameters.length);
    }

    @Test
    public void shouldHandleStringsCase5() {
        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage("");
        assertEquals("", parameterizedMessage.getMessage());
        assertEquals(0, parameterizedMessage.parameters.length);
    }

    @Test
    public void shouldHandleStringsCase6() {
        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage("", new Object[]{"something"});
        assertEquals("", parameterizedMessage.getMessage());
        assertEquals(1, parameterizedMessage.parameters.length);
    }

    @Test
    public void shouldHandleToString() {
        final ParameterizedMessage parameterizedMessage = new ParameterizedMessage("result");
        assertEquals(parameterizedMessage.toString(), parameterizedMessage.getMessage());
    }

}