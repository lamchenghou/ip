package parser;

import commands.*;
import errors.DotException;
import errors.TaskError;
import tasks.TaskList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    /**
     * Parses a dateInput in the format dd/MM/yyyy into a LocalDateTime
     * We will standardise hhmmss as 000000. It is important to run
     * a Validation function on the input before using a Parser fn,
     *
     * @param dateInput in the format dd/MM/yyyy into a LocalDateTime
     * @return corresponding LocalDateTime object
     */
    public static LocalDateTime parseDateInputIntoDateTime(String dateInput) {
        String[] dayMonthYear = dateInput.split("/");
        return LocalDateTime.parse(String.format("%s-%02d-%02dT00:00:00", dayMonthYear[2],
                Integer.valueOf(dayMonthYear[1]), Integer.valueOf(dayMonthYear[0])));
    }

    /**
     * Parses from MMM dd yyyy HHmm to yyyy-MM-dd HHmm
     * Reference: <a href="https://docs.oracle.com/en/java/javase/11/
     * docs/api/java.base/java/time/format/DateTimeFormatter.html">...</a>
     *
     * @param displayDateTime of format MMM dd yyyy ha
     * @return String in format yyyy-MM-dd HHmm i.e. user input
     */
    public static String parseDisplayDatetimeToStorageDatetime(String displayDateTime) {
        try {
            DateTimeFormatter displayDateTimeFormat = DateTimeFormatter.ofPattern("MMM dd yyyy ha");
            LocalDateTime displayLocalDateTime = LocalDateTime.parse(displayDateTime, displayDateTimeFormat);
            DateTimeFormatter storageDateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
            return displayLocalDateTime.format(storageDateTimeFormat);
        } catch (DateTimeParseException e) {
            // This shouldn't happen, but will default to TimeDescription
            return displayDateTime;
        }

    }

    public static Command parseInputToCommand(String input,
                                              TaskList dotTaskList) throws DotException {
        if (input.strip().equals("bye")) {
            return new ByeCommand();
        } else if (input.strip().equals("list") || input.strip().equals("ls")) {
            return new ListCommand(dotTaskList);
        } else if (Validation.isValidCommand(input, "mark")) {
            int position = Validation.intIfValidCommandSpaceNumber(input, TaskError.ERR_USING_MARK);
            return new MarkCommand(position, dotTaskList);
        } else if (Validation.isValidCommand(input, "unmark")) {
            int position = Validation.intIfValidCommandSpaceNumber(input, TaskError.ERR_USING_UNMARK);
            return new UnmarkCommand(position, dotTaskList);
        } else if (Validation.isValidCommand(input, "todo")) {
            String restOfString = Validation.descIfValidCommandSpaceDesc(input, "todo",
                    "task description", TaskError.ERR_USING_TODO);
            return new TodoCommand(restOfString, dotTaskList);
        } else if (Validation.isValidCommand(input, "deadline")) {
            String[] args = Validation.argsIfValidDeadlineFormat(input);
            return new DeadlineCommand(args[0], args[1], dotTaskList);
        } else if (Validation.isValidCommand(input, "event")) {
            String[] args = Validation.argsIfValidEventFormat(input);
            return new EventCommand(args[0], args[1], args[2], dotTaskList);
        } else if (Validation.isValidCommand(input, "delete")) {
            int position = Validation.intIfValidCommandSpaceNumber(input, TaskError.ERR_DELETING_TASK);
            return new DeleteCommand(position, dotTaskList);
        } else if (Validation.isValidCommand(input, "whatsgoingon")) {
            String restOfString = Validation.descIfValidCommandSpaceDesc(input,
                    "whatsgoingon", "date", TaskError.ERR_USING_WHATSGOINGON);
            if (!(Validation.isValidDate(restOfString))) {
                throw new DotException("Incorrect format for data, use dd/MM/yyyy",
                        TaskError.ERR_USING_WHATSGOINGON);
            }
            LocalDateTime parsedLocalDateTime = Parser.parseDateInputIntoDateTime(restOfString);
            return new WhatsgoingonCommand(parsedLocalDateTime, dotTaskList);
        } else if (input.equals("help")) {
            return new HelpCommand();
        }
        throw new DotException("Unknown command.", TaskError.ERR_READING_COMMAND);
    }
}