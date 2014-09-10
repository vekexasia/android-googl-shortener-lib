package com.andreabaccega.googlshortenerlib.tests;

import com.andreabaccega.googlshortenerlib.GooglShortenerResult;
import com.andreabaccega.googlshortenerlib.GoogleShortenerPerformer;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import static org.hamcrest.Matchers.*;

/**
 * Created by andrea on 09/09/14.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ShortnerTest {
    private MockWebServer server;
    private OkHttpClient client;
    private MockRequestBuilder requestBuilder;
    private static final String testPath = "/testPath";
    private GoogleShortenerPerformer performer;

    @Before
    public void before() throws IOException {
        server = new MockWebServer();

        server.play();
        client = new OkHttpClient();
        performer = new GoogleShortenerPerformer(client);
        URL url = server.getUrl(testPath);

        requestBuilder = new MockRequestBuilder(null, url.getPath(), url.getAuthority(), url.getProtocol() );
    }

    public void after() throws IOException {
        server.shutdown();
    }
    @Test
    public void test404() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(404));

        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/"));

        Assert.assertThat(googlShortenerResult.getStatus(), is( GooglShortenerResult.Status.RESPONSE_ERROR ));
        Assert.assertThat(googlShortenerResult.getShortenedUrl(), is( nullValue() ));
        Assert.assertThat(googlShortenerResult.getException().getMessage(), containsString("is not 200 -> Received: 404"));
    }

    @Test
    public void testEmptyResponse() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200));
        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/"));

        Assert.assertThat(googlShortenerResult.getStatus(), is( not(GooglShortenerResult.Status.SUCCESS )));
        Assert.assertThat(googlShortenerResult.getShortenedUrl(), is( nullValue() ));
        Assert.assertThat(googlShortenerResult.getException().getMessage(), is("Shortened url is null. Response body: "));

    }

    @Test
    public void testMalformedResponse() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("not a json"));
        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/"));

        Assert.assertThat(googlShortenerResult.getStatus(), is( GooglShortenerResult.Status.RESPONSE_ERROR ));
        Assert.assertThat(googlShortenerResult.getShortenedUrl(), is( nullValue() ));
        Assert.assertThat(googlShortenerResult.getException().getMessage(), containsString("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1"));

    }

    @Test
    public void testEmptyObjectResponse() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{}"));
        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/"));

        Assert.assertThat(googlShortenerResult.getStatus(), is( GooglShortenerResult.Status.RESPONSE_ERROR ));
        Assert.assertThat(googlShortenerResult.getShortenedUrl(), is( nullValue() ));
        Assert.assertThat(googlShortenerResult.getException().getMessage(), is("Shortened url is null. Response body: {}"));

    }

    @Test
    public void testOkResponse() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{id:'http://goo.gl/test'}"));
        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/"));

        Assert.assertThat(googlShortenerResult.getStatus(), is( GooglShortenerResult.Status.SUCCESS ));
        Assert.assertThat(googlShortenerResult.getShortenedUrl(), is( "http://goo.gl/test" ));
        Assert.assertThat(googlShortenerResult.getException(), is( nullValue() ));
    }

    @Test
    public void testHostError() throws InterruptedException {
        requestBuilder.setAuthority("some lol domain");
        server.enqueue(new MockResponse().setBody("{id:'http://goo.gl/test'}"));
        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/"));

        Assert.assertThat(googlShortenerResult.getStatus(), is( GooglShortenerResult.Status.IO_EXCEPTION ));
        Assert.assertThat(googlShortenerResult.getShortenedUrl(), is( nullValue() ));
        Assert.assertThat(googlShortenerResult.getException(), is( not(nullValue()) ));
    }

    /*
    @Test
    public void testTimeoutError() throws InterruptedException, IOException {
        client.setConnectTimeout(100, TimeUnit.MILLISECONDS);
        client.setReadTimeout(100, TimeUnit.MILLISECONDS);
        performer = new GoogleShortenerPerformer(client);
        server.enqueue(new MockResponse().setBody("{id:'http://goo.gl/test'}").setBodyDelayTimeMs(10000));
        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/"));
        Assert.assertThat(googlShortenerResult.getStatus(), is( GooglShortenerResult.Status.IO_EXCEPTION ));
        Assert.assertThat(googlShortenerResult.getShortenedUrl(), is( nullValue() ));
        Assert.assertThat(googlShortenerResult.getException(), is( not( nullValue() ) ));
    }
    */

    @Test
    public void testUsesApiKeyInRequest() throws InterruptedException {
        String apiKey = "myApiKey";
        server.enqueue(new MockResponse().setResponseCode(200));
        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/", apiKey));

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertThat(recordedRequest.getPath(), containsString(requestBuilder.getApiKeyUrlParameterName() + "=" + apiKey) );
    }
    @Test
    public void testSentBodyIsOk() throws InterruptedException, UnsupportedEncodingException {
        server.enqueue(new MockResponse().setResponseCode(200));
        GooglShortenerResult googlShortenerResult = performer.shortenUrl(requestBuilder.buildRequest("http://www.andreabaccega.com/"));

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertThat(recordedRequest.getMethod(), is("POST"));
        Assert.assertThat(recordedRequest.getHeader("Content-Type"), containsString("application/json"));
        Assert.assertThat(new String(recordedRequest.getBody(), "UTF8"), is("{\"longUrl\":\"http://www.andreabaccega.com/\"}"));

    }


}
