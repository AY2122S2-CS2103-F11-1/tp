package manageezpz.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import manageezpz.commons.exceptions.IllegalValueException;
import manageezpz.model.AddressBook;
import manageezpz.model.ReadOnlyAddressBook;
import manageezpz.model.person.Person;
import manageezpz.model.task.Task;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_TASKS = "Tasks list contains duplicate task(s).";
    public static final String MESSAGE_INVALID_JSON_VALUE = "Fields in JSON files are incorrect.";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedTask> tasks = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("tasks") List<JsonAdaptedTask> tasks) {
        this.persons.addAll(persons);
        this.tasks.addAll(tasks);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        tasks.addAll(source.getTaskList().stream().map(JsonAdaptedTask::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        HashMap<Person, Integer> hm = new HashMap<>();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
            hm.put(person, 0);
        }

        for (JsonAdaptedTask jsonAdaptedTask : tasks) {
            Task task = jsonAdaptedTask.toModelType(addressBook.getPersonList());
            if (addressBook.hasTask(task)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_TASKS);
            }
            addressBook.addTask(task);
        }

        handleNumOfTask(addressBook.getTaskList(), addressBook.getPersonList(), hm);

        return addressBook;
    }


    public void handleNumOfTask(List<Task> taskList, List<Person> personList, HashMap<Person, Integer> hm)
            throws IllegalValueException {
        for (Task task : taskList) {
            List<Person> assigneesList = task.getAssignees();
            for (int i = 0; i < assigneesList.size(); i++) {
                Person currentPerson = assigneesList.get(i);
                if (hm.containsKey(currentPerson)) {
                    hm.put(currentPerson, hm.get(currentPerson) + 1);
                }
            }
        }

        for (Person person : personList) {
            int numOfTask = hm.get(person);
            if (numOfTask != person.getNumOfTasks()) {
                throw new IllegalValueException(MESSAGE_INVALID_JSON_VALUE);
            }
        }
    }
}
