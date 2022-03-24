---
layout: page
title: Developer Guide
---
* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

### \[Proposed\] Editing details of a task

The proposed edit mechanism is facilitated by `EditTaskCommandParser` and `EditTaskCommand`. <br/>
`EditTaskCommandParser.parse()` - parses the input by the user and returns a `EditTaskCommand` object. <br/>
`EditTaskCommand.execute()` - creates a new `Task` object based on the parsed user input and calls `Model.setTask()` 
to replace the old `Task` object with the new `Task` object.

The command is as follows: taskEdit [TASK_INDEX] desc/ [DESC] at/ [TIME] date/ [DATE]

- [TASK_INDEX] must not be empty.
- At least one of [DESC], [TIME] and [DATE] should not be empty.
- desc/ [DESC], at/ [TIME] or date/ [DATE] may be omitted but all of them cannot be omitted at the same time.


Below is a sequence diagram of the editing of a task.

![EditTaskSequenceDiagram](images/EditTaskSequenceDiagram.png)

Step 1. The user executes a `editTask 1 desc/ eat` to edit the 1st task in the address book. The `editTask` command
in `AddressBookParser` calls `EditTaskCommandParser.parse()` 
and parses the task index which is given as 1 and the task description that the
user wants to edit his task with is parsed as a String with value eat. 

If either the description or the date or time was not provided by the user, then the default value
would be an empty String. Otherwise, it would be parsed as a String with value provided by the user.
It is not the responsibility of `EditTaskCommandParser.parse()` to ensure that the input provided by the user is valid.
It will however ensure that a `Task` index is provided and 
at least the description or date or time has a corresponding value.

Step 2.  
In `LogicManager`, `EditTaskCommand.execute()` is called. This `EditTaskCommand` object is the one that was returned
by `EditTaskCommandParser.parse()`. Within the `execute()` method,  there are 3 cases to consider.

1. The task is of type Todo.
2. The task is of type Deadline.
3. The task is of type Event.

Each case is being handled separately by its corresponding handler method. 

In general, what each handler method will do is to ensure that the input provided 
by the user is valid and if so, create a new `Task` object with the given input and 
call `model.setTask()` to replace the old `Task` object 
with the new `Task` object. If there are some input which is not provided, then the default value would be the
same value as the old `Task` object. If any input is not valid, a `ParseException` is thrown.

For `editTask 1 desc/ eat`, in step 1, we have obtained the task index which is given as 1. 
Using `model.getFilteredTaskList().get(index)` we obtain a copy of the task that the user wants to edit. 
Next, depending on what type the obtained task is, the corresponding handler method is called.

If the task is of type Todo, then the handler method will create a new `Todo` object with the description value as
"eat", call `model.setTask()` and return a CommandResult showing that the update has been successful.

If the task is of type Deadline, then the handler method will create a new `Deadline` object with the description 
value as "eat", and the date and time values set to be the same 
values from the copy obtained using `model.getFilteredTaskList().get(index)`. Then, `model.setTask()` is called and
return a CommandResult showing that the update has been successful.

If the task is of type Event, then the handler method will create a new `Event` object with the description
value as "eat", and the date and time values set to be the same
value from the copy obtained using `model.getFilteredTaskList().get(index)`. Then, `model.setTask()` is called and
return a CommandResult showing that the update has been successful. 

<b>Note:</b> For the Event type, a String value with two time values corresponding to be the start and end time 
separated with an **empty space** must be provided. Other than the time values being valid, 
the range between the start and end time must be valid as well. For example, 1700 2000 is valid while 2000 1700 is not.   

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Company Managers/Supervisors wants to keep track of all the tasks given to their subordinates
* They need to assign tasks to their subordinates as well
* Prefers typing commands instead of clicking buttons
* Needs a local database to store all tasks

**Value proposition**: 

