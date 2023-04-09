# Data Analysis Tool

## A simple program for the statistical analysis of data

This program will be an intuitive and simple-to-use data analyzer where the user can enter numbers manually into data 
lists and save those lists in the program and access them freely. With the lists as the datasets, the program can also 
calculate a wide variety of summary statistics such as:
- mean
- median
- standard deviation
- variance

Further, with the datasets stored in the program, the program can also produce confidence intervals.

This program is designed to be a tool for *anyone* who is trying to do any simple data analysis. But this will mostly 
be used by students or other people who are studying or are in the field of statistics. This project is of great 
interest to me because this program if developed well could be a very convenient tool for anyone who is trying to 
calculate some simple statistics. Also, personally, as a statistics major, making a project that is closely tied to my 
field makes the project much more interesting and applicable. Once the project is finished, this will be of wonderful 
use to me. I could also, in the future, after I have completed this project as a foundation, add more statistical tests 
into the functionality of the program, developing, this into the **ultimate** tool for statistics students.

### User Stories

- As a user, I want to be able to add numbers to a dataset
- As a user, I want to be able to create new datasets
- As a user, I want to see a pool of all the data that have been added
- As a user, I want to be able to clear all datasets that have been added to the program
- As a user, I want to be able to delete individual datasets
- As a user, I want to be able to find the mean of a dataset
- As a user, I want to be able to find the median of a dataset
- As a user, I want to be able to find the standard deviation of a dataset
- As a user, I want to be able to find the variance of a dataset
- As a user, I want to be able to create confidence intervals with a dataset
- As a user, I want to be able to remove individual entries in a dataset
- As a user, I want to be able to sort datasets from small to large 
- As a user, I want to be able to save an entire database to file (based on JsonWriter in JsonSerializationDemo 
https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
- As a user, I want to be given the option to load a saved database to the program to file (based on JsonReader in
JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
- As a user, I want to be reminded and given the option to save the entire database to file when I quit the program

 ### Instructions for Grader

- You can add a dataset to the database by clicking the "add" button and then entering the name of the new dataset
- You can remove a dataset from the database by clicking the "remove" button and then entering the name of the dataset
- You can add numbers to a dataset by first clicking a dataset and then clicking the "add" button and then entering a number
- You can remove numbers from a dataset by first clicking a dataset and then clicking the "remove" button and then entering a number
- You can locate my visual components in the main menu at the top (https://pixabay.com/photos/accountant-accounting-adviser-1238598/) 
and in the "exit" button (https://pixabay.com/vectors/exit-the-end-emergency-exit-door-1699614/) as well as in the 
"back" button (https://pixabay.com/vectors/arrow-left-back-backwards-sign-39644/) in the list UI
- You can save the state of my application by clicking the "save" button
- You can reload the state of my application by clicking the "load" button

### Phase 4: Task 2
Example event log

"Event log:

Fri Apr 07 22:07:50 PDT 2023
list 1 added to "Database" database,
Fri Apr 07 22:07:50 PDT 2023
list 2 added to "Database" database,
Fri Apr 07 22:07:50 PDT 2023
list 3 added to "Database" database,
Fri Apr 07 22:07:54 PDT 2023
lsit 4 added to "Database" database,
Fri Apr 07 22:08:08 PDT 2023
lsit 4 removed from "Database" database,
Fri Apr 07 22:08:10 PDT 2023
"Database" database cleared,
Fri Apr 07 22:08:11 PDT 2023
list 1 added to "Database" database,
Fri Apr 07 22:08:11 PDT 2023
list 2 added to "Database" database,
Fri Apr 07 22:08:11 PDT 2023
list 3 added to "Database" database,
Fri Apr 07 22:08:20 PDT 2023
123.0 added to list 3,
Fri Apr 07 22:08:20 PDT 2023
123.0 added to Pooled List,
Fri Apr 07 22:08:23 PDT 2023
123.0 removed from list 3,
Fri Apr 07 22:08:23 PDT 2023
123.0 removed from Pooled List,
Fri Apr 07 22:08:28 PDT 2023
1.0 added to list 3,
Fri Apr 07 22:08:28 PDT 2023
1.0 added to Pooled List,
Fri Apr 07 22:08:29 PDT 2023
list 3 sorted,
Fri Apr 07 22:08:33 PDT 2023
list 3's confidence interval calculated,
Fri Apr 07 22:08:41 PDT 2023
Pooled List sorted"

### Phase 4: Task 3

Having created a UML diagram to properly visualize my project and experience in having to make changes clumsily while 
coding, a few flaws have come into the picture warranting refactoring. Firstly, I should have separated the GUI and 
the console UI into two different classes in the UI package. This way, whether a method handles something in the GUI
functionality or the console UI functionality will be explicitly clear and whatever changes I would like to change in 
functionality of the application can be changed at a single point of control. This also reduces a lot of duplicated 
code that is currently present in the UI code. What I could do to still make both GUI and console UI work in tandem(a 
reason for the shortcut in my current design) is to extract out all the duplicated logic methods that makes the program 
function and put them in an interface or abstract class and have the two UI classes implement or extend the class to 
gain access to those methods. There are also some smaller functionality shared between some output methods that could be 
extracted into helper methods. 

Secondly, in my model directory, specifically regarding my DataBase and DataSet classes, there is a lot of code 
duplication between the two classes. Other than methods for the manipulation of the list of DataSets in the DataBase,
the classes have effectively the same methods for the modification of their respective lists of numbers. To reduce the 
code duplication and instate a single point of control for the shared methods, I should extract an abstract class with 
composed of those methods that are extended by the DataBase and DataSet classes.

After implementing the refactoring suggested above, the code for the program will be greatly more accessible and easier
to edit for any purpose from adding functionality to improving the UI and, in the long run, become much more 
maintainable. 
