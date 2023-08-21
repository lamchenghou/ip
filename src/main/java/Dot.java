import errors.DotException;
import errors.TaskError;
import tasks.*;
import ui.Ui;

import java.util.Scanner;

public class Dot {
    private static final TaskList dotTaskList = TaskList.newTaskList(100);

    public static void main(String[] args) {
        Ui.welcome();
        Scanner sc = new Scanner(System.in);

        boolean isOngoing = true;
        while (isOngoing) {
            try {
                String input = sc.nextLine();
                switch (input) {
                    case "bye":
                        isOngoing = false;
                        break;
                    case "list":
                        dotTaskList.list();
                        break;
                    default:
                        // TODO: abstract out validation
                        // TODO: Refactor the below validation using String::match & regex
                        // TODO: generalise the validation function for "<cmd> <args>" by using Consumer
                        /* TODO: In a later increment, there will be a requirement to abstract out the Parser
                                 However, it is unclear whether we should keep constructing Tasks in TaskList.
                                 We allow for Dot to access the 4 classes that are <: Task
                         */
                        // Note: To support more than 3 kinds of tasks, we can code a robust function and follow
                        // a standardised format.
                        if (input.startsWith("mark") && (input.length() == 4 || input.charAt(4) == ' ')) {
                            String[] substrings = input.split(" ");
                            if (substrings.length == 2) {
                                int position = Integer.parseInt(substrings[1]);
                                dotTaskList.markTask(position - 1, true);
                            } else if (substrings.length == 1) {
                                throw new DotException("No task number stated", TaskError.ERR_USING_MARK);
                            } else {
                                throw new DotException("Too many parameters", TaskError.ERR_USING_MARK);
                            }
                            break;
                        } else if (input.startsWith("unmark") && (input.length() == 6 || input.charAt(6) == ' ')) {
                            String[] substrings = input.split(" ");
                            if (substrings.length == 2) {
                                int position = Integer.parseInt(substrings[1]);
                                dotTaskList.markTask(position - 1, false);
                            } else if (substrings.length == 1) {
                                throw new DotException("No task number stated", TaskError.ERR_USING_UNMARK);
                            } else {
                                throw new DotException("Too many parameters", TaskError.ERR_USING_UNMARK);
                            }
                            break;
                        } else if (input.startsWith("todo") && (input.length() == 4 || input.charAt(4) == ' ')) {
                            if (input.strip().length() <= 5) {
                                throw new DotException("No task description given", TaskError.ERR_USING_TODO);
                            }
                            String restOfString = input.substring(5);
                            Task newTodoTask = new Todo(restOfString);
                            dotTaskList.addTask(newTodoTask);
                            break;
                        } else if (input.startsWith("deadline") && (input.length() == 8 || input.charAt(8) == ' ')) {
                            if (input.length() <= 9) {
                                throw new DotException("No task description given", TaskError.ERR_USING_DEADLINE);
                            }
                            // We can assume that input is of format "deadline .+"
                            // Case: "deadline /by"
                            int indexOfSlash = input.indexOf(" /by");
                            if (indexOfSlash == -1 || indexOfSlash == 8) {
                                throw new DotException("No deadline given or is given without task description.",
                                        TaskError.ERR_USING_DEADLINE);
                            }

                            // We can assume that input is now in the format "deadline .+ /by.*'
                            String[] substrings = input.split(" /by");
                            // RHS of || explanation: in the case /by is at the end of str,
                            // then "" will not be an element of the resulting array
                            if (substrings.length == 1) {
                                throw new DotException("No deadline description given.", TaskError.ERR_USING_DEADLINE);
                            } else if (substrings.length != 2) {
                                // A side effect of this is that "deadline <desc> /by today /by"
                                // will pass the check, and in a way, autocorrect
                                throw new DotException("Too many instances of deadline descriptions.",
                                        TaskError.ERR_USING_DEADLINE);
                            }
                            // Since supposedly filled spaces can appear as whitespace,
                            // we need to run a check after the split
                            // We will truncate "deadline" from the first element and strip it
                            String description = substrings[0].substring(8).strip();
                            if (description.isEmpty()) {
                                throw new DotException("No task description given", TaskError.ERR_USING_DEADLINE);
                            }
                            // We will strip the second element, to see if deadline desc is given
                            String deadline = substrings[1].strip();
                            if (deadline.isEmpty()) {
                                throw new DotException("No deadline description given", TaskError.ERR_USING_DEADLINE);
                            }
                            Task newDeadlineTask = new Deadline(description, deadline);
                            dotTaskList.addTask(newDeadlineTask);
                            break;
                        } else if (input.startsWith("event")) {
                            // .+ enforces at least one character, but disallows empty string
                            // Regex below inspired by
                            // https://stackoverflow.com/questions/2912894/how-to-match-any-character-in-regular-expression
                            String eventRegex = "event .+ /from .+ /to .+";
                            if (!input.matches(eventRegex)) {
                                throw new DotException("Wrong format for event.", TaskError.ERR_USING_EVENT);
                            }
                            int indexOfFirstSlash = input.indexOf("/");
                            int indexOfFSecondSlash = input.indexOf("/", indexOfFirstSlash + 1);
                            String description = input.substring(6, indexOfFirstSlash - 1);
                            String start = input.substring(indexOfFirstSlash + 5, indexOfFSecondSlash).strip();
                            String end = input.substring(indexOfFSecondSlash + 4);
                            Task newEventTask = new Event(description, start, end);
                            dotTaskList.addTask(newEventTask);
                            break;
                        } else if (input.startsWith("delete") && (input.length() == 6 || input.charAt(6) == ' ')) {
                            String[] substrings = input.split(" ");
                            if (substrings.length == 2) {
                                int position = Integer.parseInt(substrings[1]);
                                dotTaskList.deleteTask(position - 1);
                            } else if (substrings.length == 1) {
                                throw new DotException("No task number stated", TaskError.ERR_USING_MARK);
                            } else {
                                throw new DotException("Too many parameters", TaskError.ERR_USING_MARK);
                            }
                            break;
                        }
                        throw new DotException("Unknown command.", TaskError.ERR_READING_COMMAND);
                }
            } catch (DotException e) {
                // For checked exception
                e.handleError();
            } catch (NumberFormatException e) {
                // For runtime exceptions
                TaskError.ERR_USING_MARK.printErrorMessage(e);
            }
        }
        Ui.goodbye();
    }
}
