package ch.unisg.tapasroster.roster.application.port.in;

import ch.unisg.tapasroster.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class RetrieveTaskAssignmentsQuery extends SelfValidating<RetrieveTaskAssignmentsQuery> {

    public RetrieveTaskAssignmentsQuery() {
        this.validateSelf();
    }
}
