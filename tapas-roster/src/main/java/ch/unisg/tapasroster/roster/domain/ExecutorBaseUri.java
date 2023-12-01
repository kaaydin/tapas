package ch.unisg.tapasroster.roster.domain;

import lombok.Value;

@Value
public class ExecutorBaseUri {

    String baseUri;

    public ExecutorBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public ExecutorBaseUri() {
        baseUri = null;
    }
}