package org.ranapat.webrtc.example.dependencies;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final public class InstanceFactory {
    private static final Map<Class, Object> map = Collections.synchronizedMap(new HashMap<Class, Object>());

    private InstanceFactory() {
        //
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> T get(final Class<T> _class) {
        T result;

        if (map.containsKey(_class)) {
            result = (T) map.get(_class);
        } else {
            try {
                if (_class.isAnnotationPresent(StaticallyInstantiable.class)) {
                    final StaticallyInstantiable annotation = _class.getAnnotation(StaticallyInstantiable.class);
                    final Method method = _class.getDeclaredMethod(annotation.method());

                    result = (T) method.invoke(null);
                } else {
                    result = _class.newInstance();
                }

                map.put(_class, result);
            } catch (
                    final InstantiationException
                            | IllegalAccessException
                            | InvocationTargetException
                            | NoSuchMethodException
                            exception
                    ) {
                result = null;
            }
        }

        return result;
    }

    public static synchronized boolean isSet(final Class _class) {
        return map.containsKey(_class);
    }

    public static synchronized void set(final Class _class, final Object value) {
        map.put(_class, value);
    }

    public static synchronized void remove(final Class _class) {
        map.remove(_class);
    }

    public static synchronized void clear() {
        map.clear();
    }

}
