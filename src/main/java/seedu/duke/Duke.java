package seedu.duke;

import java.util.Scanner;

import static seedu.duke.AttendanceList.executeCommand;

public class Duke {
    private static final String FILE_PATH = "Duke.txt";

    public static void main(String[] args) {
        new Duke();
    }

    private Duke() {
        Ui ui = new Ui();
        Storage storage = new Storage(FILE_PATH, ui);
        while (true) {
            String userInput = ui.readCommand();
            try {
                Command command = executeCommand(userInput);
                command.execute(ui, storage);
                if (command instanceof ExitCommand) {
                    break;
                }
            } catch (DukeException e) {
                Ui.displayError(e.getMessage());
            }
        }
    }
}
