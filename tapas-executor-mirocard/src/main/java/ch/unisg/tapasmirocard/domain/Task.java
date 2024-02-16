package ch.unisg.tapasmirocard.domain;

import lombok.Getter;
import lombok.Setter;

public class Task {

    public static class Status {
        public static String RUNNING = "RUNNING";
        public static String EXECUTED = "EXECUTED";
    }

    @Getter
    @Setter
    private String taskLocation;

    @Getter
    @Setter
    private String input;

    @Getter
    @Setter
    private String output;

    @Getter
    @Setter
    private String originalTaskUri;

}
