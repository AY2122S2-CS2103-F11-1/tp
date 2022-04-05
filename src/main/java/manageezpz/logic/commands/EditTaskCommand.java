package manageezpz.logic.commands;

import static java.util.Objects.requireNonNull;
import static manageezpz.commons.core.Messages.MESSAGE_DUPLICATE_TASK;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static manageezpz.logic.parser.CliSyntax.PREFIX_AT_DATETIME;
import static manageezpz.logic.parser.CliSyntax.PREFIX_DATE;
import static manageezpz.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import java.util.List;

import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.exceptions.CommandException;
import manageezpz.model.Model;
import manageezpz.model.task.Date;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Description;
import manageezpz.model.task.Event;
import manageezpz.model.task.Task;
import manageezpz.model.task.Time;
import manageezpz.model.task.Todo;
import manageezpz.model.task.exceptions.DuplicateTaskException;

/**
 * Edits the details of an existing task in the address book.
 */
public class EditTaskCommand extends Command {

    public static final String COMMAND_WORD = "editTask";

    public static final String EXAMPLE_ONE = COMMAND_WORD + " 1 " + PREFIX_DESCRIPTION + "Eat bananas";

    public static final String EXAMPLE_TWO = COMMAND_WORD + " 2 " + PREFIX_DESCRIPTION + "Eat Apple "
            + PREFIX_DATE + "2022-09-05 " + PREFIX_AT_DATETIME + "1800";

    public static final String EXAMPLE_THREE = COMMAND_WORD + " 3 " + PREFIX_DESCRIPTION + "Midterm Exam "
            + PREFIX_DATE + "2022-04-06 " + PREFIX_AT_DATETIME + "1800 2000";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the task identified "
            + "by the index number used in the displayed task list.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must exist in the Address Book) "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_DATE + "DATE "
            + PREFIX_AT_DATETIME + "TIME\n"
            + "At least one of " + PREFIX_DESCRIPTION + " " + PREFIX_DATE
            + " " + PREFIX_AT_DATETIME + " must have a value.\n"
            + "For an event task, a start time and an end time "
            + "separated with an empty space must be provided "
            + "instead of a single time value.\n"
            + "Example 1: " + EXAMPLE_ONE + "\n"
            + "Example 2: " + EXAMPLE_TWO + "\n"
            + "Example 3: " + EXAMPLE_THREE;

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Update Task success: %1$s";

    public static final String MESSAGE_TODO_SHOULD_NOT_HAVE_DATETIME =
            "Todo Task should not have date or time! \n\n%1$s";

    public static final String MESSAGE_INVALID_TIME_DETAILS_DEADLINE =
            "Invalid time details for Deadline Task! Deadline Task should only have one time provided!\n\n%1$s";

    public static final String MESSAGE_INVALID_TIME_DETAILS_EVENT =
            "Invalid time details for Event Task! Event Task should have start time and end time!\n\n%1$s";

    public static final String MESSAGE_INVALID_TASK_TYPE = "Task is an invalid Task Type!";

    private final Index index;
    private final Description desc;
    private final Date date;
    private final Time deadlineTime;
    private final Time eventStartTime;
    private final Time eventEndTime;

    /**
     * Constructor to initialize an instance of EditTaskCommand class
     * with the given index and updated description, date and time
     * information.
     *
     * @param index Index of the Task to edit information
     * @param desc New description of the Task
     * @param date New date of the Task
     * @param deadlineTime New time of the Task (for Deadline)
     * @param eventStartTime New start time of the Task (for Event)
     * @param eventEndTime New end time of the Task (for Event)
     */
    public EditTaskCommand(Index index, Description desc, Date date, Time deadlineTime,
                           Time eventStartTime, Time eventEndTime) {
        this.index = index;
        this.desc = desc;
        this.date = date;
        this.deadlineTime = deadlineTime;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_TASK_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Task currentTask = lastShownList.get(index.getZeroBased());
        Task updatedTask;

        try {
            if (currentTask.getType().equalsIgnoreCase("todo")) {
                if (date != null || deadlineTime != null || (eventStartTime != null && eventEndTime != null)) {
                    throw new CommandException(String.format(MESSAGE_TODO_SHOULD_NOT_HAVE_DATETIME, MESSAGE_USAGE));
                }

                updatedTask = updateTodo((Todo) currentTask, this.desc);
            } else if (currentTask.getType().equalsIgnoreCase("deadline")) {
                if (eventStartTime != null && eventEndTime != null) {
                    throw new CommandException(String.format(MESSAGE_INVALID_TIME_DETAILS_DEADLINE, MESSAGE_USAGE));
                }

                updatedTask = updateDeadline((Deadline) currentTask, this.desc, this.date, this.deadlineTime);
            } else if (currentTask.getType().equalsIgnoreCase("event")) {
                if (deadlineTime != null) {
                    throw new CommandException(String.format(MESSAGE_INVALID_TIME_DETAILS_EVENT, MESSAGE_USAGE));
                }

                updatedTask = updateEvent((Event) currentTask, this.desc, this.date,
                        this.eventStartTime, this.eventEndTime);
            } else {
                // Should not reach this as there are only three types of tasks
                throw new CommandException(MESSAGE_INVALID_TASK_TYPE);
            }

            model.setTask(currentTask, updatedTask);
            return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, updatedTask));
        } catch (DuplicateTaskException de) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TASK, this.desc) + MESSAGE_USAGE);
        }
    }

    /**
     * Updates a Todo task.
     *
     * @param currentTask Current Todo task that is to be updated
     * @param desc New description of the Todo Task
     * @return Updated Todo task
     */
    private Task updateTodo(Todo currentTask, Description desc) {
        Todo updatedToDoTask = new Todo(currentTask);

        if (desc != null) {
            updatedToDoTask.setDescription(desc);
        }

        return updatedToDoTask;
    }

    /**
     * Updates a Deadline task.
     *
     * @param currentTask Current Deadline task that is to be updated
     * @param desc New description of the Deadline Task
     * @param date New date of the Deadline Task
     * @param time New time of the Deadline Task
     * @return Updated Todo task
     */
    private Task updateDeadline(Deadline currentTask, Description desc, Date date, Time time) {
        Deadline updatedDeadlineTask = new Deadline(currentTask);

        if (desc != null) {
            updatedDeadlineTask.setDescription(desc);
        }

        if (date != null) {
            updatedDeadlineTask.setDate(date);
        }

        if (time != null) {
            updatedDeadlineTask.setTime(time);
        }

        return updatedDeadlineTask;
    }

    /**
     * Updates an Event task.
     *
     * @param currentTask Current Event task that is to be updated
     * @param desc New description of the Event Task
     * @param date New date of the Event Task
     * @param startTime New start time of the Event Task
     * @param endTime New end time of the Event Task
     * @return Updated Todo task
     */
    private Task updateEvent(Event currentTask, Description desc, Date date, Time startTime, Time endTime) {
        Event updatedEventTask = new Event(currentTask);

        if (desc != null) {
            updatedEventTask.setDescription(desc);
        }

        if (date != null) {
            updatedEventTask.setDate(date);
        }

        if (startTime != null && endTime != null) {
            updatedEventTask.setStartTime(startTime);
            updatedEventTask.setEndTime(endTime);
        }

        return updatedEventTask;
    }
}
