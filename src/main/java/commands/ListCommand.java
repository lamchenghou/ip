package commands;

import errors.DotException;
import tasks.TaskList;

/**
 * Command to list out all the Tasks, with completed status and
 * all relevant information such as deadline for Deadline.
 */
public class ListCommand extends Command {
    private final TaskList dotTaskList;

    /**
     * Constructor for ListCommand.
     * @param dotTaskList This is the TaskList which encapsulates the Task and operations
     */
    public ListCommand(TaskList dotTaskList) {
        this.dotTaskList = dotTaskList;
    }

    /**
     * Lists out all the tasks in dotTaskList.
     * @throws DotException On detected error
     */
    @Override
    public void execute() throws DotException {
        this.dotTaskList.list();
    };
}
