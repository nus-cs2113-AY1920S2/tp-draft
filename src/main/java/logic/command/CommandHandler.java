package logic.command;

import exception.InvalidUrlException;
import model.meeting.MeetingList;
import exception.MoException;
import model.meeting.Meeting;
import logic.modulelogic.LessonsGenerator;
import logic.schedulelogic.ScheduleHandler;
import model.contact.Contact;
import model.contact.ContactList;
import ui.TextUI;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;

import static common.Messages.MESSAGE_WRONG_COMMAND_DELETE;
import static common.Messages.MESSAGE_WRONG_COMMAND_MEETING;
import static common.Messages.MESSAGE_WRONG_COMMAND_SCHEDULE;

public class CommandHandler {


    public static Contact addContact(ContactList myContactList, String[] userInputWords,
                                     Integer startDay, Integer endDay) throws MoException {
        Contact member;
        int checkerForRepeatedName;
        checkerForRepeatedName = myContactList.getContactList().stream()
                .mapToInt(person -> check(person, userInputWords[0])).sum();
        if (checkerForRepeatedName == 1) {
            TextUI.showRepeatedPerson(userInputWords[0]);
            throw new MoException("Repeated user");
        }

        member = new Contact(userInputWords[0]);
        String name = userInputWords[0];
        String url = userInputWords[1];

        LessonsGenerator myLessonGenerator;
        try {
            myLessonGenerator = new LessonsGenerator(url);
            myLessonGenerator.generate();
            ArrayList<String[]> myLessonDetails = myLessonGenerator.getLessonDetails();

            for (int k = 0; k < myLessonDetails.size(); k++) {
                String startTimeString = null;
                String endTimeString = null;
                String[] weeks = new String[0];
                for (int j = 0; j < myLessonDetails.get(k).length; j++) {
                    switch (j) {
                    case 0:
                        startTimeString = myLessonDetails.get(k)[j].substring(0, 2) + ":" + myLessonDetails.get(k)[j].substring(2);
                        break;
                    case 1:
                        endTimeString = myLessonDetails.get(k)[j].substring(0, 2) + ":" + myLessonDetails.get(k)[j].substring(2);
                        break;
                    case 2:
                        startDay = getNumberFromDay(myLessonDetails.get(k)[j]);
                        endDay = startDay;
                        break;
                    case 3:
                        weeks = myLessonDetails.get(k)[j].split(":");
                        //future improvement: since myLessonDetails.get(k)[3] contains data on the
                        // week number that this class occurs on, add capability of schedule to reflect
                        // schedule of the current week.
                        //
                        //0900 1200 Friday 5:7:9:11
                        //1600 1800 Thursday 1:2:3:4:5:6:7:8:9:10:11:12:13
                        //1600 1800 Tuesday 1:2:3:4:5:6:7:8:9:10:11:12:13
                        //0900 1200 Tuesday 1:2:3:4:5:6
                        break;
                    default:
                        //data only has four sections from api
                        throw new AssertionError(j);
                    }
                }
                member.addBusyBlocks(name, startDay, startTimeString, endDay, endTimeString, weeks);
            }
            TextUI.showAddedMember(member.getName());
        } catch (InvalidUrlException e) {
            System.out.println(e.getMessage());
        }
        return member;
    }

    public static void editContact(String[] userInputWords, Contact mainUser, ContactList contactList,
                                   int currentWeekNumber) throws MoException {

        try {
            if (userInputWords.length != 7) {
                throw new MoException(MESSAGE_WRONG_COMMAND_SCHEDULE);
            }
            int endOfMonthDate = 0;
            endOfMonthDate = getEndOfMonthDate(endOfMonthDate);

            Integer startDay;
            Integer endDay;
            int startDate = Integer.parseInt(userInputWords[3]);
            int endDate = Integer.parseInt(userInputWords[5]);
            int startOfWeekDate = getStartOfWeekDate();
            startDay = getDay(endOfMonthDate, startOfWeekDate, startDate);
            endDay = getDay(endOfMonthDate, startOfWeekDate, endDate);

            String meetingName = userInputWords[2];

            int memberNumber = Integer.parseInt(userInputWords[2]);
            Contact member = contactList.getContactList().get(memberNumber);
            String memberName = member.getName();
            LocalTime startTime = LocalTime.parse(userInputWords[4]);
            LocalTime endTime = LocalTime.parse(userInputWords[6]);
            String startTimeString = userInputWords[4];
            String endTimeString = userInputWords[6];
            String[] thisWeekNumber = {Integer.toString(currentWeekNumber)};

            if (memberNumber != 0 || ScheduleHandler.isValidEdit(mainUser, startDay, startTime, endDay, endTime, currentWeekNumber)) {
                if (userInputWords[1].equals("busy")) {
                    member.addBusyBlocks(memberName, startDay, startTimeString, endDay, endTimeString,thisWeekNumber);
                } else if (userInputWords[1].equals("free")) {
                    member.addFreeBlocks(memberName, startDay, startTimeString, endDay, endTimeString,thisWeekNumber);
                }
                TextUI.showContactEdited(member.getName(),userInputWords[2]);
            } else {
                throw new AssertionError("isValidEdit() should not return false");
            }
        } catch (MoException e) {
            System.out.println(e.getMessage());
            TextUI.printFormatEdit();
        } catch (DateTimeParseException e) {
            TextUI.timeOutOfRangeMsg();
            TextUI.printFormatEdit();
        } catch (NumberFormatException e) {
            TextUI.invalidNumberMsg();
            TextUI.printFormatEdit();
        }
    }

