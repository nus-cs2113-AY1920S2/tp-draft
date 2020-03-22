package seedu.happypills.ui;

import seedu.happypills.data.Patient;
import seedu.happypills.data.PatientList;
import seedu.happypills.data.PatientMap;

import java.util.Map;

public class TextUi {
    public static final String DIVIDER = "    =====================================================";

    /**
     * Shows a list of patients' name and their NRIC to the user, formatted as an indexed list.
     * @param patients A patient list with all existing patients
     * @return a message to be displayed to user
     */
    public static String getList(PatientMap patients) {
        String message = "";
        for (Map.Entry patient : patients.entrySet()) {
            String nric = (String)patient.getKey();
            Patient p = (Patient)patient.getValue();
            String name = p.getName();
            message += "    " + name + " | " + nric + "\n";
        }
        message += DIVIDER;
        return message;
    }

    /**
     * Generates and prints the list of commands.
     * @return a message to be displayed to user
     */
    public static String getHelp() {
        String helpMessage = "    HappyPills Commands:\n"
                + "      add /ic[NRIC] /n[NAME] /p[PHONE_NUMBER] /d[DOB] /b[BLOOD_TYPE]\n"
                + "      list\n"
                + "      get [NRIC]\n"
                + "      edit [NRIC] /p[PHONE_NUMBER] or /a[ALLERGIES] or /r[REMARKS]\n"
                + "      delete [NRIC]\n"
                + "      help\n"
                + "      exit\n"
                + "      For more detailed command instructions, enter help [COMMAND].\n";
        helpMessage += DIVIDER;
        return helpMessage;
    }

    /**
     * Displays empty list message when there are no patients in the list.
     * @return a message to be displayed to user
     */
    public static String getEmptyList() {
        String emptyListMessage = "    There are no patients in the list.\n" + DIVIDER;
        return emptyListMessage;
    }

    public static void printPatient(Patient patient) {
        System.out.println("    Got it! I've added this patient:");
        System.out.println(patient + DIVIDER);
    }

    /**
     * format a get patient success message for user.
     * Includes a specified string, requested patient details and a divider.
     * @param patient patient object to be returned as a part of the string.
     * @return a formatted success message string to be displayed to user.
     */
    public static String getPatientSuccessMessage(Patient patient) {
        String returnMessage = "    Here are the patient's details:\n" + patient + DIVIDER;
        return returnMessage;
    }

    /**
     * Generates and prints the welcome message upon the start of the application.
     */
    public void printWelcomeMessage() {
        String logo = "\n"
                + " __   __  _______  _______  _______  __   __  _______  ___   ___      ___      _______\n"
                + "|  | |  ||   _   ||       ||       ||  | |  ||       ||   | |   |    |   |    |       |\n"
                + "|  |_|  ||  |_|  ||    _  ||    _  ||  |_|  ||    _  ||   | |   |    |   |    |  _____|\n"
                + "|       ||       ||   |_| ||   |_| ||       ||   |_| ||   | |   |    |   |    | |_____\n"
                + "|       ||       ||    ___||    ___||_     _||    ___||   | |   |___ |   |___ |_____  |\n"
                + "|   _   ||   _   ||   |    |   |      |   |  |   |    |   | |       ||       | _____| |\n"
                + "|__| |__||__| |__||___|    |___|      |___|  |___|    |___| |_______||_______||_______|\n";

        System.out.println("Hello from" + logo);
        System.out.println("What can I do for you today, doctor?");
    }

    /**
     * format an add patient success message for user verification.
     * Includes a specified string, patient details and a divider.
     * @param patient patient object added to patientList
     * @return a string message to be displayed to the user for successful addCommand executed
     */
    public static String getPatient(Patient patient) {
        String message;
        message = "    Got it! I've added this patient:\n" + patient + DIVIDER;
        return message;
    }

    /**
     * Displays confirmation message for deleting patients.
     *
     * @param patient The patient to be deleted.
     */
    public static void printDeleteConfirmation(Patient patient) {
        System.out.println("    Are you sure you want to delete this patient:\n      "
                + patient.getName()
                + " || "
                + patient.getNric()
                + "\n                                                   (Y/N)?\n"
                + DIVIDER);
    }

    /**
     * Displays confirmation message for deleting patients.
     *
     * @param patient The patient to be deleted.
     */
    public static void printDeleteConfirmationAgain(Patient patient) {
        System.out.println("    Please try again."
                + "    Are you sure you want to delete this patient:\n      "
                + patient.getName()
                + " || "
                + patient.getNric()
                + "\n                                                   (Y/N)?\n"
                + DIVIDER);
    }

    /**
     * Displays message that patient is not deleted.
     *
     * @return display message.
     */
    public static String printNotDeleted() {
        return DIVIDER + "\n    Patient is not deleted.\n";
    }

    public static String printIncorrectCommand(String command) {
        return "    The command \"" + command + "\" does not exist. Please try again.\n"
                + DIVIDER;
    }

