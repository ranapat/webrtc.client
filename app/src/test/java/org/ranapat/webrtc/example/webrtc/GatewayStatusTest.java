package org.ranapat.webrtc.example.webrtc;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GatewayStatusTest {

    @Test
    public void shouldConstruct() {
        final GatewayStatus gatewayStatus = new GatewayStatus("status", "message");

        assertThat(gatewayStatus.status, is(equalTo("status")));
        assertThat(gatewayStatus.message, is(equalTo("message")));
    }

}