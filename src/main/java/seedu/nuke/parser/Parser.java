package seedu.nuke.parser;


import seedu.nuke.command.ChangeDirectoryCommand;
import seedu.nuke.command.addCommand.AddTagCommand;
import seedu.nuke.command.deletecommand.DeleteCommand;
import seedu.nuke.command.listCommand.ListAllTasksDeadlineCommand;
import seedu.nuke.command.listCommand.ListCommand;
import seedu.nuke.command.listCommand.ListModuleTasksDeadlineCommand;
import seedu.nuke.command.Command;
import seedu.nuke.command.editCommand.EditDeadlineCommand;
import seedu.nuke.command.ExitCommand;
import seedu.nuke.command.HelpCommand;
import seedu.nuke.command.IncorrectCommand;
import seedu.nuke.command.addCommand.AddModuleCommand;
import seedu.nuke.command.addCommand.AddTaskCommand;
import seedu.nuke.command.deletecommand.DeleteModuleCommand;
import seedu.nuke.command.deleteCommand.DeleteTaskCommand;
import seedu.nuke.command.listCommand.ListModuleCommand;
import seedu.nuke.command.promptCommand.ConfirmationStatus;
import seedu.nuke.command.promptCommand.DeleteConfirmationPrompt;
import seedu.nuke.command.promptCommand.ListNumberPrompt;
import seedu.nuke.data.ModuleManager;
import seedu.nuke.directory.Module;
import seedu.nuke.directory.Root;
import seedu.nuke.directory.Task;
import seedu.nuke.format.DateTime;
import seedu.nuke.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static seedu.nuke.util.Message.MESSAGE_GO_INTO_MODULE;
import static seedu.nuke.util.Message.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.nuke.util.Message.MESSAGE_INVALID_PARAMETERS;
import static seedu.nuke.util.Message.MESSAGE_MISSING_PARAMETERS;


public class Parser {
    /**
     * Used for initial separation of command word and args.
     */
    public static final Pattern BASIC_COMMAND_FORMAT =
            Pattern.compile("(?<commandWord>\\S+)(?<parameters>.*)");
    public static final String WHITESPACES = "\\s+";
    public static final String PARAMETER_SPLITTER = " ";
    public static final int COMMAND_PARAMETER_MAXIMUM_LIMIT = 2;
    public static final int COMMAND_WORD_INDEX = 0;
    public static final int PARAMETER_WORD_INDEX = 1;

    public static final String MODULE_CODE_PREFIX = "-m";
    public static final String TASK_DESCRIPTION_PREFIX = "-t";
    public static final String PRIORITY_PREFIX = "-p";
    public static final String DEADLINE_PREFIX = "-d";
    public static final String ALL_FLAG = "-a";
    public static final String EXACT_FLAG = "-e";

    /**
     * Parses the input string read by the <b>UI</b> and converts the string into a specific <b>Command</b>, which is
     * to be executed by the <b>Nuke</b> program.
     * <p></p>
     * <b>Note</b>: The user input has to start with a certain keyword (i.e. <i>command word</i>), otherwise an
     * <i>Invalid Command Exception</i> will be thrown.
     *
     * @param input The user input read by the <b>UI.java</b>
     * @return The <b>corresponding</b> command to be executed
     * @see seedu.nuke.ui.Ui
     * @see Command
     */
    public Command parseCommand(String input) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(input.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(MESSAGE_INVALID_COMMAND_FORMAT + HelpCommand.MESSAGE_USAGE);
        }
        String commandWord = matcher.group("commandWord").toLowerCase();
        String parameters = matcher.group("parameters").trim();

