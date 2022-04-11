package manageezpz.logic.commands;

import static manageezpz.commons.core.Messages.MESSAGE_DUPLICATE_PERSON;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static manageezpz.logic.commands.CommandTestUtil.DESC_AMY;
import static manageezpz.logic.commands.CommandTestUtil.DESC_BOB;
import static manageezpz.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static manageezpz.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static manageezpz.logic.commands.CommandTestUtil.assertCommandFailure;
import static manageezpz.logic.commands.CommandTestUtil.assertCommandSuccess;
import static manageezpz.logic.commands.CommandTestUtil.showPersonAtIndex;
import static manageezpz.logic.commands.EditEmployeeCommand.MESSAGE_EDIT_PERSON_SUCCESS;
import static manageezpz.logic.commands.EditEmployeeCommand.MESSAGE_USAGE;
import static manageezpz.testutil.TypicalIndexes.INDEX_FIRST;
import static manageezpz.testutil.TypicalIndexes.INDEX_SECOND;
import static manageezpz.testutil.TypicalPersons.getTypicalAddressBookEmployees;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.EditEmployeeCommand.EditEmployeeDescriptor;
import manageezpz.model.Model;
import manageezpz.model.ModelManager;
import manageezpz.model.UserPrefs;
import manageezpz.model.person.Person;
import manageezpz.testutil.EditEmployeeDescriptorBuilder;
import manageezpz.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code EditEmployeeCommand}.
 */
public class EditEmployeeCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBookEmployees(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditEmployeeCommand.EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder(editedPerson).build();
        EditEmployeeCommand editEmployeeCommand = new EditEmployeeCommand(INDEX_FIRST, descriptor);

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();

        EditEmployeeCommand.EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder()
                .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();
        EditEmployeeCommand editEmployeeCommand = new EditEmployeeCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditEmployeeCommand editEmployeeCommand = new EditEmployeeCommand(INDEX_FIRST,
                new EditEmployeeCommand.EditEmployeeDescriptor());

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(editEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();

        EditEmployeeCommand editEmployeeCommand = new EditEmployeeCommand(INDEX_FIRST,
                new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST);
        expectedModel.setPerson(personInFilteredList, editedPerson);

        assertCommandSuccess(editEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());

        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder(firstPerson).build();
        EditEmployeeCommand editCommand = new EditEmployeeCommand(INDEX_SECOND, descriptor);

        assertCommandFailure(editCommand, model, String.format(MESSAGE_DUPLICATE_PERSON, MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST);

        // Edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND.getZeroBased());
        EditEmployeeCommand editCommand = new EditEmployeeCommand(INDEX_FIRST,
                new EditEmployeeDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, String.format(MESSAGE_DUPLICATE_PERSON, MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        EditEmployeeCommand.EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditEmployeeCommand editCommand = new EditEmployeeCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model,
                String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST);

        Index outOfBoundIndex = INDEX_SECOND;

        // Ensures that outOfBoundIndex is still in bounds of address book person list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditEmployeeCommand.EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditEmployeeCommand editCommand = new EditEmployeeCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model,
                String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        final EditEmployeeCommand standardCommand = new EditEmployeeCommand(INDEX_FIRST, DESC_AMY);

        // same values -> returns true
        EditEmployeeCommand.EditEmployeeDescriptor copyDescriptor =
                new EditEmployeeCommand.EditEmployeeDescriptor(DESC_AMY);
        EditEmployeeCommand commandWithSameValues = new EditEmployeeCommand(INDEX_FIRST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditEmployeeCommand(INDEX_SECOND, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditEmployeeCommand(INDEX_FIRST, DESC_BOB)));
    }
}
