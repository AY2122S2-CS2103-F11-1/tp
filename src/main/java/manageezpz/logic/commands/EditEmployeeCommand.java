package manageezpz.logic.commands;

import static java.util.Objects.requireNonNull;
import static manageezpz.commons.core.Messages.MESSAGE_DUPLICATE_PERSON;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static manageezpz.logic.parser.CliSyntax.PREFIX_EMAIL;
import static manageezpz.logic.parser.CliSyntax.PREFIX_NAME;
import static manageezpz.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import manageezpz.commons.core.index.Index;
import manageezpz.commons.util.CollectionUtil;
import manageezpz.logic.commands.exceptions.CommandException;
import manageezpz.model.Model;
import manageezpz.model.person.Email;
import manageezpz.model.person.Name;
import manageezpz.model.person.Person;
import manageezpz.model.person.Phone;
import manageezpz.model.task.Task;

/**
 * Edits the details of an existing employee in the address book.
 */
public class EditEmployeeCommand extends Command {

    public static final String COMMAND_WORD = "editEmployee";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the employee identified by the "
            + "index number used in the displayed employee list.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Update Employee success: %1$s";

    private final Index index;
    private final EditEmployeeDescriptor editEmployeeDescriptor;

    /**
     * Constructor to initialize a EditEmployeeCommand class with the given
     * index and editEmployeeDescriptor.
     *
     * @param index Index of the Employee to edit
     * @param editEmployeeDescriptor Details of the Employee to edit
     */
    public EditEmployeeCommand(Index index, EditEmployeeDescriptor editEmployeeDescriptor) {
        requireNonNull(index);
        requireNonNull(editEmployeeDescriptor);

        this.index = index;
        this.editEmployeeDescriptor = new EditEmployeeDescriptor(editEmployeeDescriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownPersonList = model.getFilteredPersonList();
        List<Person> fullPersonList = model.getAddressBook().getPersonList();
        List<Task> fullTaskList = model.getAddressBook().getTaskList();

        if (index.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Person personToEdit = lastShownPersonList.get(index.getZeroBased());
        Person editedPerson = createEditedEmployee(personToEdit, editEmployeeDescriptor);

        // Check for same person (i.e., name, phone or email already exists)
        for (Person person : fullPersonList) {
            if (!person.equals(personToEdit) && person.isSamePerson(editedPerson)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_PERSON, MESSAGE_USAGE));
            }
        }

        model.setPerson(personToEdit, editedPerson);

        List<Task> affectedTaskList = fullTaskList.stream()
                .filter(task -> task.getAssignees().contains(personToEdit))
                .collect(Collectors.toList());

        for (Task task : affectedTaskList) {
            List<Person> assignees = task.getAssignees();

            for (Person assignee : assignees) {
                if (assignee.equals(personToEdit)) {
                    Task taskToUpdate = fullTaskList.get(fullTaskList.indexOf(task));
                    model.updateTaskWithEditedPerson(taskToUpdate, assignees.indexOf(assignee), editedPerson);
                }
            }
        }

        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editEmployeeDescriptor}.
     */
    private static Person createEditedEmployee(Person personToEdit, EditEmployeeDescriptor editEmployeeDescriptor) {
        assert personToEdit != null;

        Name updatedName = editEmployeeDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editEmployeeDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editEmployeeDescriptor.getEmail().orElse(personToEdit.getEmail());
        int personToEditNumOfTask = personToEdit.getNumOfTasks();

        return new Person(updatedName, updatedPhone, updatedEmail, personToEditNumOfTask);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditEmployeeCommand)) {
            return false;
        }

        // state check
        EditEmployeeCommand e = (EditEmployeeCommand) other;
        return index.equals(e.index)
                && editEmployeeDescriptor.equals(e.editEmployeeDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditEmployeeDescriptor {
        private Name name;
        private Phone phone;
        private Email email;

        public EditEmployeeDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditEmployeeDescriptor(EditEmployeeDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEmployeeDescriptor)) {
                return false;
            }

            // state check
            EditEmployeeDescriptor e = (EditEmployeeDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail());
        }
    }
}
