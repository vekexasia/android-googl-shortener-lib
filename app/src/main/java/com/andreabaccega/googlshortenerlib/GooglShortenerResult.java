package com.andreabaccega.googlshortenerlib;

import java.io.IOException;

/**
 * Created by andrea on 09/09/14.
 */
public class GooglShortenerResult {


    public static enum Status {
        SUCCESS,
        IO_EXCEPTION,
        RESPONSE_ERROR
    }
    private Status status;
    private String shortenedUrl;
    private Exception exception;

    private GooglShortenerResult() {};
    public GooglShortenerResult(Status status, String shortenedUrl) {
        this.status = status;
        this.shortenedUrl = shortenedUrl;
        this.exception = null;
    }

    public Status getStatus() {
        return status;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public Exception getException() {
        return exception;
    }

    public static GooglShortenerResult buildSuccess(String shortenedUrl) {
        GooglShortenerResult toRet = new GooglShortenerResult();
        toRet.status = Status.SUCCESS;
        toRet.shortenedUrl = shortenedUrl;
        return toRet;
    }

    public static GooglShortenerResult fromFail(IOException e) {
        GooglShortenerResult toRet = new GooglShortenerResult();
        toRet.status = Status.IO_EXCEPTION;
        toRet.exception = e;
        return toRet;
    }

    public static GooglShortenerResult buildFail(String s) {
        GooglShortenerResult toRet = new GooglShortenerResult();
        toRet.status = Status.RESPONSE_ERROR;
        toRet.exception = new GooglShortenerException(s);
        return toRet;
    }
}
