package manageezpz.logic.commands;

import static manageezpz.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static manageezpz.logic.commands.CommandTestUtil.assertCommandFailure;
import static manageezpz.logic.commands.CommandTestUtil.assertCommandSuccess;
import static manageezpz.logic.commands.CommandTestUtil.showPersonAtIndex;
import static manageezpz.logic.commands.DeleteEmployeeCommand.MESSAGE_DELETE_PERSON_SUCCESS;
import static manageezpz.logic.commands.DeleteEmployeeCommand.MESSAGE_USAGE;
import static manageezpz.testutil.TypicalIndexes.INDEX_FIRST;
import static manageezpz.testutil.TypicalIndexes.INDEX_SECOND;
import static manageezpz.testutil.TypicalPersons.getTypicalAddressBookEmployees;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import manageezpz.commons.core.index.Index;
import manageezpz.model.Model;
import manageezpz.model.ModelManager;
import manageezpz.model.UserPrefs;
import manageezpz.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteEmployeeCommand}.
 */
public class DeleteEmployeeCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBookEmployees(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        DeleteEmployeeCommand deleteEmployeeCommand = new DeleteEmployeeCommand(INDEX_FIRST);

        String expectedMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        DeleteEmployeeCommand deleteEmployeeCommand = new DeleteEmployeeCommand(outOfBoundIndex);

        assertCommandFailure(deleteEmployeeCommand, model,
                String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        DeleteEmployeeCommand deleteEmployeeCommand = new DeleteEmployeeCommand(INDEX_FIRST);

        String expectedMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST);

        Index outOfBoundIndex = INDEX_SECOND;

        // Ensures that outOfBoundIndex is still in bounds of address book person list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteEmployeeCommand deleteEmployeeCommand = new DeleteEmployeeCommand(outOfBoundIndex);

        assertCommandFailure(deleteEmployeeCommand, model,
                String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        DeleteEmployeeCommand deleteFirstEmployeeCommand = new DeleteEmployeeCommand(INDEX_FIRST);
        DeleteEmployeeCommand deleteSecondEmployeeCommand = new DeleteEmployeeCommand(INDEX_SECOND);

        // same object -> returns true
        assertTrue(deleteFirstEmployeeCommand.equals(deleteFirstEmployeeCommand));

        // same values -> returns true
        DeleteEmployeeCommand deleteFirstEmployeeCommandCopy = new DeleteEmployeeCommand(INDEX_FIRST);
        assertTrue(deleteFirstEmployeeCommand.equals(deleteFirstEmployeeCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstEmployeeCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstEmployeeCommand.equals(null));

        // different person (employee) -> returns false
        assertFalse(deleteFirstEmployeeCommand.equals(deleteSecondEmployeeCommand));
    }

    /**
     * Updates {@code model}'s filtered person list to show no person.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
