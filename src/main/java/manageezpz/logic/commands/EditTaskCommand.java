package manageezpz.logic.commands;

import static manageezpz.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_TIME_RANGE;
import static manageezpz.commons.core.Messages.MESSAGE_TASK_UPDATE_SUCCESS;
import static manageezpz.commons.core.Messages.MESSAGE_UNEXPECTED_ERROR;
import static manageezpz.logic.parser.CliSyntax.PREFIX_DATE;
import static manageezpz.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static manageezpz.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.ArrayList;

import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.exceptions.CommandException;
import manageezpz.logic.parser.ParserUtil;
import manageezpz.logic.parser.exceptions.ParseException;
import manageezpz.model.Model;
import manageezpz.model.task.Date;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Description;
import manageezpz.model.task.Event;
import manageezpz.model.task.Task;
import manageezpz.model.task.Time;
import manageezpz.model.task.Todo;

/**
 * Edits the details of an existing task in the address book.
 */

public class EditTaskCommand extends Command {
    public static final String COMMAND_WORD = "editTask";
    public static final String MORE_EXAMPLES = COMMAND_WORD + " 1 " + PREFIX_DESCRIPTION + "Eat Apple "
            + PREFIX_DATE + "2022-09-05 " + PREFIX_TIME + "1800";
    public static final String MORE_EXAMPLES_1 = COMMAND_WORD + " 2 " + PREFIX_DESCRIPTION + "Midterm Exam "
            + PREFIX_DATE + "2022-04-06 " + PREFIX_TIME + "1800 2000";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
            + "by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must exist in the Address Book) "
            + "[" + PREFIX_DESCRIPTION + " NAME]"
            + "[" + PREFIX_DATE + " DATE]"
            + "[" + PREFIX_TIME + " TIME] \n"
            + "At least one of " + PREFIX_DESCRIPTION + " "
            + PREFIX_DATE + " " + PREFIX_TIME + " must have a value. \n"
            + "For an event task, a start time and an end time" +
            " separated with an empty space" + "\n must be provided" +
            " instead of a single time value. \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DESCRIPTION + " Eat bananas "
            + "\n"
            + MORE_EXAMPLES
            + "\n"
            + MORE_EXAMPLES_1;

    private final Index index;
    private final String desc;
    private final String time;
    private final String date;

    /**
     * A constructor to store information that is used in editing a task.
     */

    public EditTaskCommand(Index index, String desc, String time, String date) {
        this.index = index;
        this.desc = desc;
        this.time = time;
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            Task currentTask = model.getFilteredTaskList().get(index.getZeroBased());
            if (currentTask.getType().equalsIgnoreCase("todo")) {
                return handleTodo((Todo) currentTask, model, this.desc);
            } else if (currentTask.getType().equalsIgnoreCase("deadline")) {
                return handleDeadline((Deadline) currentTask, model, this.desc, this.date, this.time);
            } else if (currentTask.getType().equalsIgnoreCase("event")) {
                return handleEvent((Event) currentTask, model, this.desc, this.date, this.time);
            } else {
                throw new CommandException(MESSAGE_UNEXPECTED_ERROR);
            }
        } catch (Exception e) {
            throw new CommandException(MESSAGE_USAGE);
        }
    }

    private ArrayList<Time> handleEventStartAndEndTime(String start, String end) throws ParseException {
        Time startTime = ParserUtil.parseTime(start);
        Time endTime = ParserUtil.parseTime(end);
        ArrayList<Time> startEndTimeStorageArr = new ArrayList<>();
        if (endTime.getParsedTime().compareTo(startTime.getParsedTime()) < 0) {
            throw new ParseException(MESSAGE_INVALID_TIME_RANGE);
        } else {
            startEndTimeStorageArr.add(startTime);
            startEndTimeStorageArr.add(endTime);
            return startEndTimeStorageArr;
        }
    }

    private CommandResult handleTodo(Todo currentTask, Model model, String desc) throws ParseException {
        try {
            Description taskDesc = ParserUtil.parseDescription(desc);
            Todo newTask = new Todo(taskDesc);
            model.setTask(currentTask, newTask);
            return new CommandResult(String.format(MESSAGE_TASK_UPDATE_SUCCESS, newTask));
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTaskCommand.MESSAGE_USAGE), pe);
        }
    }

    private CommandResult handleDeadline(Deadline currentTask, Model model, String desc, String date, String time)
            throws ParseException {

        Description dlDescription;
        Time dlTime;
        Date dlDate;

        if (desc.isEmpty()) {
            dlDescription = currentTask.getDescription();
        } else {
            dlDescription = ParserUtil.parseDescription(desc);
        }

        if (time.isEmpty()) {
            dlTime = currentTask.getTime();
        } else {
            dlTime = ParserUtil.parseTime(time);
        }

        if (date.isEmpty()) {
            dlDate = currentTask.getDate();
        } else {
            dlDate = ParserUtil.parseDate(date);
        }

        Deadline newTask = new Deadline(dlDescription, dlDate, dlTime);
        model.setTask(currentTask, newTask);
        return new CommandResult(String.format(MESSAGE_TASK_UPDATE_SUCCESS, newTask));
    }

    private CommandResult handleEvent(Event currentTask, Model model, String desc, String date, String time)
            throws ParseException {

        Description eventDescription;
        Time startTime;
        Time endTime;
        Date eventDate;

        if (desc.isEmpty()) {
            eventDescription = currentTask.getDescription();
        } else {
            eventDescription = ParserUtil.parseDescription(desc);
        }

        if (time.isEmpty()) {
            startTime = currentTask.getStartTime();
            endTime = currentTask.getEndTime();
        } else {
            String[] splitStartEnd = time.split(" ");
            ArrayList<Time> timeArr = handleEventStartAndEndTime(splitStartEnd[0], splitStartEnd[1]);
            startTime = timeArr.get(0);
            endTime = timeArr.get(1);
        }

        if (date.isEmpty()) {
            eventDate = currentTask.getDate();
        } else {
            eventDate = ParserUtil.parseDate(date);
        }

        Event newTask = new Event(eventDescription, eventDate, startTime, endTime);
        model.setTask(currentTask, newTask);
        return new CommandResult(String.format(MESSAGE_TASK_UPDATE_SUCCESS, newTask));
    }
}
