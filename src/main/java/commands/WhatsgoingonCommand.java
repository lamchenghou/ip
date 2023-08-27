package commands;

import errors.DotException;
import tasks.TaskList;

import java.time.LocalDateTime;

public class WhatsgoingonCommand extends Command {

    private final LocalDateTime parsedLocalDateTime;
    private final TaskList dotTaskList;
    public WhatsgoingonCommand(LocalDateTime parsedLocalDateTime,
                               TaskList dotTaskList) {
        this.parsedLocalDateTime = parsedLocalDateTime;
        this.dotTaskList = dotTaskList;
    }
    public void execute() throws DotException {
        dotTaskList.listAllTasksFallingOnDate(parsedLocalDateTime);
    }
}