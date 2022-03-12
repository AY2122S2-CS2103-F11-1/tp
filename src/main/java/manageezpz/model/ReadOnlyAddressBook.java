package manageezpz.model;

import javafx.collections.ObservableList;
import manageezpz.model.person.Person;
import manageezpz.model.task.Task;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the task list.
     * This list will not contain any duplicate task.
     */
    ObservableList<Task> getTaskList();

}
