package org.ranapat.webrtc.example.webrtc.command;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CommandsTest {

    @Test
    public void shouldGetNonExisting() {
        final Commands commands = new Commands();

        assertThat(commands.get("_undefined_"), is(equalTo(null)));
    }

    @Test
    public void shouldGetLogin() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.LOGIN), is(instanceOf(Login.class)));
    }

    @Test
    public void shouldGetUpdate() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.UPDATE), is(instanceOf(Update.class)));
    }

    @Test
    public void shouldGetCreateOffer() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.CREATE_OFFER), is(instanceOf(CreateOffer.class)));
    }

    @Test
    public void shouldGetCreateAnswer() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.CREATE_ANSWER), is(instanceOf(CreateAnswer.class)));
    }

    @Test
    public void shouldGetSetRemoveDescription() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.SET_REMOTE_DESCRIPTION), is(instanceOf(SetRemoveDescription.class)));
    }

    @Test
    public void shouldGetAddIceCandidate() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.ADD_ICE_CANDIDATE), is(instanceOf(AddIceCandidate.class)));
    }

    @Test
    public void shouldGetRing() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.RING), is(instanceOf(Ring.class)));
    }

    @Test
    public void shouldGetBusy() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.BUSY), is(instanceOf(Busy.class)));
    }

    @Test
    public void shouldGetCancel() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.CANCEL), is(instanceOf(Cancel.class)));
    }

    @Test
    public void shouldGetTerminate() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.TERMINATE), is(instanceOf(Terminate.class)));
    }

    @Test
    public void shouldGetUnlock() {
        final Commands commands = new Commands();

        assertThat(commands.get(Commands.UNLOCK), is(instanceOf(Unlock.class)));
    }

}