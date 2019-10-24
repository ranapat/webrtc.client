package org.ranapat.webrtc.example.api.tools;

import android.content.Context;
import android.graphics.Bitmap;

import org.ranapat.webrtc.example.Settings;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.ApplicationContext;
import org.ranapat.webrtc.example.management.NetworkManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import gherkin.lexer.Encoding;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class LogDecorationTest {

    @Test
    public void shouldGetInstance() {
        //
    }

    @Test
    public void shouldGetCurrentClassName() {
        //
    }

    @Test
    public void shouldCallerClassAndMethod() {
        //
    }

}