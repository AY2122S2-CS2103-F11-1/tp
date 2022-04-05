package manageezpz.logic.parser;

import static manageezpz.commons.core.Messages.MESSAGE_FIELD_NOT_EDITED;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT_BIND;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_TIME_RANGE;
import static manageezpz.logic.parser.CliSyntax.PREFIX_AT_DATETIME;
import static manageezpz.logic.parser.CliSyntax.PREFIX_DATE;
import static manageezpz.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import manageezpz.commons.core.index.Index;
import manageezpz.commons.util.CollectionUtil;
import manageezpz.logic.commands.EditEmployeeCommand;
import manageezpz.logic.commands.EditTaskCommand;
import manageezpz.logic.parser.exceptions.ParseException;
import manageezpz.model.task.Date;
import manageezpz.model.task.Description;
import manageezpz.model.task.Time;

/**
 * Parses input arguments and creates a new EditTaskCommand object.
 */
public class EditTaskCommandParser implements Parser<EditTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditTaskCommand
     * and returns a EditTaskCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditTaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DESCRIPTION, PREFIX_DATE, PREFIX_AT_DATETIME);

        // Invalid command if getPreamble() is empty or contains whitespaces
        if (argMultimap.getPreamble().isEmpty() || argMultimap.getPreamble().contains(" ")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT_BIND,
                    EditTaskCommand.MESSAGE_USAGE));
        }

        Index index;

        Description desc = null;
        Date date = null;
        Time deadlineTime = null;
        Time eventStartTime = null;
        Time eventEndTime = null;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());

            if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
                desc = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
            }

            if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
                date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
            }

            if (argMultimap.getValue(PREFIX_AT_DATETIME).isPresent()) {
                String timeInput = argMultimap.getValue(PREFIX_AT_DATETIME).get();
                String[] timeInputParts = timeInput.split(" ");

                System.out.println("Meow");

                if (timeInputParts.length == 2) {
                    eventStartTime = ParserUtil.parseTime(timeInputParts[0]);
                    eventEndTime = ParserUtil.parseTime(timeInputParts[1]);

                    if (eventEndTime.getParsedTime().compareTo(eventStartTime.getParsedTime()) < 1) {
                        throw new ParseException(MESSAGE_INVALID_TIME_RANGE);
                    }
                } else {
                    deadlineTime = ParserUtil.parseTime(timeInputParts[0]);
                }
            }
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage() + "\n\n" + EditTaskCommand.MESSAGE_USAGE, pe);
        }

        if (!CollectionUtil.isAnyNonNull(desc, date, deadlineTime, eventStartTime, eventEndTime)) {
            throw new ParseException(MESSAGE_FIELD_NOT_EDITED + EditEmployeeCommand.MESSAGE_USAGE);
        }

        return new EditTaskCommand(index, desc, date, deadlineTime, eventStartTime, eventEndTime);
    }
}
