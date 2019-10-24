package org.ranapat.webrtc.example.webrtc;

public class EnvironmentNotInitializedException extends RuntimeException {
    public EnvironmentNotInitializedException() {
        super("Environment is not initialized");
    }
}
