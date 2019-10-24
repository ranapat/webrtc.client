package org.ranapat.webrtc.example.api.tools;

public final class LogDecoration {
    private static LogDecoration instance;
    public static LogDecoration getInstance() {
        if (instance == null) {
            instance = new LogDecoration();
        }

        return instance;
    }

    private LogDecoration() {
        //
    }

    public String getCurrentMethodName() {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        final int length = stackTraceElements.length;
        boolean startToTrack = false;
        for (int i = 0; i < length; ++i) {
            final StackTraceElement stackTraceElement = stackTraceElements[i];

            if (stackTraceElement.getClassName().contains("BaseRao")) {
                startToTrack = true;
            } else if (startToTrack) {
                return stackTraceElement.getMethodName();
            }
        }

        return "undefined";
    }

    public String getCurrentClassName(final Class<?> _class) {
        final Class<?> enclosingClass = _class.getEnclosingClass();
        if (enclosingClass != null) {
            return enclosingClass.getSimpleName();
        } else {
            return _class.getSimpleName();
        }
    }

    public String getCallerClassAndMethod(final Class<?> _class) {
        return getCurrentClassName(_class) + "::" + getCurrentMethodName();
    }

}
