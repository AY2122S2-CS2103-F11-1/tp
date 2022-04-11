---
layout: page
title: Tay Jun Yang's Project Portfolio Page
---

## Project: ManageEZPZ

ManageEZPZ is a desktop application that allows managers or supervisors to manage employees and assign tasks to them. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java.

Given below are my contributions to the project.

* **New Features:**
  * Implemented `markTask` feature, which marks the task identified by the index number used in the displayed task list as done. ([#87](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/87), [#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170))
  * Implemented `unmarkTask` feature, which unmarks the task identified by the index number used in the displayed task list, which changes the status back to not done. ([#87](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/87), [#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170))
  * Implemented `deleteTask` feature, which deletes the task identified by the index number used in the displayed task list. The number of assigned tasks of the employees who were assigned to the deleted task will then be decreased by 1. ([#89](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/89), [#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170), [#259](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/259))
  * Implemented Copying Employee Details to Computer Clipboard feature, which enables the user to right-click on an employee to copy the full name, phone number and email to Computer Clipboard. ([#181](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/181))
    * Justification: This feature allows the user to tag a task to an employee or untag a task from an employee easily by pasting the copied full name of the employee into the command text box.


* **Enhancements to Graphical User Interface (GUI) of Main Window:** ([#123](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/123), [#142](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/142), [#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170), [#181](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/181), [#247](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/247))
  * Tweaked the GUI to display the employee list and task list.
    * In employee list, details of each employee (`name`, `phone`, `email`, `numOfTasks`) are listed.
    * In task list, details of each task (`description`, `type`, `assignees`, `priority`, `isDone`) are listed.
  * Justification:
    * This enhancement is important as all other features implemented by my teammates must be reflected correctly in the employee list and task list.
  * Highlights:
    * This enhancement requires me to research on JavaFX extensively as I need to understand JavaFx properties and build layouts in fxml code with the help of the Scene Builder software. I will also need to have knowledge on CSS as CSS is used to set the style and alignment of JavaFx containers and controls.
    * This enhancement ensures that the employee list and task list displayed in the GUI are updated synchronously whenever any changes are made to the data.
    * This enhancement ensures that all features implemented by my teammates are working as expected.


* **Enhancements to Existing Features:**
  * Refactored `edit` feature of AddressBook-Level3 to `editEmployee` feature and then enhanced the feature.
    * Justification: The `editEmployee` feature edits the details of the employee identified by the index number used in the displayed employee list. All tasks that are assigned to the edited employee is then updated to reflect the new changes of the employee. ([#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170), [#179](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/179))
  * Refactored `delete` feature of AddressBook-Level3 to `deleteEmployee` feature and then enhanced the feature.
    * Justification: The `deleteEmployee` feature deletes the employee identified by the index number used in the displayed employee list. All tasks that are assigned to the deleted employee is then updated to remove the employee from the respective tasks. ([#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170), [#182](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/182))
  * Updated the GUI of the Help Window to replace the user guide URL and showcase the feature of copying employee details to Computer Clipboard. ([#247](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/247))


* **Enhancements to New Features:**
  * Tweaked the `tagTask` feature implemented by team member [Alfred Koh](https://ay2122s2-cs2103-f11-1.github.io/tp/team/alfredkohhh.html). ([#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170))
    * Ensured that the GUI is updated synchronously to show that
      * the task is assigned to the employee in the displayed employee list and the number of assigned tasks of the employee is increased by 1.
      * the employee's full name is added to the assignees of the task in the displayed task list.
  * Tweaked the `untagTask` feature implemented by team member [Alfred Koh](https://ay2122s2-cs2103-f11-1.github.io/tp/team/alfredkohhh.html). ([#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170))
    * Ensured that the GUI is updated synchronously to show that
      * the task is deallocated from the employee in the displayed employee list and the number of assigned tasks of the employee is decreased by 1.
      * the employee's full name is removed from the assignees of the task in the displayed task list.
  * Tweaked the `tagPriority` feature implemented by team member [Alfred Koh](https://ay2122s2-cs2103-f11-1.github.io/tp/team/alfredkohhh.html). ([#170](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/170))
    * Ensured that the GUI is updated synchronously to show that the task is assigned with the specified `priority`.
  * Tweaked the `editTask` feature implemented by team member [Aaron Loke](https://ay2122s2-cs2103-f11-1.github.io/tp/team/aaron-ljx.html). ([#179](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/179), [#182](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/182), [#185](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/185))
    * Handled bugs such as IndexOutOfBounds for invalid task index of the displayed task list.
    * Ensured that `type`, `assignees`, `priority` and `isDone` remain unchanged and the original values are not discarded after editing the task.
    * Ensured that exception messages are thrown correctly and appropriately.
    * Ensured that the GUI is updated synchronously to show that the task details are updated.
    * Guided team member Aaron on the approach of resolving the bugs so that the advertised behaviour in the UG will be consistent with the actual behaviour ([#256](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/256)).
    

* **Enhancements to Validation Checks and Error Messages:** ([#179](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/179), [#185](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/185), [#247](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/247), [#259](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/259))
  * Ensured that all validation checks are handled properly.
  * Ensured that all error messages shown to the user upon executing the wrong commands are correct and understandable, so that the user can recover from any errors and continue using the application.


* **Icons for GUI:**
  * Created icons. ([#142](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/142), [#181](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/181))
  <br> <img height="30" src="../images/employee_icon.png" width="30"/> <img height="30" src="../images/phone_icon.png" width="30"/> <img height="30" src="../images/email_icon.png" width="30"/> <img height="30" src="../images/priorities_low.png" width="23"/> <img height="30" src="../images/priorities_medium.png" width="23"/> <img height="30" src="../images/priorities_high.png" width="23"/>
  * Modified Task icon created by <a href="https://www.flaticon.com/premium-icon/task_3774569">feen (Flaticon)</a> to fill up colours and change stroke colour. ([#142](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/142))
  <br> <img height="30" src="../images/task_icon.png" width="30"/>


* **Code Contributed:** [RepoSense link](https://nus-cs2103-ay2122s2.github.io/tp-dashboard/?search=dannytayjy&breakdown=true&sort=groupTitle&sortWithin=title&since=2022-02-18&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)


* **Project Management:**
  * Released v1.3.3 and v1.3.4 on GitHub after bug fixes.
  * Assisted in merging PRs of implementations done by other teammates.


* **Documentation:**
  * User Guide:
    * Added full details for the features `editEmployee`, `deleteEmployee`, `markTask`, `unmarkTask` and `deleteTask`. ([#169](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/169), [#259](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/259))
    * Polished the details and examples for the features `tagTask`, `untagTask` and `tagPriority`. ([#169](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/169))
    * Standardized the formatting of all commands and update some missing details in other commands. ([#169](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/169))
    * Updated the table formatting for Command Summary. ([#169](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/169))
  * Developer Guide:
    * Added Non-Functional Requirements and Glossary details. ([#20](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/20))
    * Added implementation details of the `UI` component for `TaskListPanel` and `TaskListCard`. ([#129](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/129))
    * Added implementation details of the `Logic` component for `deleteEmployee`, `deleteTask`, `markTask` and `unmarkTask` features, including the class diagrams and screenshots of the application executing the feature. ([#129](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/129))


* **Community:**
  * Refactored project's Java package to manageezpz : [#60](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/60)
  * Updated the user guide URL in the Help Window : [#247](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/247)
  * Reviewed PRs with non-trivial review comments : [#108](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/108), [#145](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/145), [#156](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/156), [#161](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/161), [#257](https://github.com/AY2122S2-CS2103-F11-1/tp/pull/257)
  * Reported bugs and suggestions for other team in the same CS2103/T class : [PE Dry Run](https://github.com/dannytayjy/ped/issues)


* **Tools:**
  * IntelliJ IDEA
  * JavaFX Scene Builder
  * Adobe Photoshop
