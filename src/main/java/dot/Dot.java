package dot;

import dot.commands.Command;
import dot.errors.DotException;
import dot.parser.Parser;
import dot.storage.Storage;
import dot.tasks.TaskList;
import dot.ui.Ui;

/**
 * The Dot class, which integrates the high-level components together,
 * to form a robust, helpful and cheerful to-do list chatbot.
 */
public class Dot {

    private TaskList dotTaskList;

    private final Ui userInterface;

    private final Storage storage;

    /**
     * Constructor for Dot.
     *
     * @param maxSize The maximum number of tasks Dot can handle
     */
    public Dot(int maxSize) {
        String storageLocation = "./data/dot.txt";
        this.storage = new Storage(storageLocation);
        try {
            this.dotTaskList = TaskList.getTaskListFromArrayList(maxSize,
                    this.storage.getTasks(), this.storage);
        } catch (DotException e) {
            e.handleError();
            this.dotTaskList = TaskList.getNewTaskList(100, this.storage);
        }
        this.userInterface = new Ui();
    }

    // Inspired by tutorial sheet
    // The organisation was adapted, thus a similar looking run()
    // However, the deeper implementation were not adapted

    /**
     * This method starts the execution of Dot functionalities.
     */
    public void run() {
        Ui.displayWelcome();

        while (true) {
            try {
                String input = userInterface.readNextLine();
                Command cmd = Parser.parseInputToCommand(input, dotTaskList);
                if (cmd.isTerminateCommand()) {
                    break;
                }
                cmd.execute();
            } catch (DotException e) {
                // For checked exception
                e.handleError();
            }
        }
        Ui.displayGoodbye();
    }

    public static void main(String[] args) {
        Dot dotInstance;
        dotInstance = new Dot(100);
        dotInstance.run();
    }

}