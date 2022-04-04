package manageezpz.logic.parser;

import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.DeleteEmployeeCommand;
import manageezpz.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteEmployeeCommand object
 */
public class DeleteEmployeeCommandParser implements Parser<DeleteEmployeeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteEmployeeCommand
     * and returns a DeleteEmployeeCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteEmployeeCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteEmployeeCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage() + "\n\n" + DeleteEmployeeCommand.MESSAGE_USAGE, pe);
        }
    }

}
