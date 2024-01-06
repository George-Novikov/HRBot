package com.fatemorgan.hrbot.network;

public class UrlParamBuilder {
    private StringBuilder builder;

    public UrlParamBuilder(){
        this.builder = new StringBuilder();
    }

    public UrlParamBuilder add(String name, String value){
        if (builder.length() > 0) builder.append("&");
        builder.append(name).append("=").append(value);
        return this;
    }

    public String build(){
        return builder.toString();
    }
}