        switch (commandWord) {

        //todo check if in module
        case AddTagCommand.COMMAND_WORD:
            return new AddTagCommand(parameters);

        case EditDeadlineCommand.COMMAND_WORD:
            return prepareEditDeadlineCommand(parameters);

//        case ChangeDirectoryCommand.COMMAND_WORD:
//            return prepareChangeDirectoryCommand(parameters, moduleManager);

        case AddModuleCommand.COMMAND_WORD:
            return prepareAddModuleCommand(parameters);

        case DeleteModuleCommand.COMMAND_WORD:
            return prepareDeleteModuleCommand(parameters);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ListCommand.COMMAND_WORD:
            return prepareListCommand(parameters);

        case ListAllTasksDeadlineCommand.COMMAND_WORD:
            return new ListAllTasksDeadlineCommand();

        case AddTaskCommand.COMMAND_WORD:
            return prepareAddTaskCommand(parameters);

//        case DeleteCommand.COMMAND_WORD:
//            return prepareDeleteCommand(parameters);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        default:
            return new IncorrectCommand(MESSAGE_INVALID_COMMAND_FORMAT + HelpCommand.MESSAGE_USAGE);
        }
    }

    private Command prepareListCommand(String parameters) {
        if (parameters.trim().matches(ALL_FLAG)){
            return new ListAllTasksDeadlineCommand();
        }
        if (Command.getCurrentDirectory() instanceof Root){
           return prepareListModuleCommand(parameters);
        } else if (Command.getCurrentDirectory() instanceof Module){
           return new ListModuleTasksDeadlineCommand();
        }
        return new ListAllTasksDeadlineCommand();
    }

    private Command prepareEditDeadlineCommand(String parameters) {
        Task taskToEdit;
        DateTime deadline;
        String[] temp = parameters.split("-d");
        Module dir = (Module) Command.getCurrentDirectory();
        String moduleCode = dir.getModuleCode();
        taskToEdit = new Task(ModuleManager.getModuleWithCode(moduleCode), temp[0].trim(), moduleCode);
        try {
            deadline = DateTimeFormat.stringToDateTime(temp[1].trim());
            return new EditDeadlineCommand(taskToEdit, deadline);
        } catch (DateTimeFormat.InvalidDateException | DateTimeFormat.InvalidTimeException
                | DateTimeFormat.InvalidDateTimeException e) {
            return new IncorrectCommand("Invalid datetime format!\n");
        }

    }

