package ch.unisg.tapascherrybot.domain;

import lombok.Data;

@Data
public class Executor {
    private String executorType;
    private String executorName;
    private String executorBaseUri;
}
