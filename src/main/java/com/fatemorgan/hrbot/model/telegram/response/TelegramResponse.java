package com.fatemorgan.hrbot.model.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TelegramResponse {
    private Boolean ok;
    private List<TelegramUpdateResult> result;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public List<TelegramUpdateResult> getResult() {
        return result;
    }

    public void setResult(List<TelegramUpdateResult> result) {
        this.result = result;
    }

    public void cleanUpResiduals(){
        result = result
                .stream()
                .filter(updRes -> updRes.getMessage() != null)
                .collect(Collectors.toList());
    }
}
