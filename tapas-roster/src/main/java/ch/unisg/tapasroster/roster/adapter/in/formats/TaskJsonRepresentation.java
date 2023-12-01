package ch.unisg.tapasroster.roster.adapter.in.formats;

import lombok.Getter;

public final class TaskJsonRepresentation {

    public static final String MEDIA_TYPE = "application/roster+task+json";

    @Getter
    private String taskLocation;

    @Getter
    private final String taskListName;

    @Getter
    private final String taskType;

    public TaskJsonRepresentation(String taskLocation, String taskListName, String taskType) {
        this.taskLocation = taskLocation;
        this.taskListName = taskListName;
        this.taskType = taskType;
    }

}
