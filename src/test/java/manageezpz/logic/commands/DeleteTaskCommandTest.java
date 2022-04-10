package manageezpz.logic.commands;

import static manageezpz.logic.commands.CommandTestUtil.assertCommandSuccess;
import static manageezpz.logic.commands.DeleteTaskCommand.MESSAGE_DELETE_TASK_SUCCESS;
import static manageezpz.testutil.TypicalIndexes.INDEX_FIRST;
import static manageezpz.testutil.TypicalIndexes.INDEX_SECOND;
import static manageezpz.testutil.TypicalPersons.AMY;
import static manageezpz.testutil.TypicalPersons.BOB;
import static manageezpz.testutil.TypicalTasks.GET_DRINK;
import static manageezpz.testutil.TypicalTasks.HOUSE_VISTING;
import static manageezpz.testutil.TypicalTasks.READ_BOOK;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import manageezpz.model.AddressBook;
import manageezpz.model.Model;
import manageezpz.model.ModelManager;
import manageezpz.model.UserPrefs;
import manageezpz.model.task.Task;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteTaskCommand}.
 */
public class DeleteTaskCommandTest {
    private Model model;

    @Test
    public void execute_validIndexUnfilteredList_success() {
        model = new ModelManager(new AddressBook(), new UserPrefs());

        // Add persons to the new address book
        model.addPerson(AMY);
        model.addPerson(BOB);

        // Add tasks to the new address book
        model.addTask(READ_BOOK);
        model.addTask(GET_DRINK);
        model.addTask(HOUSE_VISTING);

        // Tag READ_BOOK task to AMY
        model.tagEmployeeToTask(model.getAddressBook().getTaskList().get(0),
                model.getAddressBook().getPersonList().get(0));
        model.increaseNumOfTasks(model.getAddressBook().getPersonList().get(0));

        // Tag GET_DRINK task to AMY
        model.tagEmployeeToTask(model.getAddressBook().getTaskList().get(1),
                model.getAddressBook().getPersonList().get(0));
        model.increaseNumOfTasks(model.getAddressBook().getPersonList().get(0));

        // Tag HOUSE_VISTING task to AMY
        model.tagEmployeeToTask(model.getAddressBook().getTaskList().get(2),
                model.getAddressBook().getPersonList().get(0));
        model.increaseNumOfTasks(model.getAddressBook().getPersonList().get(0));

        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST.getZeroBased());
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(INDEX_FIRST);

        String expectedMessage = String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        DeleteTaskCommand deleteFirstTaskCommand = new DeleteTaskCommand(INDEX_FIRST);
        DeleteTaskCommand deleteSecondTaskCommand = new DeleteTaskCommand(INDEX_SECOND);

        // same object -> returns true
        assertTrue(deleteFirstTaskCommand.equals(deleteFirstTaskCommand));

        // same values -> returns true
        DeleteTaskCommand deleteFirstTaskCommandCopy = new DeleteTaskCommand(INDEX_FIRST);
        assertTrue(deleteFirstTaskCommand.equals(deleteFirstTaskCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstTaskCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstTaskCommand.equals(null));

        // different task -> returns false
        assertFalse(deleteFirstTaskCommand.equals(deleteSecondTaskCommand));
    }

    /**
     * Updates {@code model}'s filtered task list to show no task.
     */
    private void showNoTask(Model model) {
        model.updateFilteredTaskList(p -> false);

        assertTrue(model.getFilteredTaskList().isEmpty());
    }
}
