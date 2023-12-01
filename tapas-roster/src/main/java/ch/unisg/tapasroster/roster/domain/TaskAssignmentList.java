package ch.unisg.tapasroster.roster.domain;

import lombok.Getter;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;

/**This is our aggregate root**/
public class TaskAssignmentList {

    @Getter
    private final TaskAssignmentListName taskAssignmentListName;

    @Getter
    private final ListOfTaskAssignments listOfTaskAssignments;

    //Note: We do not care about the management of task lists, there is only one within this service
    //--> using the Singleton pattern here to make lives easy; we will later load it from a repo
    private static final TaskAssignmentList assignmentList = new TaskAssignmentList(new TaskAssignmentListName("tapas-assignments-group5"));

    private TaskAssignmentList(TaskAssignmentListName taskAssignmentListName) {
        this.taskAssignmentListName = taskAssignmentListName;
        this.listOfTaskAssignments = new ListOfTaskAssignments(new LinkedList<>());
    }

    public static TaskAssignmentList getTaskAssignmentList() {return assignmentList;}

    public static List<TaskAssignment> retrieveAllAssignments() {
        List<TaskAssignment> listOfAssignments = assignmentList.getListOfTaskAssignments().value;
        return listOfAssignments;
    }


    public void addNewAssignment(TaskAssignment newAssignment) {
        listOfTaskAssignments.value.add(newAssignment);
        //This is a simple debug message to see that the task list is growing with each new request
        System.out.println("Number of assignments: " + listOfTaskAssignments.value.size());
    }

    @Value
    public static class TaskAssignmentListName {
        private String value;
    }

    @Value
    public static class ListOfTaskAssignments {
        private List<TaskAssignment> value;
    }
}

