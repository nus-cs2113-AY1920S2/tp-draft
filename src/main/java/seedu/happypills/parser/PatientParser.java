package seedu.happypills.parser;

import seedu.happypills.commands.patientcommands.AddPatientCommand;
import seedu.happypills.commands.patientcommands.DeletePatientCommand;
import seedu.happypills.commands.patientcommands.EditPatientCommand;
import seedu.happypills.commands.patientcommands.PatientCommand;
import seedu.happypills.commands.patientcommands.IncorrectPatientCommand;
import seedu.happypills.commands.patientcommands.ListPatientCommand;
import seedu.happypills.commands.patientcommands.GetPatientCommand;
import seedu.happypills.exception.HappyPillsException;
import seedu.happypills.ui.TextUi;

import java.util.Scanner;

/**
 * Parses user input.
 */
public class PatientParser {
    /**
     * Parses user input into command for execution.
     *
     * @param fullCommand Full user input string
     * @return the command Based on the user input
     * @throws HappyPillsException Errors base on invalid input or insufficient input
     */
    public static PatientCommand parse(String fullCommand) throws HappyPillsException {
        String[] userCommand = fullCommand.split(" ", 3);

        if (userCommand[0].equalsIgnoreCase("list")) {
            return new ListPatientCommand();
        } else if (userCommand[0].equalsIgnoreCase("add")) {
            if (userCommand.length == 1 || userCommand[1].trim().isEmpty()) {
                throw new HappyPillsException("    Patient's detail is empty.");
            }
            return parseAddCommand(userCommand[2]);
        } else if (userCommand[0].equalsIgnoreCase("get")) {
            if (userCommand.length == 1 || userCommand[1].trim().isEmpty()) {
                throw new HappyPillsException("    NRIC of the patient not provided");
            }
            return new GetPatientCommand(userCommand[2].trim().toUpperCase());
        } else if (userCommand[0].equalsIgnoreCase("edit")) {
            String[] edit = fullCommand.split(" ", 4);
            if (edit.length < 3) {
                throw new HappyPillsException("    Please input your patient's details correctly.");
            }
            return new EditPatientCommand(edit[2].substring(3), edit[3]);
        } else if (userCommand[0].equalsIgnoreCase("delete")) {
            return new DeletePatientCommand(userCommand[2]);
        } else {
            throw new HappyPillsException("    Invalid Command.");
        }
    }

