package tasks;

import errors.DotException;
import storage.Storage;
import ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * TaskList class that contains an ArrayList of tasks, where
 * the Dot can tell it to perform actions on the tasks, and
 * perform storage actions, enabled by the storage package.
 * <p>
 * Note that the errors package is not used for TaskList as
 * this class can be repurposed.
 */
public class TaskList {

    private final ArrayList<Task> tasks;
    private final int maxSize;
    private final Storage storage;

    /**
     * Protected constructor for an empty TaskList, used by
     * the newTaskList factory method
     * @param maxSize of TaskList
     * @param storage object for file read/write
     */
    protected TaskList(int maxSize, Storage storage) {
        this.tasks = new ArrayList<>();
        this.maxSize = maxSize;
        this.storage = storage;
    }

    protected TaskList(int maxSize, ArrayList<Task> tasks, Storage storage) {
        this.tasks = tasks;
        this.maxSize = maxSize;
        this.storage = storage;
    }

    public void addTask(Task newTask) {
        if (this.tasks.size() < this.maxSize) {
            this.tasks.add(newTask);
            Ui.wrapPrintWithHorizontalRules(String.format("Got it. I've added this task:\n"
                    + "  %s\nNow you have %d tasks in the list.", newTask, this.tasks.size()));
        } else {
            Ui.wrapPrintWithHorizontalRules(String.format("Your task list has reached the limit of %d tasks. "
                    + "Please remove some tasks to proceed.", this.maxSize));
        }
    }

    public void list() {
        ArrayList<String> linesToBePrinted = new ArrayList<>();
        for (int i = 0; i < this.tasks.size(); i++) {
            Task currTask = this.tasks.get(i);
            linesToBePrinted.add(String.format("%d.%s", i + 1, currTask));
        }
        Ui.displayArrayList(linesToBePrinted);
    }

    /**
     * Factory method allows for future flexibility.
     * For instance, if they are multiple empty TaskLists,
     * we are able to use a singleton.
     *
     * @return new TaskList
     */
    public static TaskList newTaskList(int maxSize, Storage storage) {
        return new TaskList(maxSize, storage);
    }

    public static TaskList taskListFromArrayList(int maxSize, ArrayList<Task> taskList, Storage storage) {
        return new TaskList(maxSize, taskList, storage);
    }

    public void toggleTaskStatus(int position, boolean isCompleted) {
        if (position >= 0 && position < this.tasks.size()) {
            this.tasks.get(position).toggleStatus(isCompleted);
        } else {
            Ui.wrapPrintWithHorizontalRules("Invalid position.");
        }
    }

    public void deleteTask(int position) {
        if (position >= 0 && position < this.tasks.size()) {
            Task removedTask = this.tasks.remove(position);
            Ui.wrapPrintWithHorizontalRules(String.format("Task \"%s\" removed successfully!", removedTask));
        } else {
            Ui.wrapPrintWithHorizontalRules("Invalid position.");
        }
    }

    public ArrayList<String> getDisplayForTasksFallingOnDate(LocalDateTime dateTime) {
        // Deadline must be within the day
        // Event can either start or end on the date itself, or both

        // Note that dateTime is at the start of day due to parsing standardisation
        // Create a copy of dateTime to represent the endOfDay
        LocalDateTime endOfDay = LocalDateTime.from(dateTime).withHour(23).withMinute(59).withSecond(59);
        ArrayList<String> outputList = new ArrayList<>();
        outputList.add("Finding the dots... to illuminate a constellation of "
                + "tasks happening today!");
        boolean hasTaskToday = false;
        for (Task currTask : this.tasks) {
            if (currTask.isOnDate(dateTime, endOfDay)) {
                outputList.add(currTask.toString());
                hasTaskToday = true;
            }
        }
        if (!hasTaskToday) {
            outputList.add("Like a tiny dot in the sky, you're schedule is empty! ^o^");
        }
        return outputList;
    }

    public void saveTaskListToStorage() throws DotException {
        this.storage.saveTasks(this.tasks);
    }
}