* An application to show all the tasks assigned to the employees
* Tasks should be assigned to the employees as well
* Commands are typed using command lines
* All tasks created are stored in the local database

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​        | I want to …​                                                               | So that I can…​                                                                                   |
|----------|----------------|----------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `* * *`  | user           | add a task to the database                                                 | better organise my time                                                                           |
| `* * *`  | user           | delete a task from the database                                            | better organise my list                                                                           |
| `* * *`  | user           | view all my tasks                                                          | have a better picture of my schedule                                                              |
| `* * *`  | user           | able to edit a task                                                        | update any details                                                                                |
| `* * *`  | user           | able to view my tasks for the day (i.e. today)                             | better manage my time                                                                             |
| `* * *`  | user           | able to view the tasks for the week                                        | have a better picture of my schedule for the week                                                 |
| `* * *`  | user           | view the tasks on a specific day                                           | plan for that day ahead                                                                           |
| `* *`    | CEO or manager | have the flexibility to reschedule tasks that are assigned to any employee | better manage the manpower and deadlines                                                          |
| `* *`    | manager        | retrieve the list of tasks allocated with an employee                      | allow myself to have an overview of the employee's workload. For example, command: ``Track <name> |
| `* *`    | new user       | have a more begineer-friendly user guide                                   | learn more about the product                                                                      |
| `* *`    | recurring user | be able to see tasks that are due within X number of days                  | better manage my time                                                                             |
| `* *`    | advance user   | able to sort tasks based on a specific location                            | better plan my travel to that location                                                            |


### Use cases

> Definition:
> - For all use cases below, the **System** is `ManageEZPZ` and the **Actor** is the `User`, unless specified otherwise.
> - More specifically, the `User` are **Supervisors**.

> Guarantees:
> - For any use cases below that changes any data, ManageEZPZ will guarantee that the data is updated and saved.
****

**Use Case 1 - Add Task**

**MSS**

1. User starts up ManageEZPZ
2. ManageEZPZ greets User with our HELP page, with all the commands.
3. User uses the appropriate command to add Task.
4. ManageEZPZ adds the task & confirms with a successful message that the task is added.

    Use case ends.

**Extensions**

* 3a. User uses one of the three `addTask` commands: 
    * 3a1. User uses `addTask /todo` command 

      Use case resumes from step 4. 

    * 3a2. User uses `addTask /event` command 

      Use case resumes from step 4. 

    * 3a3. User uses `addTask /deadline` command 

      Use case resumes from step 4. 
    
* 3b. User uses Add Task Commands with the wrong syntax 

    * 3b1. ManageEZPZ sends an error message to User, indicating the
      format for adding Task is incorrect, attached with the correct syntax format. 

      Use case ends.

****

**Use Case 2 - Delete Task**

**MSS**

1. User starts up ManageEZPZ
2. ManageEZPZ greets User with our HELP page, with all the commands.
3. User uses the appropriate command to delete a Task
4. ManageEZPZ deletes the Task & confirms with a successful message that the Task is deleted. 

   Use case ends.

**Extensions**

* 3a. ManageEZPZ detects an error in the entered data. (Invalid index) 
    * 3a1. ManageEZPZ sends an error message to User, indicating the Index used for the delete
      command is incorrect, attached with the correct syntax format. 

      Use case ends.

****

**Use Case 3 - List Tasks**

**MSS**

1. User starts up ManageEZPZ
2. ManageEZPZ greets User with our HELP page, with all the commands.
3. User enters the command to list Tasks.
4. ManageEZPZ displays the Tasks according.

   Use case ends.

**Extensions**

* 3a. User uses one of the five `list` Task commands: 
    * 3a1. User uses `list /all` command 

      Use case resumes from step 4. 

    * 3a2. User uses `list /todo` command 

      Use case resumes from step 4. 

    * 3a3. User uses `list /deadline` command 

      Use case resumes from step 4. 

    * 3a2. User uses `list /event` command 

      Use case resumes from step 4. 

    * 3a3. User uses `list /today` command 

      Use case resumes from step 4. 
    
* 3b. User uses list Task commands with the wrong syntax. 
    * 3b1. ManageEZPZ sends an error message to User, that the list
      command is incorrect, attached with the correct syntax format. 

      Use case ends.

****

**Use Case 4 - Mark Tasks**

Preconditions: User is currently using ManageEZPZ.

**MSS**

1. User enters the command to view Tasks.
2. ManageEZPZ displays the Tasks.
3. User wants to mark a Task as finished, enters command to mark Task.
4. ManageEZPZ marks the Task & confirms with a successful message that the task is marked

   Use case ends.

**Extensions**

* 3a. ManageEZPZ detects an error in the entered data. (Invalid Index) 

    * 3a1. ManageEZPZ sends an error message to User, indicating the Index used for
      the Mark command is incorrect, attached with the correct syntax format. 
      
      Use Case ends.

****

**Use Case 5 - Unmark Tasks**

Preconditions: User is currently using ManageEZPZ.

**MSS**