//    private Command prepareDeleteCommand(String parameters) {
//        if (Command.getCurrentDirectory() instanceof Root) {
//            if (ModuleManager.contains(parameters)){
//                return new DeleteModuleCommand(parameters);
//            } else return new IncorrectCommand(MESSAGE_INVALID_COMMAND_FORMAT);
//        } else if (Command.getCurrentDirectory() instanceof Module) {
//            if (((Module) Command.getCurrentDirectory()).getCategories().filter("").contains(parameters)) {
//                return new DeleteTaskCommand(parameters);
//            }
//        }
//        //should never reach
//        return null;
//    }

    private Command prepareChangeDirectoryCommand(String parameters, ModuleManager moduleManager) {
        if (parameters.equals("..")) {
            return new ChangeDirectoryCommand((ModuleManager.getRoot()));
        }
        if (moduleManager.getModuleWithCode(parameters) != null) {
            return new ChangeDirectoryCommand(moduleManager.getModuleWithCode(parameters));
        }
        return new IncorrectCommand(MESSAGE_INVALID_COMMAND_FORMAT);
    }

    private Command prepareAddTaskCommand(String parameters) {
        //todo
        //add a very simple task (for testing)
        Module module = (Module) Command.getCurrentDirectory();
        if (module != null) {
            String moduleCode = module.getModuleCode();
            return new AddTaskCommand(new Task(ModuleManager.getModuleWithCode(moduleCode), parameters, moduleCode));
        } else {
            return new IncorrectCommand(MESSAGE_GO_INTO_MODULE);
        }
    }

    /**
     * Splits user input into command word and rest of parameters (if any).
     *
     * @param input the input from the user
     * @return array of String contains command and parameter
     */
    private String[] getCommandAndParameter(String input) {
        String[] separatedInput = input.split(WHITESPACES, COMMAND_PARAMETER_MAXIMUM_LIMIT);
        String commandWord = separatedInput[COMMAND_WORD_INDEX].toLowerCase();
        String parameters = (separatedInput.length == COMMAND_PARAMETER_MAXIMUM_LIMIT)
                ? separatedInput[PARAMETER_WORD_INDEX].trim() : "";
        return new String[]{commandWord, parameters};
    }

    private Command prepareAddModuleCommand(String parameters) {
        final Pattern[] addModuleFormat = AddModuleCommand.REGEX_FORMATS;
        final int invalidParameterFormatsIndex = addModuleFormat.length - 1;
        Matcher[] matchers = new Matcher[addModuleFormat.length];

        if (isMissingCompulsoryParameters(addModuleFormat, matchers, parameters)) {
            return new IncorrectCommand(MESSAGE_MISSING_PARAMETERS);
        }

        if (matchers[invalidParameterFormatsIndex].find()) {
            return new IncorrectCommand(MESSAGE_INVALID_PARAMETERS);
        }

        String moduleCode = matchers[0].group("identifier").trim();

        return new AddModuleCommand(moduleCode);
    }

    private Command prepareDeleteModuleCommand(String parameters) {
        final Pattern[] deleteModuleFormat = DeleteModuleCommand.REGEX_FORMATS;
        final int invalidParameterFormatsIndex = deleteModuleFormat.length - 1;
        Matcher[] matchers = new Matcher[deleteModuleFormat.length];

        if (isMissingCompulsoryParameters(deleteModuleFormat, matchers, parameters)) {
            return new IncorrectCommand(MESSAGE_MISSING_PARAMETERS);
        }

        if (matchers[invalidParameterFormatsIndex].find()) {
            return new IncorrectCommand(MESSAGE_INVALID_PARAMETERS);
        }

        String moduleCode = matchers[0].group("identifier").trim();
        String exactFlag = matchers[1].group("exact").trim();
        boolean isExact = !exactFlag.isEmpty();

        return new DeleteModuleCommand(moduleCode, isExact);
    }

    /**
     *
     * @param parameters -a: list all tasks -CS1231 list tasks from specific module
     * @return command
     */
    private Command prepareListModuleCommand(String parameters) {
        final Pattern[] listModuleFormat = ListModuleCommand.REGEX_FORMATS;
        final int invalidParameterFormatsIndex = listModuleFormat.length - 1;
        Matcher[] matchers = new Matcher[listModuleFormat.length];

        if (isMissingCompulsoryParameters(listModuleFormat, matchers, parameters)) {
            return new IncorrectCommand(MESSAGE_MISSING_PARAMETERS);
        }

        if (matchers[invalidParameterFormatsIndex].find()) {
            return new IncorrectCommand(MESSAGE_INVALID_PARAMETERS);
        }

        /* To add later after updating my code - iceclementi
        String moduleKeyword = matchers[0].group("identifier").trim();
        String allFlag = matchers[1].group("all").trim();
        String exactFlag = matchers[2].group("exact").trim();
        boolean isExact = !exactFlag.isEmpty();
        */

        return new ListModuleCommand();
    }

    /**
     * Checks if there is more than <b>one</b> parameter in the input.
     *
     * @param parameters The parameter(s) provided in the input
     * @return <code>TRUE</code> if there is more than one parameter in the input, and <code>FALSE</code> otherwise
     */
    private boolean hasMultipleParameters(String parameters) {
        return parameters.contains(PARAMETER_SPLITTER);
    }

    private boolean isMissingCompulsoryParameters(Pattern[] formats, Matcher[] matchers, String parameters) {
        // Match patterns
        for (int i = 0; i < formats.length; ++i) {
            matchers[i] = formats[i].matcher(parameters);
        }

        // Check if matches with each pattern except last pattern which checks for invalid parameters
        for (int i = 0; i < formats.length - 1; ++i) {
            if (!matchers[i].find()) {
                return true;
            }
        }
        return false;
    }

    public Command parseInputAsConfirmation(String userInput) {
        switch (userInput) {
            case "yes":
            case "y":
                return new DeleteConfirmationPrompt(ConfirmationStatus.CONFIRM);

            case "no":
            case "n":
                return new DeleteConfirmationPrompt(ConfirmationStatus.ABORT);

            default:
                return new DeleteConfirmationPrompt(ConfirmationStatus.WAIT);
        }
    }

    public Command parseInputAsIndices(String input) {
        final Matcher matcher = ListNumberPrompt.INDICES_FORMAT.matcher(input.trim());

        if (!matcher.matches()) {
            return new ListNumberPrompt(null);
        }

        String indicesString = matcher.group("indices");
        String[] separatedIndicesString = indicesString.split(WHITESPACES);

        // Convert String array to Integer ArrayList and removing duplicates
        ArrayList<Integer> indices = Stream.of(separatedIndicesString)
                .map(Integer::parseInt).distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        // Decrement each index by 1 due to 0-based indexing
        for (int i = 0; i < indices.size(); i++) {
            indices.set(i, indices.get(i)-1);
        }

        return new ListNumberPrompt(indices);
    }
}
