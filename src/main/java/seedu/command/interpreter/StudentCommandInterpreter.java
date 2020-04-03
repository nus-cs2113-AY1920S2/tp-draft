package seedu.command.interpreter;

import seedu.command.Command;
import seedu.command.student.SortStudentListByList;
import seedu.command.student.SortStudentListByName;
import seedu.command.student.ClearStudentList;
import seedu.command.student.FindStudentList;
import seedu.command.student.AddStudentList;
import seedu.command.student.DeleteStudentList;
import seedu.command.student.ViewStudentList;
import seedu.event.EventList;
import seedu.exception.PacException;
import seedu.ui.UI;

public class StudentCommandInterpreter extends CommandInterpreter {

    protected UI ui;

    public StudentCommandInterpreter(EventList eventList) {
        super(eventList);
        this.ui = new UI();
    }

    /**
     * Method to decide the type of command to execute.
     * @param commandDescription the following parameter used.
     *                           Currently only used for delete command.
     * @return The student related command that the user calls.
     * @throws PacException If an invalid command Description is provided.
     */
    public Command decideCommand(String commandDescription) throws PacException {

        String commandType = getFirstWord(commandDescription);
        switch (commandType) {
        case "add":
            try {
                return new AddStudentList();
            } catch (Exception e) {
                throw new PacException("Student List Command Add failed.");
            }

        case "view":
            try {
                return new ViewStudentList();
            } catch (Exception e) {
                throw new PacException("Student List Command View failed.");
            }
        case "delete":
            try {
                return new DeleteStudentList();
            } catch (Exception e) {
                throw new PacException("Student List Command Delete failed.");
            }
        case "sort":
            try {
                UI.display("Please Key in either 'name' or 'list'.");
                ui.readUserInput();
                String sortType = ui.getUserInput();
                switch (sortType) {
                case "name":
                    try {
                        return new SortStudentListByName();
                    } catch (Exception e) {
                        throw new PacException("Student List Command Sort By Name failed.");
                    }
                case "list":
                    try {
                        return new SortStudentListByList();
                    } catch (Exception e) {
                        throw new PacException("Student List Command Sort By List failed.");
                    }
                default:
                    throw new PacException("Unknown Student List Sort Command");
                }
            } catch (Exception e) {
                throw new PacException("Student List Command Sort failed.");
            }
        case "find":
            try {
                return new FindStudentList();
            } catch (Exception e) {
                throw new PacException("Student List Command Find failed.");
            }
        case "clear":
            try {
                return new ClearStudentList();
            } catch (Exception e) {
                throw new PacException("Student List Command Clear failed.");
            }
        default:
            throw new PacException("Unknown Student List Command.");
        }
    }
}
