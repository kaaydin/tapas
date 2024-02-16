package ch.unisg.tapasmasonsmanifesto.domain;

import lombok.Data;

@Data
public class Executor {
    private String executorType;
    private String executorName;
    private String executorBaseUri;
}
