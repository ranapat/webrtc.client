package org.ranapat.webrtc.example.webrtc.command;

import java.util.HashMap;
import java.util.Map;

public final class Commands {
    public static final String LOGIN = "login";
    public static final String UPDATE = "update";
    public static final String JOIN = "join";
    public static final String LEAVE = "leave";
    public static final String CREATE_OFFER = "init";
    public static final String CREATE_ANSWER = "offer";
    public static final String SET_REMOTE_DESCRIPTION = "answer";
    public static final String ADD_ICE_CANDIDATE = "candidate";
    public static final String RING = "ring";
    public static final String BUSY = "busy";
    public static final String CANCEL = "cancel";
    public static final String TERMINATE = "terminate";
    public static final String UNLOCK = "unlock";

    private final Map<String, Command> map;

    public Commands() {
        map = new HashMap<String, Command>(){{
            put(LOGIN, new Login());
            put(UPDATE, new Update());
            put(JOIN, new Join());
            put(LEAVE, new Leave());
            put(CREATE_OFFER, new CreateOffer());
            put(CREATE_ANSWER, new CreateAnswer());
            put(SET_REMOTE_DESCRIPTION, new SetRemoveDescription());
            put(ADD_ICE_CANDIDATE, new AddIceCandidate());
            put(RING, new Ring());
            put(BUSY, new Busy());
            put(CANCEL, new Cancel());
            put(TERMINATE, new Terminate());
            put(UNLOCK, new Unlock());
        }};
    }

    public Command get(final String type) {
        return map.get(type);
    }
}