    private static Integer getNumberFromDay(String day) {
        int dayInNumber;
        switch (day) {
        case "Monday":
            dayInNumber = 1;
            break;
        case "Tuesday":
            dayInNumber = 2;
            break;
        case "Wednesday":
            dayInNumber = 3;
            break;
        case "Thursday":
            dayInNumber = 4;
            break;
        case "Friday":
            dayInNumber = 5;
            break;
        case "Saturday":
            dayInNumber = 6;
            break;
        case "Sunday":
            dayInNumber = 0;
            break;
        default:
            dayInNumber = Integer.parseInt(null);
            break;
        }
        return dayInNumber;
    }

    private static int check(Contact person, String name) {
        if (person.getName().equals(name)) {
            return 1;
        } else {
            return 0;
        }
    }


    public static void listMeetings(String[] userInputWords, MeetingList meetingList) {
        try {
            if (userInputWords.length != 1) {
                throw new MoException(MESSAGE_WRONG_COMMAND_MEETING);
            }
            meetingList.show();
        } catch (MoException e) {
            System.out.println(e.getMessage());
            TextUI.printFormatMeeting();
        }
    }

    public static void deleteMeeting(String[] userInputWords, MeetingList meetingList, Contact mainUser, ContactList
            contactList) {
        try {
            int index = Integer.parseInt(userInputWords[1]) - 1;
            Meeting meetingToDelete = meetingList.getMeetingList().get(index);
            String meetingNameToDelete = meetingToDelete.getMeetingName();
            mainUser.deleteBlocksWithName(meetingNameToDelete);
            meetingList.delete(index);
            contactList.set(0, mainUser);
        } catch (IndexOutOfBoundsException e) {
            TextUI.displayInvalidDeleteTarget();
        }
    }

    public static void scheduleMeeting(String[] userInputWords, MeetingList meetingList, Contact mainUser,
                                       ContactList contactList, int currentWeekNumber) {

        try {
            if (userInputWords.length < 6) {
                throw new MoException(MESSAGE_WRONG_COMMAND_SCHEDULE);
            }
            int endOfMonthDate = 0;
            endOfMonthDate = getEndOfMonthDate(endOfMonthDate);

            Integer startDay;
            Integer endDay;
            int startOfWeekDate = getStartOfWeekDate();
            String meetingName = userInputWords[1];
            int startDate = Integer.parseInt(userInputWords[2]);
            int endDate = Integer.parseInt(userInputWords[4]);
            startDay = getDay(endOfMonthDate, startOfWeekDate, startDate);
            endDay = getDay(endOfMonthDate, startOfWeekDate, endDate);


            LocalTime startTime = LocalTime.parse(userInputWords[3]);
            LocalTime endTime = LocalTime.parse(userInputWords[5]);
            if (ScheduleHandler.isValidMeeting(mainUser, startDay, startTime, endDay, endTime, currentWeekNumber)) {
                Meeting myMeeting = new Meeting(meetingName, startDay, startTime, endDay, endTime, startDate, endDate);
                meetingList.add(myMeeting);
                String[] thisWeekNumber = {Integer.toString(currentWeekNumber)};
                mainUser.addBusyBlocks("meeting", startDay, userInputWords[3], endDay, userInputWords[5], thisWeekNumber);
                TextUI.meetingListSizeMsg(meetingList);
            } else {
                System.out.println("Schedule is blocked at that timeslot");
            }
        } catch (MoException e) {
            System.out.println(e.getMessage());
            TextUI.printFormatSchedule();
        } catch (DateTimeParseException e) {
            TextUI.timeOutOfRangeMsg();
            TextUI.printFormatSchedule();
        } catch (NumberFormatException e) {
            TextUI.invalidNumberMsg();
            TextUI.printFormatSchedule();
        }
        // Replace main user's timetable with updated model.meeting blocks into TeamMember.TeamMemberList for model.storage purposes.
        contactList.set(0, mainUser);
    }

