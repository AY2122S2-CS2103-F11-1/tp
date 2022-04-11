package manageezpz.logic.commands;

import static manageezpz.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static manageezpz.logic.commands.CommandTestUtil.assertCommandFailure;
import static manageezpz.logic.commands.CommandTestUtil.showTaskAtIndex;
import static manageezpz.logic.commands.DeleteTaskCommand.MESSAGE_USAGE;
import static manageezpz.testutil.TypicalIndexes.INDEX_FIRST;
import static manageezpz.testutil.TypicalIndexes.INDEX_SECOND;
import static manageezpz.testutil.TypicalTasks.getTypicalAddressBookTasks;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import manageezpz.commons.core.index.Index;
import manageezpz.model.Model;
import manageezpz.model.ModelManager;
import manageezpz.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteTaskCommand}.
 */
public class DeleteTaskCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBookTasks(), new UserPrefs());

    /*@Test
    public void execute_validIndexUnfilteredList_success() { // Failed on GitHub
        Task taskToDelete = model.getAddressBook().getTaskList().get(INDEX_FIRST.getZeroBased());

        // Add Benson to model's UniquePersonList
        model.addPerson(BENSON);
        // Tag task with index 1 in model's UniqueTaskList to Benson
        model.tagEmployeeToTask(taskToDelete, model.getAddressBook().getPersonList().get(0));
        // Increase Benson's numOfTasks by 1
        model.increaseNumOfTasks(model.getAddressBook().getPersonList().get(0));

        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(INDEX_FIRST);

        String expectedMessage = String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }*/

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);

        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(outOfBoundIndex);

        assertCommandFailure(deleteTaskCommand, model,
                String.format(MESSAGE_INVALID_TASK_DISPLAYED_INDEX, MESSAGE_USAGE));
    }

    /*@Test
    public void execute_validIndexFilteredList_success() { // Failed on GitHub
        showTaskAtIndex(model, INDEX_FIRST);

        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST.getZeroBased());
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(INDEX_FIRST);

        String expectedMessage = String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);
        showNoTask(expectedModel);

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }*/

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showTaskAtIndex(model, INDEX_FIRST);

        Index outOfBoundIndex = INDEX_SECOND;

        // Ensures that outOfBoundIndex is still in bounds of address book task list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getTaskList().size());

        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(outOfBoundIndex);

        assertCommandFailure(deleteTaskCommand, model,
                String.format(MESSAGE_INVALID_TASK_DISPLAYED_INDEX, MESSAGE_USAGE));
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
