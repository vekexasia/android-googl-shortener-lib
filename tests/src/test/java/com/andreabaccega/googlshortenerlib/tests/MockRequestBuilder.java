package com.andreabaccega.googlshortenerlib.tests;

import com.andreabaccega.googlshortenerlib.GooglShortenerRequestBuilder;

/**
* Created by andrea on 10/09/14.
*/
class MockRequestBuilder extends GooglShortenerRequestBuilder {
    private String apiKeyParameter;
    private String path;
    private String authority;
    private String schema;

    MockRequestBuilder(String apiKeyParameter, String path, String authority, String schema) {
        this.apiKeyParameter = apiKeyParameter;
        this.path = path;
        this.authority = authority;
        this.schema = schema;
    }

    @Override
    protected String getApiKeyUrlParameterName() {
        if (apiKeyParameter == null) {
            return super.getApiKeyUrlParameterName();
        } else {
            return apiKeyParameter;
        }
    }

    @Override
    protected String getPath() {
        if (path == null) {
            return super.getPath();
        } else {
            return path;
        }
    }

    @Override
    protected String getAuthority() {
        if (authority == null) {
            return super.getAuthority();
        } else {
            return authority;
        }
    }

    @Override
    protected String getSchema() {
        if (schema == null) {
            return super.getSchema();
        } else {
            return schema;
        }
    }

    public void setApiKeyParameter(String apiKeyParameter) {
        this.apiKeyParameter = apiKeyParameter;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
