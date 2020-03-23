package seedu.command.student;

import seedu.StudentList;
import seedu.command.Command;
import seedu.exception.DukeException;
import seedu.ui.DisplayList;
import seedu.ui.UI;

import static seedu.duke.Duke.studentListCollection;

public class AddStudent extends Command {

    private static UI ui = new UI();
    private DisplayList displayList;
    StudentList newStudentList;

    public AddStudent(StudentList newStudentList) {
        this.newStudentList = newStudentList;
        this.displayList = new DisplayList();
    }

    protected void addToList() {
        String listName = ui.getListName();
        StudentList studentList = new StudentList(listName);
        ui.addStudent(studentList);
        displayList.printStudentList(studentList, listName);
        studentListCollection.add(studentList);
    }

    @Override
    public void execute() {
        addToList();
    }
}
