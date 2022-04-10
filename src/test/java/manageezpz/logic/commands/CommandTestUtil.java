package manageezpz.logic.commands;

import static manageezpz.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static manageezpz.logic.parser.CliSyntax.PREFIX_EMAIL;
import static manageezpz.logic.parser.CliSyntax.PREFIX_EVENT;
import static manageezpz.logic.parser.CliSyntax.PREFIX_NAME;
import static manageezpz.logic.parser.CliSyntax.PREFIX_PHONE;
import static manageezpz.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static manageezpz.logic.parser.CliSyntax.PREFIX_TODO;
import static manageezpz.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.exceptions.CommandException;
import manageezpz.model.AddressBook;
import manageezpz.model.Model;
import manageezpz.model.person.Person;
import manageezpz.model.person.PersonMultiplePredicate;
import manageezpz.model.task.Date;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Description;
import manageezpz.model.task.Event;
import manageezpz.model.task.Task;
import manageezpz.model.task.TaskMultiplePredicate;
import manageezpz.model.task.Time;
import manageezpz.model.task.Todo;
import manageezpz.testutil.EditEmployeeDescriptorBuilder;
import manageezpz.testutil.TodoBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    // All attributes pertaining to employees
    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + " "; // space not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditEmployeeCommand.EditEmployeeDescriptor DESC_AMY;
    public static final EditEmployeeCommand.EditEmployeeDescriptor DESC_BOB;

    // Attributes pertaining tasks
    public static final String VALID_TASK_DESCRIPTION = "get a drink";
    public static final List<String> LIST_DESCRIPTIONS = List.of(VALID_TASK_DESCRIPTION.split(" "));
    public static final String VALID_DATE = "2022-01-01"; // Date are in YYYY-MM-DD
    public static final String VALID_TIME = "1700"; // Time are in HHmm
    public static final String VALID_START_TIME = "1000";
    public static final String VALID_END_TIME = "1200";
    public static final String VALID_PRIORITY = "LOW"; // Valid priority are none, low, medium, high
    public static final String INVALID_PRIORITY = "H1GH";
    public static final String VALID_BOOLEAN = "true";
    public static final String INVALID_BOOLEAN = "t";
    public static final String INVALID_DATE = "2022 01 01";

    // addTodo-related commands
    public static final String VALID_TODO_DESC_READ_BOOK = " " + PREFIX_DESCRIPTION + "Read Book";
    public static final String INVALID_TODO_DESC_DRINK = " " + PREFIX_DESCRIPTION + " ";

    // tagTask-related commands
    public static final String VALID_TAG_TASK = " " + 1 + " " + PREFIX_NAME + "Bob Choo";
    public static final String INVALID_TAG_TASK_NO_PREFIX = " " + 1 + " ";
    public static final String INVALID_TAG_TASK_NO_INDEX = " " + PREFIX_NAME + "BOB";
    public static final String INVALID_TAG_TASK_INVALID_INDEX = "asd " + PREFIX_NAME + "BOB";
    public static final String INVALID_TAG_TASK_EMPTY_NAME = " " + 1 + " " + PREFIX_NAME + " ";
    public static final String INVALID_TAG_TASK_PREAMBLE = "asd 1 " + PREFIX_NAME + "BOB";

    // tagPriority-related commands
    public static final String VALID_TAG_PRIORITY = " " + 1 + " " + PREFIX_PRIORITY + "HIGH";
    public static final String INVALID_TAG_PRIORITY_NO_PREFIX = " " + 1 + " ";
    public static final String INVALID_TAG_PRIORITY_NO_INDEX = " " + PREFIX_PRIORITY + "HIGH";
    public static final String INVALID_TAG_PRIORITY_INVALID_INDEX = "asd " + PREFIX_PRIORITY + "HIGH";
    public static final String INVALID_TAG_PRIORITY_INVALID_PRIORITY = " " + 1 + " " + PREFIX_PRIORITY + "IMPORTANT";
    public static final String INVALID_TAG_PRIORITY_EMPTY_PRIORITY = " " + 1 + " " + PREFIX_PRIORITY + " ";
    public static final String INVALID_TAG_PRIORITY_PREAMBLE = "asd 1 " + PREFIX_PRIORITY + "HIGH";
    public static final String INVALID_TAG_PRIORITY_EMPTY_PREAMBLE = " " + PREFIX_PRIORITY + "HIGH";
    // Creation of events
    public static final Todo TODO_TASK;
    public static final Event EVENT_TASK;
    public static final Deadline DEADLINE_TASK;

    // Multiple arguments not allowed for list command
    public static final String INVALID_LIST_MULTIPLE_ARGUMENTS = " " + PREFIX_TODO + " " + PREFIX_EVENT;

    static {
        DESC_AMY = new EditEmployeeDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .build();
        DESC_BOB = new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .build();

        TODO_TASK = new TodoBuilder().withDescription(VALID_TASK_DESCRIPTION).build();
        EVENT_TASK = new Event(new Description(VALID_TASK_DESCRIPTION), new Date(VALID_DATE),
                new Time(VALID_START_TIME), new Time(VALID_END_TIME));
        DEADLINE_TASK = new Deadline(new Description(VALID_TASK_DESCRIPTION), new Date(VALID_DATE),
                new Time(VALID_TIME));
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new PersonMultiplePredicate(Arrays.asList(splitName[0]), null,
                null));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the task at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showTaskAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredTaskList().size());

        Task task = model.getFilteredTaskList().get(targetIndex.getZeroBased());
        final String[] splitName = task.getDescription().toString().split("\\s+");
        TaskMultiplePredicate predicate = new TaskMultiplePredicate(
                null,
                Arrays.asList(splitName[0]),
                null,
                null,
                null,
                null
        );
        model.updateFilteredTaskList(predicate);

        assertEquals(1, model.getFilteredTaskList().size());
    }

}
