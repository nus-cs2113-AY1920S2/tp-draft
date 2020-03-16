package seedu.duke.commands;

import seedu.duke.Profile;
import seedu.duke.exceptions.InvalidFormatException;
import seedu.duke.parser.Parser;
import seedu.duke.ui.UI;

public class SetAgeCommand extends Command {

    private static final int ARGUMENTS_REQUIRED = 1;
    private int age;

    /**
     * Constructs the Command object.
     *
     * @param command the command prompt entered by the user.
     */

    public SetAgeCommand(String command, String description) throws InvalidFormatException, NumberFormatException {
        super(command);
        String[] descriptionArray = Parser.parseDescription(description, ARGUMENTS_REQUIRED);
        this.age = Integer.parseInt(descriptionArray[0]);
    }

    @Override
    public void execute(Profile profile, UI ui) {
        profile.setAge(this.age);
        System.out.println(String.format("Your username has been changed to %d", profile.getAge()));
    }
}