1. User enters the command to view Tasks.
2. ManageEZPZ displays the Tasks.
3. User realises that Task is marked as done, but is actually not done.
4. User enters command to unmark Task for the specific Task Number.
5. ManageEZPZ unmarks the Task & confirms with a successful message that the task is unmarked. 

   Use case ends.

**Extensions**

* 4a. ManageEZPZ detects an error in the entered data. (Invalid Index) 

    * 4a1. ManageEZPZ sends an error message to User, indicating the Index used for
      the unmark command is incorrect, attached with the correct syntax format. 
      
      Use Case ends.

****

**Use Case 6 - Find Tasks**

**MSS**

1. User starts up ManageEZPZ
2. ManageEZPZ greets User with our HELP page, with all the commands.
3. User enters the command to find Tasks.
4. ManageEZPZ displays the Task(s) which matches the search keyword. 

   Use case ends.

**Extensions**

* 3a. User uses one of the two Find Task commands: 

    * 3a1. User uses `find /task TASK_DESCRIPTION` command 

      Use case resumes from step 4. 

    * 3a2. User uses `find /date DD-MM-YYYY` command 

      Use case resumes from step 4. 

* 3b. User uses find Task commands with the wrong syntax 

    * 3b1. ManageEZPZ sends an error message to User, indicating syntax used for
      the find Task command is incorrect, attached with the correct syntax format. 
      
      Use Case ends.

****

**Use Case 7 - Add Employee**

**MSS**
1. User starts up ManageEZPZ.
2. ManageEZPZ greets User with our HELP page, with all the commands.
3. User wants to add a new Employee, enters command to add Employee.
4. ManageEZPZ adds the Employee & confirms with a successful message that the task is marked 

   Use case ends.

**Extensions**

* 3a. ManageEZPZ detects an error in the entered data. 

    * 3a1. ManageEZPZ sends an error message to User, indicating the
      format for the add Employee command is incorrect, attached with the
      correct syntax format. 
      
      Use Case ends.

****

**Use Case 8 - Clear all Tasks**

**MSS**

1. User enters the command to clear all Tasks
2. ManageEZPZ clears all Tasks & confirms with a successful 
message that all Task are cleared. 

   Use case ends.

****

**Use Case 9 - Exit ManageEZPZ**

**MSS**

1. User enters a command to exit ManageEZPZ.
2. ManageEZPZ confirms with a successful exit message.
3. ManageEZPZ saves all changes to disk. 

   Use case ends.

****
*{More to be added}*
****
### Non-Functional Requirements

#### Technical Requirements

1. ManageEZPZ should be able to run on any _mainstream OS_ as long as it has Java `11` or above installed.
2. ManageEZPZ should work on both 32-bit and 64-bit environments.
3. ManageEZPZ should be able to store and retrieve task data from File.
4. ManageEZPZ must occupy as little storage as possible.
5. ManageEZPZ should be backward compatible with data produced by earlier versions of itself.

#### Performance Requirements

1. ManageEZPZ should respond within two seconds for any queries.
   * ManageEZPZ should be closed/terminated within 2 seconds.
2. ManageEZPZ should work well under both normal and high workloads. 
3. ManageEZPZ should be scalable.
4. ManageEZPZ should be able to load huge amounts of data in a short amount of time.

#### Quality Requirements

1. ManageEZPZ should be easy to use by a novice.
2. ManageEZPZ should be in English.
3. The UI and fonts used in ManageEZPZ should be big enough for senior managers/supervisors.

#### Process Requirements

1. ManageEZPZ is expected to adhere to a schedule that delivers a feature set every 2 weeks.
2. Updates to ManageEZPZ should be able to roll out to existing clients remotely.

#### Other Noteworthy Points

1. ManageEZPZ should not be used to support management of illegal activities

### Glossary

| Terms             | Meaning                                                                     |
|:------------------|:----------------------------------------------------------------------------|
| **Mainstream OS** | Windows, Linux, Unix, OS-X                                                  |
| **Users**         | Applies to both supervisors & employees                                     |
| **command**       | A message sent as an input from User, that coincides with our Command List  |
| **todo**          | A task that only needs a description with no dates or times                 |
| **deadline**      | A task that needs a description, a date, and a end time                     |
| **event**         | A task that needs a description, a date, start and end times                |
| **mark**          | To indicate that Task is done                                               |
| **unmark**        | To indicate that Task is not done                                           |

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
