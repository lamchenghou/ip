package errors;


import ui.Ui;

/**
 *  This contains the main logic of error handling.
 *  Each enum value has an associated method and
 *  error message. The method `printErrorMessage`
 *  takes in an Exception as an argument, and
 *  prints the exception message as a sub-error
 *  message. As such we are able to combine Dot's
 *  own error messages, and Java's exception messages,
 *  in the case of a built-in runtime error.
 */
public enum TaskErrors {
    ERR_USING_MARK ("..o.o..beep..Invalid use of mark, use: \"mark <task number>\""),
    ERR_USING_UNMARK ("..o.o..beep..Invalid use of unmark, use: \"unmark <task number>\""),
    ERR_USING_TODO ("..o.o..beep..Invalid use of todo, use: \"todo <description>\""),
    ERR_USING_DEADLINE ("..o.o..beep..Invalid use of deadline, use: \"deadline <description> " +
            "/by <deadline_desc>\""),
    ERR_USING_EVENT ("..o.o..beep..Invalid use of event, use: \"event <description>" +
            " /from <start_desc> /to <end_desc>\""),
    ERR_DELETING_EVENT ("..o.o..beep..Invalid use of delete, use: \"delete <task number>\""),
    ERR_READING_COMMAND ("..o.o..beep..Command not found..beep..");


    private final String errorMessage;
    TaskErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void printErrorMessage(Exception e) {
        if (e instanceof NumberFormatException) {
            Ui.wrapPrintWithHorizontalRules(this.errorMessage +
                    "\nConnecting the dots: An index number was not specified.");
        } else {
            Ui.wrapPrintWithHorizontalRules(this.errorMessage +
                    String.format("\nConnecting the dots: %s", e));
        }

    }
}
