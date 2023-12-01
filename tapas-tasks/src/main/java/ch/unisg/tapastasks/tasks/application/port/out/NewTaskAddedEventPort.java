package ch.unisg.tapastasks.tasks.application.port.out;

public interface NewTaskAddedEventPort {

    void publishNewTaskAddedEvent(NewTaskAddedEvent event);

}
