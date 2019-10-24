package org.ranapat.webrtc.example.dependencies;

import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class InstanceFactoryTest {

    @After
    public void after() {
        InstanceFactory.clear();
    }

    @Test
    public void gettingNotPresentInstanceShouldProduceNewInstance() {
        String instance = InstanceFactory.get(String.class);

        assertThat(instance, is(not(nullValue())));
    }

    @Test
    public void gettingTwoTimesSameClassInstanceShouldReturnSameInstance() {
        String instance1 = InstanceFactory.get(String.class);
        String instance2 = InstanceFactory.get(String.class);

        assertThat(instance1, is(sameInstance(instance2)));
    }

    @Test
    public void isSetInstanceForClassShouldWork() {
        String instance = "test";
        InstanceFactory.set(String.class, instance);

        assertThat(InstanceFactory.isSet(String.class), is(equalTo(true)));
    }

    @Test
    public void isSetInstanceForClassShouldAlsoWork() {
        String instance = "test";
        InstanceFactory.set(String.class, instance);
        InstanceFactory.remove(String.class);

        assertThat(InstanceFactory.isSet(String.class), is(equalTo(false)));
    }

    @Test
    public void setInstanceForClassShouldWork() {
        String instance = "test";
        InstanceFactory.set(String.class, instance);

        assertThat(InstanceFactory.get(String.class), is(sameInstance(instance)));
    }

    @Test
    public void removingInstanceAndGettingNewOneWorks() {
        String instance = "test";
        InstanceFactory.set(String.class, instance);
        InstanceFactory.remove(String.class);

        String instance2 = InstanceFactory.get(String.class);

        assertThat(instance, is(not(sameInstance(instance2))));
    }

    @Test
    public void clearingInstanceFactoryShouldLeadToAllInstanceBeingCreatedFreshly() {
        TestInstance integerInstance1 = InstanceFactory.get(TestInstance.class);
        String stringInstance1 = InstanceFactory.get(String.class);

        InstanceFactory.clear();

        TestInstance integerInstance2 = InstanceFactory.get(TestInstance.class);
        String stringInstance2 = InstanceFactory.get(String.class);

        assertThat(integerInstance1, is(not(sameInstance(integerInstance2))));
        assertThat(stringInstance1, is(not(sameInstance(stringInstance2))));
    }

    @Test
    public void instanceOfClassWithoutDefaultConstructorShouldLeadToNullValue() {
        TestClassWithPrivateConstructor testClassWithPrivateConstructor = InstanceFactory.get(TestClassWithPrivateConstructor.class);

        assertThat(testClassWithPrivateConstructor, is(nullValue()));
    }

    @Test
    public void instanceOfAbstractClassShouldBeNull() {
        TestAbstractClass testAbstractClass = InstanceFactory.get(TestAbstractClass.class);
        assertThat(testAbstractClass, is(nullValue()));
    }

    @Test
    public void shouldNotBeCreatedDirectly() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<InstanceFactory> constructor = InstanceFactory.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(equalTo(true)));

        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void shallWorkWithStaticallyMarked() {
        StaticallyMarked staticallyMarked = InstanceFactory.get(StaticallyMarked.class);
        assertThat(staticallyMarked, is(not(nullValue())));
    }
}

class TestInstance {
    private String test;

    public TestInstance() {
        this.test = "test";
    }
}

class TestClassWithPrivateConstructor {
    private TestClassWithPrivateConstructor() {}
}

@StaticallyInstantiable
class StaticallyMarked {
    public static StaticallyMarked getInstance() {
        return new StaticallyMarked();
    }

    private StaticallyMarked() {}
}

abstract class TestAbstractClass {}
