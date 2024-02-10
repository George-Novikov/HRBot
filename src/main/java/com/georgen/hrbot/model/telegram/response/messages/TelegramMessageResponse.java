package com.georgen.hrbot.model.telegram.response.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TelegramMessageResponse {
    private Boolean ok;
    private List<MessageUpdateResult> result;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public List<MessageUpdateResult> getResult() {
        return result;
    }

    public void setResult(List<MessageUpdateResult> result) {
        this.result = result;
    }

    public void cleanUpResiduals(){
        result = result
                .stream()
                .filter(updRes -> updRes.getMessage() != null)
                .collect(Collectors.toList());
    }

    public boolean isEmpty(){
        return this.result == null || this.result.isEmpty();
    }
}
