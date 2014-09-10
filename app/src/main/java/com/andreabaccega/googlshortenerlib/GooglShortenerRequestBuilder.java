package com.andreabaccega.googlshortenerlib;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/**
 * Created by andrea on 10/09/14.
 */
public class GooglShortenerRequestBuilder {
    private static final String GOOGL_SCHEMA           = "https";
    private static final String GOOGL_AUTHORITY        = "www.googleapis.com";
    private static final String GOOGL_PATH             = "/urlshortener/v1/url";
    private static final String GOOGL_APIKEY_PARAMETER = "key";

    public Request buildRequest(String urlToShorten) {
        return buildRequest(urlToShorten, null);
    }
    public Request buildRequest(String urlToShorten, String apiKey) {
        return new Request.Builder()
                .url(buildUrl(apiKey))
                .post(
                        RequestBody.create(
                                MediaType.parse("application/json"),
                                new Gson().toJson(new GooglShortenBody(urlToShorten))
                        )
                )
                .build();
    }

    private String buildUrl(String apiKey) {
        Uri.Builder builder = new Uri.Builder()
                .scheme(getSchema())
                .encodedAuthority(getAuthority())
                .appendEncodedPath(getPath());

        if ( ! TextUtils.isEmpty(apiKey) ) {
            builder.appendQueryParameter(getApiKeyUrlParameterName(), apiKey);
        }

        String lol = builder.toString();
        return lol;
    }

    protected String getApiKeyUrlParameterName() {
        return GOOGL_APIKEY_PARAMETER;
    }

    protected String getPath() {
        return GOOGL_PATH;
    }

    protected String getAuthority() {
        return GOOGL_AUTHORITY;
    }

    protected String getSchema() {
        return GOOGL_SCHEMA;
    }

    protected static class GooglShortenBody {
        private final String longUrl;

        private GooglShortenBody(String longUrl) {
            this.longUrl = longUrl;
        }


    }
}
