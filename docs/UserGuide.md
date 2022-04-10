---
layout: page
title: User Guide
---

ManageEZPZ is a **desktop app for that allows managers or supervisors to manage employees and assign tasks to them.  Optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI).

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

2. Download the latest `ManageEZPZ.jar` from [here](https://github.com/AY2122S2-CS2103-F11-1/tp/releases).

3. Copy the file to the folder you want to use as the _home folder_ for your ManageEZPZ.

4. Double-click the file to start the app.

5. Start communicating with ManageEZPZ using the command box.

6. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * **`listTask`** : Lists all Tasks.

   * **`addEmployee`**`n/John Doe p/98765432 e/johnd@example.com` : Adds a contact named `John Doe` to ManageEZPZ.

   * **`deleteTask`**`3` : Deletes the 3rd Task shown in the Task list.

   * **`clear`** : Deletes **ALL** data from ManageEZPZ.
   
   * **`addTodo desc/read book`** : Adds a todo task with a description of `read book` to the Task list.

   * **`tagTask 1 n/John Doe`** : Assigns the first task on the task list to an employee named John Doe.

   * **`exit`** : Exits the app.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `addEmployee n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Parameters for adding employees can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* If a parameter is expected only once in the command but you specified it multiple times, only the last occurrence of the parameter will be taken.<br>
  e.g. if you specify `p/12341234 p/56785678`, only `p/56785678` will be taken.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* Task related commands must be strictly lower case.

* Task related parameters must be in sequence as shown in the instruction.

* Parsing parameters of a task is done using the keywords such as `desc/`, `by/` & `at/`, as such, the parsing mechanism would take everything inserted after the keywords.

* All indexes are int based, as such the maximum value is 2147483647 (2<sup>31</sup> - 1).

* Employee `Names` and Task `Description` are case-sensitive. 

</div>

<div style="page-break-after: always;"></div>

### Viewing help : `help`

Shows a message explaining how to access the user guide and copying employee details to Computer Clipboard.

![help message](images/helpMessage_new.png)

Format: `help`

<div style="page-break-after: always;"></div>

### Adding an Employee : `addEmployee`

Adds an employee to ManageEZPZ.

Note:
- Adding a duplicated Employee will result in an error.

Format: `addEmployee n/NAME p/PHONE_NUMBER e/EMAIL`

Examples:
* `addEmployee n/John Doe p/98765432 e/johnd@example.com`
* `addEmployee p/98754123 n/Betsy Crowe e/betsycrowe@example.com`

### Listing all Employees : `listEmployee`

Shows a list of all employees in ManageEZPZ.

Format: `listEmployee`

### Finding Employees by multiple options : `findEmployee`

Finds employee(s) based on multiple conditions provided.

Note:
* Parameters for finding employees can be entered together in any order.
* You must enter at least one parameter.
* Names are case-insensitive 

Format: `findEmployee n/NAMES p/PHONE_NUMBER e/EMAIL`
* `findEmployee n/[LIST OF NAMES]` finds employees whose names contain any of the words in [LIST OF NAMES].
* `findEmployee p/PHONE_NUMBER` finds employees with the exact phone number.
* `findEmployee e/EMAIL` finds employees with the exact email.

Examples:
* `findEmployee n/Alex`
* `findEmployee p/87438807`
* `findEmployee e/alexyeoh@example.com`
* `findEmployee n/Bernice Yu p/99272758 e/berniceyu@example.com`

### Editing an Employee : `editEmployee`

Edits an existing employee in ManageEZPZ.

Format: `editEmployee INDEX n/NAME p/PHONE_NUMBER e/EMAIL`

* Edits the employee at the specified INDEX.
* The index refers to the index number shown in the displayed employee list.
* The index **must be a positive integer** 1, 2, 3, …​
* Existing values will be updated to the input values.
* All tasks that are assigned to the edited employee will be updated to reflect the new changes of the employee.
* No 2 Employees should have the same name, phone number or email.

Examples:
* `editEmployee 1 p/91234567 e/johndoe@example.com` edits the phone number and email address of the 1st employee to be 91234567 and johndoe@example.com respectively. All tasks that are assigned to the 1st employee will be updated to reflect the new changes of the employee.
* `editEmployee 2 n/Betsy Crower` edits the name of the 2nd employee to be Betsy Crower. All tasks that are assigned to the 2nd employee will be updated to reflect the new changes of the employee.

### Deleting an Employee : `deleteEmployee`

Deletes the specified employee from ManageEZPZ.

Format: `deleteEmployee INDEX`

* Deletes the employee at the specified INDEX.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* All tasks that are assigned to the deleted employee will be updated to remove the employee from the respective tasks.

Examples:
* `deleteEmployee 2` deletes the 2nd employee in the displayed employee list. All tasks that are assigned to the 2nd employee will be updated to remove the employee from the respective tasks.
* `listEmployee` followed by `deleteEmployee 2` sets the displayed employee list to show all employees in ManageEZPZ and deletes the 2nd employee in ManageEZPZ. All tasks that are assigned to the 2nd employee will be updated to remove the employee from the respective tasks.
* `findEmployee n/Betsy` followed by `deleteEmployee 1` sets the displayed employee list with the results from the findEmployee command and deletes the 1st employee in the displayed employee list. All tasks that are assigned to the 1st employee will be updated to remove the employee from the respective tasks.

<div style="page-break-after: always;"></div>

### Adding a Task : `addTodo`, `addEvent`, `addDeadline`

Adds a task to ManageEZPZ.

Format:
* `addTodo desc/TASK_DESCRIPTION`
* `addDeadline desc/TASK_DESCRIPTION by/DATE TIME`
* `addEvent desc/TASK_DESCRIPTION at/DATE START_TIME END_TIME`

Examples:
* `addTodo desc/Powerpoint Slides for Company XYZ`
* `addDeadline desc/Client Proposal Slides by/2022-03-20 1800`
* `addEvent desc/Business Meeting at/2022-02-18 1900 2000`

<div markdown="span" class="alert alert-primary">

**:bulb: Take Note:** <br>

For creation of Tasks, ManageEZPZ will allow past deadlines and events to be added for the Managers to track. <br>  
 
For Deadline and Event, the DATE must be in this format: YYYY-MM-DD <br>  

For Deadline and Event, any TIME related fields must be in the format HHmm, where HH should only be between 00 and 23 and mm should only be between 00 and 59. <br><br>
 
For event, the START_TIME must be earlier than the END_TIME. <br><br>

Adding a duplicate Task will result in an error.

</div>

### Listing all Tasks : `listTask`

Shows a list of all tasks in ManageEZPZ.

Format: `listTask`

<div style="page-break-after: always;"></div>

### Finding Tasks by multiple options : `findTask`

Finds task(s) based on multiple conditions provided.

Note:
* Parameters for finding tasks can be entered together in any order.
* You must enter at least one parameter from either Task Type or the valid options.
* Task Type is optional, however, when entered, only one task type is allowed.
* The first option must be valid.
* After the first valid option, any other invalid options that is not stated below will be ignored.

Task Type Available:
* `todo/`: Todos
* `deadline/`: Deadlines
* `event/`: Events

Options:
* `desc/`: Description of the tasks
* `date/`: Date of the task in YYYY-MM-DD (only for deadline and event)
* `priority/`: Priority of task, only `HIGH`, `MEDIUM`, `LOW` and `NONE`
* `assignees/`: The assignees that was assigned to the task (only one full name of assignee allowed)
* `isMarked/`: Whether the task is marked, only `true` or `false`

Format:
* `findTask todo/` finds all todos
* `findTask deadline/` finds all deadlines
* `findTask event/` find all events
* `findTask desc/[LIST OF WORDS]` finds all tasks which contain any of the words in [LIST OF WORDS].
* `findTask date/YYYY-MM-DD` finds all deadlines and events with the date
* `findTask priority/PRIORITY` find all tasks with the given PRIORITY [HIGH, MEDIUM, LOW, NONE]
* `findTask assignees/ASSIGNEE FULL NAME` finds all tasks assigned to the stated assignee (in full name)
* `findTask isMarked/true` finds all tasks that is already marked as done.
* `findTask isMarked/false` finds all tasks that is already marked as not done.

<div style="page-break-after: always;"></div>

Example:
* `findTask desc/homework`
* `findTask date/2022-04-16`
* `findTask desc/work priority/HIGH`
* `findTask deadline/ desc/school date/2022-04-16 priority/HIGH assignees/Alex Yeo isMarked/true`
    * Finds the task with a description that contains all the following options:
        * Task type of deadline,
        * description which contains the word “school”,
        * date 2022-04-16,
        * priority high,
        * assigned to Alex Yeoh,
        * and is marked as done.
        
### Editing a Task : `editTask`

Edits an existing task in ManageEZPZ.

Formats:
* `editTask INDEX desc/NAME`
* `editTask INDEX desc/NAME date/DATE`
* `editTask INDEX desc/NAME date/DATE at/TIME`
* `editTask INDEX date/DATE`
* `editTask INDEX date/DATE at/TIME`
* `editTask INDEX at/TIME`

Editing tasks is flexible in ManageEZPZ.
For example, you can update just the task description or perhaps
just the date and time of the task only. <br/>
However, you are not allowed to edit a task with no prefix supplied or if you have supplied a prefix, 
a corresponding input after the prefix must exist. <br/>
Either one of `desc/NAME`, `date/DATE` or `at/TIME` must exist.

<b>Note:</b> 
* For Deadline and Event, any TIME related fields must be in the format HHmm, where HH should only be between 00 and 23
  and mm should only be between 00 and 59.
* For Todo, you are not allowed to use `date/DATE` and/or `at/TIME` as it does not have a date 
and time field to be edited. 
* You can update a task to the same description, date and/or time.  

<div style="page-break-after: always;"></div>

Examples: <br/>

Given a task list as follows... <br/>

1. Type: `Todo`, Description: `Eat Bread`
2. Type: `Deadline`, Description: `Chemistry Homework`, Date: `2022-05-03`, Time: `1700`
3. Type: `Event`, Description: `Final Exam`, Date: `2022-06-04`, Time: `1700 2000`

* `editTask 1 desc/Drink Water` edits the task description of a `Todo` task.
* `editTask 2 date/2022-05-10 at/2000` edits the date and the time of a `Deadline` task.
* `editTask 3 at/1800 2100` edits the time of an `Event` task.

<div markdown="span" class="alert alert-primary">

**:bulb: Take Note:** <br>

For Deadline and Event, the DATE must be in this format: YYYY-MM-DD <br><br>
 
For Deadline and Event, any TIME related fields must be in the format HHmm, where HH should only be between 00 and 23 and mm should only be between 00 and 59. <br><br>

For Event, the START_TIME must be earlier than the END_TIME. <br><br>

Editing a Task to another already existing task will result in an error.

</div>

### Marking a Task : `markTask`

Marks the specified task in ManageEZPZ as done.

Format: `markTask INDEX`

* Marks the task at the specified `INDEX` as done.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, …​
* Marking a marked task (i.e., that is already set as done) will not change its physical state.

Examples:
* `markTask 2` marks the 2nd task in the displayed task list as done.
* `listTask` followed by `markTask 2` sets the displayed task list to show all tasks in ManageEZPZ and marks the 2nd task in ManageEZPZ as done.
* `findTask desc/slides` followed by `markTask 1` sets the displayed task list with the results from the findTask command and marks the 1st task in the displayed task list as done.

<div style="page-break-after: always;"></div>

### Unmarking a Task : `unmarkTask`

Unmarks the specified task in ManageEZPZ, i.e., changes the status back to not done.

Format: `unmarkTask INDEX`

* Unmarks the task at the specified `INDEX` to change the status back to not done.
* The index refers to the index number shown in the displayed Task list.
* The index **must be a positive integer** 1, 2, 3, …​
* Unmarking an unmarked task (i.e., that is already set as not done) will not change its physical state.

Examples:
* `unmarkTask 2` changes the 2nd task in the displayed task list back to not done.
* `listTask` followed by `unmarkTask 2` sets the displayed task list to show all tasks in ManageEZPZ and changes the 2nd task in ManageEZPZ back to not done.
* `findTask desc/slides` followed by `unmarkTask 1` sets the displayed task list with the results from the findTask command and changes the 1st task in the displayed task list back to not done.

<div style="page-break-after: always;"></div>

### Deleting a Task : `deleteTask`

Deletes the specified task from ManageEZPZ.

Format: `deleteTask INDEX`

* Deletes the task at the specified `INDEX`.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, …​
* The number of assigned tasks of the employees who were assigned to the deleted task will be decreased by 1.

Examples:
* `deleteTask 2` deletes the 2nd task in the displayed task list. The number of assigned tasks of the employees who were assigned to the deleted task will be decreased by 1.
* `listTask` followed by `deleteTask 2` sets the displayed task list to show all tasks in ManageEZPZ and deletes the 2nd task in ManageEZPZ. The number of assigned tasks of the employees who were assigned to the deleted task will be decreased by 1.
* `findTask desc/slides` followed by `deleteTask 1` sets the displayed task list with the results from the findTask command and deletes the 1st task in the displayed task list. The number of assigned tasks of the employees who were assigned to the deleted task will be decreased by 1.

### Tagging a Task to an Employee : `tagTask`

Assigns the specified task to an employee.

Format: `tagTask INDEX n/NAME`
* Assigns the task at the specified `INDEX` to the employee with the specified `NAME`.
* The index refers to the index number shown in the current displayed task list.
* The index **must be a positive integer** 1, 2, 3, …​
* The employee you are tagging **must** be in the current displayed employees list.
* The name must be a valid employee **full name** in ManageEZPZ.

Example: `tagTask 1 n/Alex Yeoh`
* `tagTask 1 n/Alex Yeoh` assigns the 1st task in the displayed task list to the employee with the name Alex Yeoh.
* `listTask` followed by `tagTask 1 n/Alex Yeoh` sets the displayed task list to show all tasks in ManageEZPZ and assigns the 1st task in ManageEZPZ to the employee with the name Alex Yeoh.
* `findTask desc/slides` followed by `tagTask 1 n/Alex Yeoh` sets the displayed task list with the results from the findTask command and assigns the 1st task in the displayed task list to the employee with the name Alex Yeoh.

### Untagging a Task from an Employee : `untagTask`

Deallocates the specified task from an employee.

Format: `untagTask INDEX n/NAME`
* Deallocates the task at the specified `INDEX` from the employee with the specified `NAME`.
* The index refers to the index number shown in the current displayed task list.
* The index **must be a positive integer** 1, 2, 3, …​
* The employee you are tagging **must** be in the current displayed employees list.
* The name must be a valid employee **full name** in ManageEZPZ.

Example:
* `untagTask 1 n/Alex Yeoh` deallocates the 1st task in the displayed task list from the employee with the name Alex Yeoh.
* `listTask` followed by `untagTask 1 n/Alex Yeoh` sets the displayed task list to show all tasks in ManageEZPZ and deallocates the 1st task in ManageEZPZ from the employee with the name Alex Yeoh.
* `findTask desc/slides` followed by `untagTask 1 n/Alex Yeoh` sets the displayed task list with the results from the findTask command and deallocates the 1st task in the displayed task list from the employee with the name Alex Yeoh.

### Tagging a Priority to a Task : `tagPriority`

Assigns the specified task with a priority of either HIGH, MEDIUM, LOW or NONE.

Format: `tagPriority INDEX priority/PRIORITY`
* Assigns the task at the specified `INDEX` with the specified priority `PRIORITY`.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, …​
* The priority must be either `HIGH`, `MEDIUM`, `LOW`, or `NONE`.
* The priority is case-insensitive, e.g., `high`, `HIGH`, `HiGh` or `hIgH` will match as `HIGH`.
* A task with the priority of `NONE` will not have the priority reflected in the displayed task list.
* Tagging the same priority to the Task, will not change its physical state.

Example: 
* `tagPriority 1 priority/HIGH`
* `tagPriority 1 priority/HIGH` assigns the 1st task in the displayed task list with the priority of `HIGH`.
* `listTask` followed by `tagPriority 1 priority/HIGH` sets the displayed task list to show all tasks in ManageEZPZ and assigns the 1st task in ManageEZPZ with the priority of `HIGH`.
* `findTask desc/slides` followed by `tagPriority 1 priority/HIGH` sets the displayed task list with the results from the findTask command and assigns the 1st task in the displayed task list with the priority of `HIGH`.

### Clearing all entries : `clear`

Deletes all entries from the employee list and task list.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

ManageEZPZ data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

ManageEZPZ data are saved as a JSON file `[JAR file location]/data/ManageEZPZ.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">

:exclamation: **Caution:**
If your changes to the data file makes its format invalid, ManageEZPZ will discard all data and start with an empty data file at the next run.

</div>

--------------------------------------------------------------------------------------------------------------------
<div style="page-break-after: always;"></div>

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous ManageEZPZ home folder.

--------------------------------------------------------------------------------------------------------------------

## Command summary

### Employee Related Commands

| Action              | Format, Examples                                                                                                 |
|---------------------|------------------------------------------------------------------------------------------------------------------|
| **Add Employee**    | `addEmployee n/NAME p/PHONE_NUMBER e/EMAIL` <br> e.g., `addEmployee n/James Ho p/22224444 e/jamesho@example.com` |
| **List Employees**  | `listEmployee`                                                                                                   |
| **Find Employee**   | `findEmployee OPTIONS` <br> e.g. `findEmployee n/Alex Yeoh`                                                      |
| **Edit Employee**   | `editEmployee INDEX n/NAME p/PHONE_NUMBER e/EMAIL` <br> e.g., `edit 2 n/James Lee e/jameslee@example.com`        |
| **Delete Employee** | `deleteEmployee INDEX` <br> e.g., `deleteEmployee 3`                                                             |

<div style="page-break-after: always;"></div>

### Task Related Commands

| Action                | Format, Examples                                                                                                                |
|-----------------------|---------------------------------------------------------------------------------------------------------------------------------|
| **Add Todo Task**     | `addTodo desc/TASK_DESCRIPTION` <br> e.g., `addTodo desc/read book`                                                             |
| **Add Deadline Task** | `addDeadline desc/TASK_DESCRIPTION by/DATETIME` <br> e.g., `addDeadline desc/return book by/2022-02-16 1800`                    |
| **Add Event Task**    | `addEvent desc/TASK_DESCRIPTION at/DATE START_TIME END_TIME` <br> e.g., `addEvent desc/project meeting at/2022-02-17 1900 2000` 
  **Edit Task**         | `editTask INDEX desc/TASK_DESCRIPTION date/DATE at/TIME` <br> e.g., `editTask 3 desc/homework deadline date/2022-03-15 at/1700`
| **List Tasks**        | `listTasks`                                                                                                                     |
| **Find Task**         | `findTask OPTIONS` <br> e.g.,`findTask todo/`                                                                                   |
| **Mark Task**         | `markTask INDEX` <br> e.g., `markTask 2`                                                                                        |
| **Unmark Task**       | `unmarkTask INDEX` <br> e.g., `unmarkTask 2`                                                                                    |
| **Delete Task**       | `deleteTask INDEX` <br> e.g., `deleteTask 2`                                                                                    |
| **Tag Task**          | `tagTask INDEX n/NAME` <br> e.g.,`tagTask 1 n/Alex Yeoh`                                                                        |
| **Untag Task**        | `untagTask INDEX n/NAME` <br> e.g.,`untagTask 1 n/Alex Yeoh`                                                                    |
| **Tag Priority**      | `tagPriority INDEX priority/PRIORITY` <br> e.g.,`tagPriority 1 priority/HIGH`                                                   |

<div style="page-break-after: always;"></div>

### Others

| Action    | Format  |
|-----------|---------|
| **Clear** | `clear` |
| **Help**  | `help`  |
| **Exit**  | `exit`  |
