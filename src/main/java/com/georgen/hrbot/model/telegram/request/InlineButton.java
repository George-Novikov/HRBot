package com.georgen.hrbot.model.telegram.request;

public class InlineButton {
    private String text;
    private String url;

    public InlineButton() {}

    public InlineButton(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
