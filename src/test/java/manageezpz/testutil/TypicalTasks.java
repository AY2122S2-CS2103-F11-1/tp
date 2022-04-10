package manageezpz.testutil;

import static manageezpz.logic.commands.CommandTestUtil.VALID_TASK_DESCRIPTION;
import static manageezpz.testutil.TypicalPersons.GEORGE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import manageezpz.model.AddressBook;
import manageezpz.model.task.*;

public class TypicalTasks {

    public static final Todo READ_BOOK = new TodoBuilder().withDescription("Read Book").build();
    public static final Todo RETURN_BOOK = new TodoBuilder().withDescription("Return Book").build();
    public static final Todo GO_FOR_RUN = new TodoBuilder().withDescription("Go for run").build();
    public static final Todo GET_HAIRCUT = new TodoBuilder().withDescription("Get Haircut").build();
    public static final Todo WEEKLY_QUIZ = new TodoBuilder().withDescription("Weekly Quiz").build();
    public static final Deadline GET_DRINK = new DeadlineBuilder().withDescription("Get Drink")
            .withDate("2022-05-13").withTime("1800").build();
    public static final Deadline PROJECT_CAPSTONE = new DeadlineBuilder().withDescription("Project capstone")
            .withDate("2022-04-01").withTime("2359").build();
    public static final Deadline FYP_REPORT = new DeadlineBuilder().withDescription("FYP Report").withDate("2024-05-02")
            .withTime("2359").build();
    public static final Event HOUSE_VISTING = new EventBuilder().withDescription("House Visiting")
            .withDate("2022-09-15").withStartTime("1800").withEndTime("2000").build();
    public static final Event MALAYSIA_BORDERS_OPEN = new EventBuilder().withDescription("Malaysia Borders open")
            .withDate("2022-04-01").withStartTime("0000").withEndTime("2359").build();
    public static final Event CS2103_PRACTICAL_EXAM = new EventBuilder().withDescription("CS2103 Practical Exam")
            .withDate("2022-04-16").withStartTime("1400").withEndTime("1600").build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final Todo GET_A_DRINK = new TodoBuilder().withDescription(VALID_TASK_DESCRIPTION).build();

    // Added for DeleteTaskCommandTest
    public static final Todo REVIEW_MONTHLY_FINANCE_KPI = new TodoBuilder()
            .withDescription("Review Monthly Finance KPI")
            .build();
    public static final Deadline FINISH_CLIENT_PROPOSAL = new DeadlineBuilder()
            .withDescription("Finish Client Proposal").withDate("2022-03-15").withTime("1800")
            .build();
    public static final Event MEETING_WITH_CLIENT = new EventBuilder()
            .withDescription("Meeting with Client").withDate("2022-03-15").withStartTime("1300").withEndTime("1400")
            .build();

    private TypicalTasks() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical tasks.
     */
    public static AddressBook getTypicalAddressBookTasks() {
        AddressBook ab = new AddressBook();
        for (Task task : getTypicalTask()) {
            ab.addTask(task);
        }
        return ab;
    }

    public static List<Task> getTypicalTask() {
        // Set Priority
        WEEKLY_QUIZ.setPriority("HIGH");
        PROJECT_CAPSTONE.setPriority("HIGH");
        FYP_REPORT.setPriority("HIGH");

        // Set Assignee
        RETURN_BOOK.addAssignees(GEORGE);
        PROJECT_CAPSTONE.addAssignees(GEORGE);
        FYP_REPORT.addAssignees(GEORGE);
        HOUSE_VISTING.addAssignees(GEORGE);

        // Set marked
        RETURN_BOOK.setTaskDone();
        PROJECT_CAPSTONE.setTaskDone();

        return new ArrayList<>(Arrays.asList(WEEKLY_QUIZ, PROJECT_CAPSTONE, FYP_REPORT, READ_BOOK, RETURN_BOOK,
                GO_FOR_RUN, GET_HAIRCUT, GET_A_DRINK, GET_DRINK, HOUSE_VISTING, MALAYSIA_BORDERS_OPEN,
                CS2103_PRACTICAL_EXAM));
    }
}
