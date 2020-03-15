package seedu.duke;

/**
 * Represent a class of user command.
 */
public class Command {

    private String commandType;
    private String description;

    public boolean isExit() {
        return this.commandType.equals("exit");
    }

    /**
     * Execute the command.
     * @param personList all operations should work on this list;
     */
    public void execute(PersonList personList) {

    }
}
