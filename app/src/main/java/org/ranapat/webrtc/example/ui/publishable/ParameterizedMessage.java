package org.ranapat.webrtc.example.ui.publishable;

import android.content.Context;

import java.io.Serializable;

import androidx.annotation.NonNull;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.ApplicationContext;

public class ParameterizedMessage implements Serializable {
    public final Object[] parameters;

    private final int messageId;
    private final String messageRaw;

    public ParameterizedMessage(final int messageId) {
        this.messageId = messageId;
        this.messageRaw = null;
        this.parameters = new Object[] {};
    }

    public ParameterizedMessage(final int messageId, final Object[] parameters) {
        this.messageId = messageId;
        this.messageRaw = null;
        this.parameters = parameters;
    }

    public ParameterizedMessage(final String messageRaw) {
        this.messageId = 0;
        this.messageRaw = messageRaw;
        this.parameters = new Object[] {};
    }

    public ParameterizedMessage(final String messageRaw, final Object[] parameters) {
        this.messageId = 0;
        this.messageRaw = messageRaw;
        this.parameters = parameters;
    }

    public String getMessage() {
        if (messageRaw != null && !messageRaw.isEmpty()) {
            return String.format(messageRaw, parameters);
        } else if (messageId != 0) {
            final String message = ((Context) InstanceFactory.get(ApplicationContext.class)).getString(messageId);
            if (message != null && !message.isEmpty()) {
                return String.format(message, parameters);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return getMessage();
    }
}
