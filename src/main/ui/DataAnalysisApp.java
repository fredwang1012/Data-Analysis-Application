package ui;

import model.DataSet;
import model.DataBase;

import java.util.*;

// Data analysis application
public class DataAnalysisApp {
    private DataBase dataBase;
    private Scanner input;
    private Scanner innerInput;
    private boolean backToMainMenu = false;

    // EFFECTS: runs the data analysis application
    public DataAnalysisApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean run = true;
        String order;

        initialize();
        System.out.println("Welcome!");
        while (run) {
            showMainMenu();
            order = input.next();
            order = order.toLowerCase();

            if (order.equals("q")) {
                run = false;
            } else {
                processOrder(order);
            }
        }

        System.out.println("Thanks for your patronage!");

    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processOrder(String order) {
        if (order.toLowerCase().equals("pooled list")) {
            pooledUI();
        } else if (isInList(order)) {
            for (DataSet data : dataBase.getDataBase()) {
                if (data.getListName().equals(order)) {
                    listUI(data);
                }
            }
        } else if (order.equals("nl")) {
            System.out.println("Please give your list a name:");
            order = input.next();
            makeNewList(order);
        } else if (order.equals("rl")) {
            System.out.println("Please enter the name of list:");
            order = input.next();
            removeExistingList(order);
        } else if (order.equals("ca")) {
            dataBase.clearAll();
            initialize();
        } else {
            System.err.println("Invalid input!");
        }
    }

    // MODIFIES: this
    // EFFECTS: outputs UI for pooled dataset and allows for user input
    private void pooledUI() {
        String userInput;
        updateStats(dataBase.getData(0));
        System.out.println("\nPooled Data\n-------------");
        for (double num : dataBase.getData(0).getList()) {
            System.out.println(num);
        }
        System.out.println("\nMean: " + dataBase.getData(0).getListMean());
        System.out.println("Median: " + dataBase.getData(0).getListMedian());
        System.out.println("Variance: " + dataBase.getData(0).getListVar());
        System.out.println("Standard Deviation: " + dataBase.getData(0).getListSD());
        System.out.println("\nEnter a command:");
        System.out.println("\t\"sl\" -> Sort List");
        System.out.println("\t\"b\" -> Back");
        userInput = innerInput.next();
        if (userInput.toLowerCase().equals("b")) {
            return;
        } else if (userInput.toLowerCase().equals("sl")) {
            dataBase.getData(0).sortList();
            pooledUI();
        } else {
            System.err.println("Invalid input!");
            pooledUI();
        }
    }

    // MODIFIES: this
    // EFFECTS: outputs UI for normal dataset and allows for user input
    private void listUI(DataSet data) {
        String userInput;
        System.out.println(data.getListName());
        System.out.println("-------------");
        for (double num : data.getList()) {
            System.out.println(num);
        }
        System.out.println("\nMean: " + data.getListMean());
        System.out.println("Median: " + data.getListMedian());
        System.out.println("Variance: " + data.getListVar());
        System.out.println("Standard Deviation: " + data.getListSD());
        System.out.println("Enter a command");
        System.out.println("\t\"an\" -> Add Number");
        System.out.println("\t\"rn\" -> Remove Number");
        System.out.println("\t\"sl\" -> Sort List");
        System.out.println("\t\"b\" -> Back");
        userInput = innerInput.next();
        listInputProcessor(data, userInput);
    }

    // MODIFIES: this
    // EFFECTS: handles input for list UI
    private void listInputProcessor(DataSet data, String userInput) {
        if (userInput.equals("an")) {
            addNum(data);
        } else if (userInput.equals("rn")) {
            removeNum(data);
        } else if (userInput.equals("b")) {
            return;
        } else if (userInput.toLowerCase().equals("sl")) {
            data.sortList();
            listUI(data);
        } else {
            System.err.println("Invalid input");
            listUI(data);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes existing number in passed in dataset
    private void removeNum(DataSet data) {
        double number = 0;
        boolean numRemoved;
        String numberInput;
        System.out.println();
        System.out.println("Enter number:");
        numberInput = input.next();
        try {
            number = Double.parseDouble(numberInput);
            numRemoved = data.removeNum(number);
            if (numRemoved) {
                dataBase.getData(0).removeNum(number);
                updateStats(data);
                listUI(data);
            } else {
                System.out.println("Number not in list, please try again!");
                removeNum(data);
            }
        } catch (NumberFormatException e) {
            System.err.println("Not a number!");
            System.err.println("Please try again:");
            removeNum(data);
        }
    }

    // MODIFIES: this
    // EFFECTS: add number to passed in dataset
    private void addNum(DataSet data) {
        double number = 0;
        String numberInput;
        System.out.println();
        System.out.println("Enter number:");
        numberInput = input.next();
        try {
            number = Double.parseDouble(numberInput);
            data.addNum(number);
            dataBase.getData(0).addNum(number);
            updateStats(data);
            listUI(data);
        } catch (NumberFormatException e) {
            System.err.println("Not a number!");
            System.err.println("Please try again:");
            addNum(data);
        }
    }

    // MODIFIES: this
    // EFFECTS: update statistics of passed in dataset
    private void updateStats(DataSet data) {
        data.calcMean();
        data.calcMedian();
        data.calcVariance();
        data.calcSD();
    }

    // EFFECTS: returns true if program contains a dataset with given String name, false otherwise
    private boolean isInList(String order) {
        for (DataSet data : dataBase.getDataBase()) {
            if (data.getListName().equals(order)) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: makes a new dataset with given name
    private void makeNewList(String listName) {
        dataBase.addList(listName);
    }

    // MODIFIES: this
    // EFFECTS: removes existing dataset with the passed in name
    private void removeExistingList(String listName) {
        boolean listExists;
        listExists = isInList(listName);
        if (listName.toLowerCase().equals("pooled list")) {
            System.out.println("You cannot delete the pooled list!");
            return;
        }
        dataBase.removeList(listName);
        if (!listExists) {
            System.out.println("List does not exist");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes datasets
    private void initialize() {
        dataBase = new DataBase();
        dataBase.addList("Pooled List");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        innerInput = new Scanner(System.in);
        innerInput.useDelimiter("\n");
    }

    // EFFECTS: shows main UI menu
    private void showMainMenu() {
        System.out.println("\nPlease select from following: ");
        System.out.println("\nLists: ");
        for (DataSet data : dataBase.getDataBase()) {
            System.out.println("\t\"" + data.getListName() + "\"");
        }
        System.out.println("\n\"nl\" -> New List");
        System.out.println("\"rl \" -> Remove List");
        System.out.println("\"ca\" -> Clear All");
        System.out.println("\"q\" -> Quit");
    }


}
