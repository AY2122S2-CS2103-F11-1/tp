package manageezpz.storage;

import java.util.List;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.collections.ObservableList;
import manageezpz.commons.exceptions.IllegalValueException;
import manageezpz.model.person.Person;
import manageezpz.model.task.Date;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Description;
import manageezpz.model.task.Event;
import manageezpz.model.task.Priority;
import manageezpz.model.task.Task;
import manageezpz.model.task.Time;
import manageezpz.model.task.Todo;
import manageezpz.model.tasktag.Tag;

/**
 * Jackson-friendly version of {@link Task}.
 */
class JsonAdaptedTask {

    public static final String INVALID_DESCRIPTION_MESSAGE_FORMAT = "Task's description is invalid!";
    public static final String NULL_DESCRIPTION_MESSAGE_FORMAT = "Task's description cannot be null!";
    public static final String INCORRECT_TYPE_MESSAGE_FORMAT = "Task's type is incorrect!";
    public static final String NULL_TYPE_MESSAGE_FORMAT = "Task's type cannot be null!";
    public static final String NULL_STATUS_MESSAGE_FORMAT = "Task's status cannot be null!";
    public static final String INVALID_STATUS_MESSAGE_FORMAT = "Task's status is invalid!";
    public static final String NULL_TAG_MESSAGE_FORMAT = "Task's tag cannot be null!";
    public static final String NULL_PRIORITY_MESSAGE_FORMAT = "Task's priority cannot be null!";
    public static final String INVALID_PRIORITY_MESSAGE_FORMAT = "Task's priority is invalid!";

    public static final String NULL_DEADLINE_DATE_MESSAGE_FORMAT = "Deadline Task's date cannot be null!";
    public static final String INVALID_DEADLINE_DATE_MESSAGE_FORMAT = "Deadline Task's date is invalid!";
    public static final String NULL_DEADLINE_TIME_MESSAGE_FORMAT = "Deadline Task's time cannot be null!";
    public static final String INVALID_DEADLINE_TIME_MESSAGE_FORMAT = "Deadline Task's time is invalid!";

    public static final String NULL_EVENT_DATE_MESSAGE_FORMAT = "Event Task's date cannot be null!";
    public static final String INVALID_EVENT_DATE_MESSAGE_FORMAT = "Event Task's date is invalid!";
    public static final String NULL_EVENT_START_TIME_MESSAGE_FORMAT = "Event Task's start time cannot be null!";
    public static final String INVALID_EVENT_START_TIME_MESSAGE_FORMAT = "Event Task's start time is invalid!";
    public static final String NULL_EVENT_END_TIME_MESSAGE_FORMAT = "Event Task's end time cannot be null!";
    public static final String INVALID_EVENT_END_TIME_MESSAGE_FORMAT = "Event Task's end time is invalid!";

    private final String description;
    private final String type;
    private final String date;
    private String deadlineTime;
    private String eventStartTime;
    private String eventEndTime;
    private String status;
    private String tag;
    private String priority;

    /**
     * Constructs a {@code JsonAdaptedTask} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedTask(@JsonProperty("type") String type, @JsonProperty("status") String status,
                           @JsonProperty("description") String description,
                           @JsonProperty("date") String date, @JsonProperty("deadlineTime") String deadlineTime,
                           @JsonProperty("eventStartTime") String eventStartTime,
                           @JsonProperty("eventEndTime") String eventEndTime,
                           @JsonProperty("tag") String tag,
                           @JsonProperty("priority") String priority) {
        this.description = description;
        this.status = status;
        this.type = type;
        this.date = date;
        this.deadlineTime = deadlineTime;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.tag = tag;
        this.priority = priority;
    }

    /**
     * Converts a given {@code Task} into this class for Jackson use.
     */

    public JsonAdaptedTask(Task source) {
        description = source.getDescription().toString(); // Generally for all tasks
        type = source.getType(); // Generally for all tasks
        status = source.getStatusIcon(); // Generally for all tasks
        this.priority = source.getPriority().name(); // Generally for all tasks
        if (source instanceof Deadline) {
            this.date = ((Deadline) source).getDate().getDate(); // For Deadline
            this.deadlineTime = ((Deadline) source).getTime().getTime(); // For Deadline
        } else if ((source instanceof Event)) {
            this.date = ((Event) source).getDate().getDate(); // For Event
            this.eventStartTime = ((Event) source).getStartTime().getTime(); // For Event
            this.eventEndTime = ((Event) source).getEndTime().getTime(); // For Event
        } else {
            this.date = "";
            this.deadlineTime = "";
            this.eventStartTime = "";
            this.eventEndTime = "";
        }
        List<Person> personList = source.getAssignees();
        StringJoiner joiner = new StringJoiner(", ");
        personList.forEach(item -> joiner.add(item.getName().toString()));
        this.tag = joiner.toString();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Task} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Task toModelType(ObservableList<Person> persons) throws IllegalValueException {
        handleGeneralNullChecks(description, type, status, tag, priority);
        Description desc = new Description(description);
        boolean isDone = status.equals("X");
        if (type.equals("todo")) {
            Todo newTodo = new Todo(desc);
            handleLoad(newTodo, isDone, priority, tag, persons);
            return newTodo;
        } else if (type.equals("deadline")) {
            handleDeadlineNullChecks(date, deadlineTime);
            Date currDeadlineDate = new Date(date);
            Time currDeadlineTime = new Time(deadlineTime);
            Deadline newDeadline = new Deadline(desc, currDeadlineDate, currDeadlineTime);
            handleLoad(newDeadline, isDone, priority, tag, persons);
            return newDeadline;
        } else {
            handleEventNullChecks(date, eventStartTime, eventEndTime);
            Date currEventDate = new Date(date);
            Time currEventStartTime = new Time(eventStartTime);
            Time currEventEndTime = new Time(eventEndTime);
            Event newEvent = new Event(desc, currEventDate, currEventStartTime, currEventEndTime);
            handleLoad(newEvent, isDone, priority, tag, persons);
            return newEvent;
        }
    }

