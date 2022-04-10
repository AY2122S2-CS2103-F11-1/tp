package manageezpz.logic.commands;

import static manageezpz.logic.commands.CommandTestUtil.assertCommandSuccess;
import static manageezpz.logic.commands.DeleteTaskCommand.MESSAGE_DELETE_TASK_SUCCESS;
import static manageezpz.testutil.TypicalIndexes.INDEX_FIRST;
import static manageezpz.testutil.TypicalIndexes.INDEX_SECOND;
import static manageezpz.testutil.TypicalPersons.ALEX;
import static manageezpz.testutil.TypicalPersons.BERNICE;
import static manageezpz.testutil.TypicalTasks.FINISH_CLIENT_PROPOSAL;
import static manageezpz.testutil.TypicalTasks.MEETING_WITH_CLIENT;
import static manageezpz.testutil.TypicalTasks.REVIEW_MONTHLY_FINANCE_KPI;
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
        model.addPerson(ALEX);
        model.addPerson(BERNICE);

        // Add tasks to the new address book
        model.addTask(REVIEW_MONTHLY_FINANCE_KPI);
        model.addTask(FINISH_CLIENT_PROPOSAL);
        model.addTask(MEETING_WITH_CLIENT);

        // Tag REVIEW_MONTHLY_FINANCE_KPI task to ALEX
        model.tagEmployeeToTask(model.getAddressBook().getTaskList().get(0),
                model.getAddressBook().getPersonList().get(0));
        model.increaseNumOfTasks(model.getAddressBook().getPersonList().get(0));

        // Tag FINISH_CLIENT_PROPOSAL task to ALEX
        model.tagEmployeeToTask(model.getAddressBook().getTaskList().get(1),
                model.getAddressBook().getPersonList().get(0));
        model.increaseNumOfTasks(model.getAddressBook().getPersonList().get(0));

        // Tag MEETING_WITH_CLIENT task to ALEX
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