    private static Integer getDay(int endOfMonthDate, int startOfWeekDate, int startDate) {
        Integer day;
        if (startDate - startOfWeekDate < 0) {
            day = endOfMonthDate - startOfWeekDate + startDate;
        } else {
            day = startDate - startOfWeekDate;
        }
        return day;
    }

    private static int getEndOfMonthDate(int endOfMonthDate) {
        Calendar cal = Calendar.getInstance();
        String day = (cal.getTime().toString().split(" "))[0];
        String month = (cal.getTime().toString().split(" "))[1];
        int distFromPreviousSunday = 0;
        for (int i = 0; i < 6 && !day.equals("Sun"); distFromPreviousSunday++, i++) {
            cal.add(Calendar.DATE, -1);
            if (!(cal.getTime().toString().split(" "))[1].equals(month)) {
                endOfMonthDate = Integer.parseInt(cal.getTime().toString().split(" ")[2]);
            }
            day = (cal.getTime().toString().split(" "))[0];
        }
        Calendar cal2 = Calendar.getInstance();
        for (int i = 0; i < (14 - distFromPreviousSunday); i++) {
            if (!(cal2.getTime().toString().split(" "))[1].equals(month)) {
                break;
            }
            endOfMonthDate = Integer.parseInt(cal2.getTime().toString().split(" ")[2]);
            cal2.add(Calendar.DATE, 1);
        }
        return endOfMonthDate;
    }

    private static int getDateOfPreviousSunday(String[] data) {
        int date;
        Calendar cal = Calendar.getInstance();
        switch (data[0]) {
        case "Sun":
            date = Integer.parseInt(data[2]);
            break;
        case "Mon":
            cal.add(Calendar.DATE, -1);
            break;
        case "Tue":
            cal.add(Calendar.DATE, -2);
            break;
        case "Wed":
            cal.add(Calendar.DATE, -3);
            break;
        case "Thu":
            cal.add(Calendar.DATE, -4);
            break;
        case "Fri":
            cal.add(Calendar.DATE, -5);
            break;
        case "Sat":
            cal.add(Calendar.DATE, -6);
            break;
        default:
            cal.add(Calendar.DATE, 0);
        }
        String[] temp = cal.getTime().toString().split(" ");
        date = Integer.parseInt(temp[2]);
        return date;
    }

    private static int getStartOfWeekDate() {
        String[] data = java.util.Calendar.getInstance().getTime().toString().split(" ");
        String day = data[0];
        int date = Integer.parseInt(data[2]);
        switch (day) {
        case "Mon":
            date -= 1;
            break;
        case "Tue":
            date -= 2;
            break;
        case "Wed":
            date -= 3;
            break;
        case "Thu":
            date -= 4;
            break;
        case "Fri":
            date -= 5;
            break;
        case "Sat":
            date -= 6;
            break;
        case "Sun":
            date = date;
            break;
        default:
            date = date;
            break;
        }
        return date;
    }

    public static void displayTimetable(String[] userInputWords, Contact mainUser,
                                        ContactList contactList, int weekNumber, int weeksMoreToView) throws MoException {
        int memberNumber;
        Contact member;
        try {
            String todayDate = java.util.Calendar.getInstance().getTime().toString().substring(0, 10).trim();
            if (userInputWords.length > 1) {
                ArrayList<Contact> myScheduleList = new ArrayList<Contact>();
                for (int i = 1; i < userInputWords.length; i++) {
                    memberNumber = Integer.parseInt(userInputWords[i]);
                    member = contactList.getContactList().get(memberNumber);
                    myScheduleList.add(member);
                }

                ScheduleHandler myScheduleHandler = new ScheduleHandler(myScheduleList);
                Boolean[][][] myMasterSchedule;
                myMasterSchedule = myScheduleHandler.getMasterSchedule();
                System.out.println("Today is " + todayDate + ", week " + weekNumber + ".");
                System.out.println("Timetable of the selected team member/s this week:");
                System.out.println();
                TextUI.printTimetable(myMasterSchedule, weeksMoreToView, weekNumber);
            } else {
                System.out.println("Today is " + todayDate + ", week " + weekNumber + ".");
                System.out.println("Your timetable this week:");
                System.out.println();
                TextUI.printTimetable(mainUser.getSchedule(), weeksMoreToView, weekNumber);
            }
        } catch (IndexOutOfBoundsException e) {
            TextUI.indexOutOfBoundsMsg();
            TextUI.printFormatTimetable();
        } catch (NumberFormatException e) {
            TextUI.invalidNumberMsg();
            TextUI.printFormatTimetable();
        }
    }

    public static void listContacts(ContactList contactList) throws MoException {
        try {
            TextUI.teamMemberListMsg(contactList.getContactList());
        } catch (NullPointerException e) {
            throw new MoException("You have no stored contacts.");
        }
    }

}
