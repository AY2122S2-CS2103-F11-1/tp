package manageezpz.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command!";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n\n%1$s";
    public static final String MESSAGE_EMPTY_NAME = "Name field cannot be empty! \n\n%1$s";
    public static final String MESSAGE_EMPTY_PRIORITY = "Priority field cannot be empty! \n\n%1$s";
    public static final String MESSAGE_INVALID_PRIORITY =
            "Invalid priority! " + "Valid priorities are NONE/LOW/MEDIUM/HIGH. \n\n%1$s";
    public static final String MESSAGE_EMPTY_TASK_NUMBER = "Task number field cannot be empty! \n\n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX =
            "The person index provided is invalid as it exceeds the amount of tasks in the displayed list! \n\n%1$s";
    public static final String MESSAGE_INVALID_TASK_DISPLAYED_INDEX =
            "The task index provided is invalid as it exceeds the amount of tasks in the displayed list! \n\n%1$s";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_TASKS_LISTED_OVERVIEW = "%1$d tasks listed!";
    public static final String MESSAGE_TASK_UPDATE_SUCCESS = "Update task success: %1$s";
    public static final String MESSAGE_UNEXPECTED_ERROR = "An unexpected error has occurred!";

    public static final String MESSAGE_DUPLICATE_TASK = "Task with the same description '%1$s' already exists!\n";
    public static final String MESSAGE_INVALID_TIME_RANGE = "The time range you provided is invalid!";
    public static final String MESSAGE_EMPTY_ENDTIME = "The end time cannot be empty!";
}