    /**
     * Display updated patient's records.
     *
     * @param patient The patient whose records were updated.
     * @return The patient's updated records.
     */
    public static String printEditSuccess(Patient patient) {
        String message = "    Patient details have been updated as follows:\n"
                + patient + DIVIDER;
        return message;
    }

    /**
     * Displays detailed Add command instruction.
     *
     * @return The Add command instruction.
     */
    public static String printAddHelp() {
        String message = "    To add a new patient into the program, use the following command:\n"
                + "    Note: patient details are within the parenthesis [ ]\n"
                + "      add /ic[NRIC] /n[NAME] /p[PHONE_NUMBER] /d[DOB] /b[BLOOD_TYPE]\n"
                + "    The command above adds a new patient.\n"
                + "    To add allergies and remarks to the patient's details, use the following commands\n"
                + "      add /ic[NRIC] /a[ALLERGIES]\n"
                + "      add /ic[NRIC] /r[REMARKS]\n"
                + "    Example:\n"
                + "      add /icS9999999Z /nJanice /p999 /d12-11-98 /bA+\n"
                + "      add /icS9999999Z /aSchool\n"
                + "      add /icS9999999Z /rHad contact with COVID-19 Case200\n"
                + DIVIDER;
        return message;
    }

    /**
     * Displays detailed List command instruction.
     *
     * @return The List command instruction.
     */
    public static String printListHelp() {
        String message = "    To retrieve a list of all the patients within the program,\n"
                + "    run the following command:\n"
                + "      list"
                + DIVIDER;
        return message;
    }

    /**
     * Displays detailed Get command instruction.
     *
     * @return The Get command instruction.
     */
    public static String printGetHelp() {
        String message = "    To retrieve a patient's information, run the following command:\n"
                + "    Note: patient details are within the parenthesis [ ]\n"
                + "      get [NRIC]\n"
                + "    Example:\n"
                + "      get S9999999Z\n"
                + "    The command above will display information regarding the patient with NRIC S9999999Z.\n"
                + DIVIDER;
        return message;
    }

    /**
     * Displays detailed Edit command instruction.
     *
     * @return The Edit command instruction.
     */
    public static String printEditHelp() {
        String message = "    To edit a patient's information, run the following command:\n"
                + "    Note: patient details are within the parenthesis [ ]\n"
                + "      edit [NRIC] /p[PHONE_NUMBER] to edit patient's phone number,\n"
                + "      edit [NRIC] /a[ALLERGIES] to edit patient's allergies,\n"
                + "      edit [NRIC] /r[REMARKS] to edit patient's remarks\"\n"
                + "    Do note that editing the patient's records will overwrite any previous information.\n"
                + "    Adding of allergies or remarks can be done with the add command.\n"
                + DIVIDER;
        return message;
    }

    /**
     * Displays detailed Delete command instruction.
     *
     * @return The Delete command instruction.
     */
    public static String printDeleteHelp() {
        String message = "    To delete a patient's records, run the following command:\n"
                + "    Note: patient details are within the parenthesis [ ]\n"
                + "      delete [NRIC]\n"
                + "    The user will be prompted to confirm if they would like to delete the patient's records.\n"
                + "    Do note that deletion cannot be undone.\n"
                + DIVIDER;
        return message;
    }

    /**
     * Displays detailed Help command instruction.
     *
     * @return The Help command instruction.
     */
    public static String printHelpHelp() {
        String message = "    To understand more of the program's features, run the following command:\n"
                + "      help\n"
                + "    The above command will list down all the commands of the program.\n"
                + "    For more detailed usage of each command, enter help [COMMAND].\n"
                + DIVIDER;
        return message;
    }

    /**
     * Displays detailed Exit command instruction.
     *
     * @return The Exit command instruction.
     */
    public static String printExitHelp() {
        String message = "    To exit the program, run the following command:\n"
                + "      exit\n"
                + "    The above command will save the current patient records and terminate the program.\n"
                + DIVIDER;
        return message;
    }

    /**
     * Displays Exit message.
     */
    public static void printExit() {
        System.out.println("Thank you for using HappyPills! ^.^");
        System.out.println("See you again!\n" + DIVIDER);
    }

    /**
     * Append the divider to the given message.
     *
     * @param s The message that requires the divider to be appended.
     * @return The message with the appended divider.
     */
    public static String appendDivider(String s) {
        return s + DIVIDER;
    }

    /**
     * Prepend the divider to the given message.
     *
     * @param s The message that requires the divider to be prepended.
     * @return The message with the prepended divider.
     */
    public static String prependDivider(String s) {
        return DIVIDER + "\n" + s;
    }

    /**
     * Display message that a patient already exists in the program.
     *
     * @param nric NRIC of existing patient.
     * @return display message.
     */
    public static String patientExists(String nric) {
        return "    Patient " + nric + " already exists in the program.";
    }

    public static String patientNotExist(String nric) {
        return "The patient you are looking for cannot be found";
    }

    /**
     * Display message that patient is successfully deleted.
     *
     * @param patient Patient to be deleted.
     * @return display message.
     */
    public static String deletePatient(Patient patient) {
        return "    Patient " + patient.getName() + " || " + patient.getNric()
            + " has been deleted successfully.\n";
    }
}