    public void handleLoad(Task task, boolean isDone, String priority,
                               String tag, ObservableList<Person> persons) {
        if (isDone) {
            task.setTaskDone();
        }
        if (priority != null && !priority.isEmpty()) {
            task.setPriority(priority);
        }
        String[] tagList = tag.split(",");
        for (int i = 0; i < tagList.length; i++) {
            String currentTag = tagList[i].trim();
            for (int j = 0; j < persons.size(); j++) {
                Person matchedPerson;
                if (persons.get(j).getName().toString().equals(currentTag)) {
                    matchedPerson = persons.get(j);
                    task.addAssignees(matchedPerson);
                }
            }
        }
    }
    public void handleGeneralNullChecks(String description, String type, String status, String tag, String priority)
            throws IllegalValueException {
        if (description == null) {
            throw new IllegalValueException(String.format(NULL_DESCRIPTION_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }

        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(String.format(INVALID_DESCRIPTION_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }

        if (type == null) {
            throw new IllegalValueException(String.format(NULL_TYPE_MESSAGE_FORMAT, Task.class.getSimpleName()));
        }

        if (!(type.equals("todo") || type.equals("deadline") || type.equals("event"))) {
            throw new IllegalValueException(String.format(INCORRECT_TYPE_MESSAGE_FORMAT, Task.class.getSimpleName()));
        }

        if (status == null) {
            throw new IllegalValueException(String.format(NULL_STATUS_MESSAGE_FORMAT, Task.class.getSimpleName()));
        }

        if (!(status.equals(" ") || status.equals("X"))) {
            throw new IllegalValueException(String.format(INVALID_STATUS_MESSAGE_FORMAT, Task.class.getSimpleName()));
        }

        if (tag == null) {
            throw new IllegalValueException(String.format(NULL_TAG_MESSAGE_FORMAT, Tag.class.getSimpleName()));
        }

        if (priority == null) {
            throw new IllegalValueException(String.format(NULL_PRIORITY_MESSAGE_FORMAT,
                    Priority.class.getSimpleName()));
        }

        if (!(priority.equals("NONE") || priority.equals("LOW")
                || priority.equals("MEDIUM") || priority.equals("HIGH"))) {
            throw new IllegalValueException(String.format(INVALID_PRIORITY_MESSAGE_FORMAT,
                    Priority.class.getSimpleName()));
        }
    }

    public void handleDeadlineNullChecks(String date, String deadlineTime) throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException(String.format(NULL_DEADLINE_DATE_MESSAGE_FORMAT,
                    Date.class.getSimpleName()));
        }

        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(String.format(INVALID_DEADLINE_DATE_MESSAGE_FORMAT,
                    Date.class.getSimpleName()));
        }
        if (deadlineTime == null) {
            throw new IllegalValueException(String.format(NULL_DEADLINE_TIME_MESSAGE_FORMAT,
                    Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(deadlineTime)) {
            throw new IllegalValueException(String.format(INVALID_DEADLINE_TIME_MESSAGE_FORMAT,
                    Time.class.getSimpleName()));
        }
    }

    public void handleEventNullChecks(String date, String eventStartTime, String eventEndTime)
            throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException(String.format(NULL_EVENT_DATE_MESSAGE_FORMAT,
                    Date.class.getSimpleName()));
        }

        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(String.format(INVALID_EVENT_DATE_MESSAGE_FORMAT,
                    Date.class.getSimpleName()));
        }

        if (eventStartTime == null) {
            throw new IllegalValueException(String.format(NULL_EVENT_START_TIME_MESSAGE_FORMAT,
                    Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(eventStartTime)) {
            throw new IllegalValueException(String.format(INVALID_EVENT_START_TIME_MESSAGE_FORMAT,
                    Time.class.getSimpleName()));
        }
        if (eventEndTime == null) {
            throw new IllegalValueException(String.format(NULL_EVENT_END_TIME_MESSAGE_FORMAT,
                    Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(eventEndTime)) {
            throw new IllegalValueException(String.format(INVALID_EVENT_END_TIME_MESSAGE_FORMAT,
                    Time.class.getSimpleName()));
        }
    }
}
