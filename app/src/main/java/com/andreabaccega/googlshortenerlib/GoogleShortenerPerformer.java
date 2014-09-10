package com.andreabaccega.googlshortenerlib;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by andrea on 09/09/14.
 */
public class GoogleShortenerPerformer {
    private final OkHttpClient httpClient;


    public GoogleShortenerPerformer(OkHttpClient client) { this.httpClient = client; }

    public GooglShortenerResult shortenUrl(Request request)  {
        Response r = null;
        try {
            r = httpClient.newCall(request).execute();
            if (r.code() != 200) {
                return GooglShortenerResult.buildFail("Status Code is not 200 -> Received: " + r.code());
            } else {
                String responseBody = r.body().string();
                try {
                    GooglShortenResult googlShortenResult = new Gson().fromJson(responseBody, GooglShortenResult.class);
                    if (googlShortenResult != null && ! TextUtils.isEmpty(googlShortenResult.getId())) {
                        return GooglShortenerResult.buildSuccess(googlShortenResult.getId());
                    } else {
                        return GooglShortenerResult.buildFail("Shortened url is null. Response body: " + responseBody);
                    }
                } catch (JsonSyntaxException e) {
                    return GooglShortenerResult.buildFail(e.getMessage());
                }
            }
        } catch (IOException e) {
            return GooglShortenerResult.fromFail(e);
        }

    }





    private static class GooglShortenResult {
        private String id;

        public String getId() {
            return id;
        }
    }

}