    private static PatientCommand parseAddCommand(String content) throws HappyPillsException {
        String[] details;
        if (content.startsWith("/")) {
            details = content.substring(1).split(" /");
        } else {
            content = "@" + content;
            details = content.split(" /");
        }
        String[] parseInput = {"", "", "", "", "", "NIL", "NIL"};
        for (String detail : details) {
            if (detail.startsWith("n") && parseInput[0].equalsIgnoreCase("")) {
                parseInput[0] = detail.substring(1).trim();
            } else if (detail.startsWith("ic") && parseInput[1].equalsIgnoreCase("")) {
                parseInput[1] = detail.substring(2).trim();
            } else if (detail.startsWith("p") && parseInput[2].equalsIgnoreCase("")) {
                parseInput[2] = detail.substring(1).trim();
            } else if (detail.startsWith("d") && parseInput[3].equalsIgnoreCase("")) {
                parseInput[3] = detail.substring(1).trim();
            } else if (detail.startsWith("b") && parseInput[4].equalsIgnoreCase("")) {
                parseInput[4] = detail.substring(1).trim();
            } else if (detail.startsWith("a") && parseInput[5].equalsIgnoreCase("NIL")) {
                parseInput[5] = detail.substring(1).trim();
            } else if (detail.startsWith("r") && parseInput[6].equalsIgnoreCase("NIL")) {
                parseInput[6] = detail.substring(1).trim();
            } else {
                System.out.println("    " + detail + " is not a valid input.\n"
                        + "    " + detail + " will not be added\n" + TextUi.DIVIDER);
            }
        }

        while (parseInput[0].equalsIgnoreCase("") || parseInput[1].equalsIgnoreCase("")
                || parseInput[2].equalsIgnoreCase("") || parseInput[3].equalsIgnoreCase("")
                || !isInteger(parseInput[2].trim()) || parseInput[4].equalsIgnoreCase("")) {
            System.out.println("    Please input your missing detail listed below");
            if (parseInput[0].equalsIgnoreCase("")) {
                System.out.println("    /n[NAME]");
            }
            if (parseInput[1].equalsIgnoreCase("")) {
                System.out.println("    /ic[NRIC]");
            }
            if (parseInput[2].equalsIgnoreCase("") || !isInteger(parseInput[2].trim())) {
                System.out.println("    /p[PHONE] only number");
            }
            if (parseInput[3].equalsIgnoreCase("")) {
                System.out.println("    /d[DOB]");
            }
            if (parseInput[4].equalsIgnoreCase("")) {
                System.out.println("    /b[BLOOD TYPE]");
            }
            String input = promptUser().trim();
            if (input.equalsIgnoreCase("clear")) {
                return new IncorrectPatientCommand(TextUi.DIVIDER + "\n    Command has been aborted.\n"
                        + TextUi.DIVIDER);
            }
            String[] updates;
            if (input.startsWith("/")) {
                updates = input.substring(1).split(" /");
            } else {
                input = "@" + input;
                updates = input.split(" /");
            }
            for (String update : updates) {
                if (update.trim().startsWith("n") && parseInput[0].equalsIgnoreCase("")) {
                    parseInput[0] = update.trim().substring(1);
                } else if (update.trim().startsWith("ic") && parseInput[1].equalsIgnoreCase("")) {
                    parseInput[1] = update.trim().substring(2);
                } else if (update.trim().startsWith("p") && (parseInput[2].equalsIgnoreCase("")
                        || !isInteger(parseInput[2].trim()))) {
                    parseInput[2] = update.trim().substring(1);
                } else if (update.trim().startsWith("d") && parseInput[3].equalsIgnoreCase("")) {
                    parseInput[3] = update.trim().substring(1);
                } else if (update.trim().startsWith("b") && parseInput[4].equalsIgnoreCase("")) {
                    parseInput[4] = update.trim().substring(1);
                }
            }
        }

        boolean userConformation = false;
        System.out.println(promptConformation(parseInput));
        while (!userConformation) {
            String conformation = promptUser();
            System.out.println(TextUi.DIVIDER);
            if (conformation.equalsIgnoreCase("y")) {
                userConformation = true;
            } else if (conformation.equalsIgnoreCase("n")) {
                return new IncorrectPatientCommand("    The current information is not added.\n"
                        + "    Please add all the details again! Thank you.\n"
                        + TextUi.DIVIDER);
            } else {
                System.out.println("    Please input [y] for yes or [n] for no");
            }
        }
        return new AddPatientCommand(parseInput[0].trim(), parseInput[1].toUpperCase().trim(),
                Integer.parseInt(parseInput[2].trim()), parseInput[3].trim(), parseInput[4].trim(),
                parseInput[5].trim(), parseInput[6].trim());
    }

    private static String promptUser() {
        System.out.println(TextUi.DIVIDER);
        Scanner in = new Scanner(System.in);
        String reInput = in.nextLine();
        return reInput;
    }

    /**
     * Check if the String can be converted to Integer.
     *
     * @param input value to check if is integer
     * @return true if is an integer, false otherwise
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Prompt user for conformation with this message.
     *
     * @param parseInput details to be displayed to user for confirmation
     * @return string to be displayed to user for confirmation
     */
    public static String promptConformation(String[] parseInput) {
        String text = TextUi.DIVIDER
                + "\n        Name : " + parseInput[0].trim() + "\n"
                + "        NRIC : " + parseInput[1].toUpperCase().trim() + "\n"
                + "        Phone Number : " + parseInput[2].trim() + "\n"
                + "        DOB : " + parseInput[3].trim() + "\n"
                + "        Blood Type : " + parseInput[4].trim() + "\n"
                + "        Allergies : " + parseInput[5].trim() + "\n"
                + "        Remarks : " + parseInput[6].trim() + "\n"
                + "                                                   (Y/N)?";
        return text;
    }
}
