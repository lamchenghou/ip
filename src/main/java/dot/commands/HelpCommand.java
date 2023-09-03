package dot.commands;

import dot.errors.DotException;
import dot.ui.Ui;

/**
 * Command to display the help message.
 */
public class HelpCommand extends Command {

    /**
     * Constructor of HelperCommand, which does nothing.
     */
    public HelpCommand() {
    }

    /**
     * Displays the help message.
     *
     * @throws DotException On detected error.
     */
    @Override
    public void execute() throws DotException {
        Ui.displayHelpMessage();
    }

}