package ui;

import model.DataSet;
import model.DataBase;

import java.util.*;

// Data analysis application
public class DataAnalysisApp {
    private DataBase dataBase;
    private Scanner input;
    private Scanner innerInput;

    // EFFECTS: runs the data analysis application
    public DataAnalysisApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: keeps the app running
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

        System.out.println("Thanks for using!");
    }

    // MODIFIES: this
    // EFFECTS: processes user input in main menu
    private void processOrder(String order) {
        if (order.equalsIgnoreCase("pooled list")) {
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
        System.out.println("\nLength: " + dataBase.getData(0).getListLength());
        System.out.println("Mean: " + dataBase.getData(0).getListMean());
        System.out.println("Median: " + dataBase.getData(0).getListMedian());
        System.out.println("Variance: " + dataBase.getData(0).getListVar());
        System.out.println("Standard Deviation: " + dataBase.getData(0).getListSD());
        System.out.println("\nEnter a command:");
        System.out.println("\t\"sl\" -> Sort List");
        System.out.println("\t\"ci\" -> Calculate Confidence Interval (Please check for CLT assumptions!)");
        System.out.println("\t\"b\" -> Back");
        userInput = innerInput.next();
        pooledListInputProcessor(userInput);
    }

    // MODIFIES: this
    // EFFECTS: handles the input for pooled dataset
    private void pooledListInputProcessor(String userInput) {
        switch (userInput.toLowerCase()) {
            case "b":
                return;
            case "sl":
                dataBase.getData(0).sortList();
                pooledUI();
                break;
            case "ci":
                calcCI(dataBase.getData(0));
                pooledUI();
                break;
            default:
                System.err.println("Invalid input!");
                pooledUI();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: outputs UI for normal dataset and allows for user input
    private void listUI(DataSet data) {
        String userInput;
        System.out.println("\n" + data.getListName());
        System.out.println("-------------");
        for (double num : data.getList()) {
            System.out.println(num);
        }
        System.out.println("\nLength: " + data.getListLength());
        System.out.println("Mean: " + data.getListMean());
        System.out.println("Median: " + data.getListMedian());
        System.out.println("Variance: " + data.getListVar());
        System.out.println("Standard Deviation: " + data.getListSD());
        System.out.println("Enter a command");
        System.out.println("\t\"an\" -> Add Number");
        System.out.println("\t\"rn\" -> Remove Number");
        System.out.println("\t\"sl\" -> Sort List");
        System.out.println("\t\"ci\" -> Calculate Confidence Interval (Please check for CLT assumptions!)");
        System.out.println("\t\"b\" -> Back");
        userInput = innerInput.next();
        listInputProcessor(data, userInput);
    }

    // EFFECTS: calculates the confidence interval for dataset and outputs the confidence interval
    private void calcCI(DataSet data) {
        String input;
        double cl;
        double z;
        System.out.println("Please enter confidence level: \n\t\"99.7\" -> 99.7%");
        System.out.println("\t\"99\" -> 99%\n\t\"95\" -> 95%");
        System.out.println("\t\"90\" -> 90%");
        System.out.println("\t\"80\" -> 80%");
        System.out.println("\t\"75\" -> 75%");
        System.out.println("\t\"50\" -> 50%");
        input = innerInput.next();
        try {
            cl = Double.parseDouble(input) / 100;
            z = DataSet.getZ(cl);
            if (z == 0) {
                System.err.println("Not a valid confidence level!\nPlease try again:");
                calcCI(data);
            } else {
                System.out.println("Confidence Interval:\n" + data.calcConfInterval(z));
            }
        } catch (NumberFormatException e) {
            System.err.println("Not a number!\nPlease try again:");
            calcCI(data);
        }
    }

    // MODIFIES: this
    // EFFECTS: handles input for list UI for given dataset
    private void listInputProcessor(DataSet data, String userInput) {
        switch (userInput) {
            case "an":
                addNum(data);
                break;
            case "rn":
                removeNum(data);
                break;
            case "b":
                return;
            case "sl":
                data.sortList();
                listUI(data);
                break;
            case "ci":
                calcCI(data);
                listUI(data);
                break;
            default:
                System.err.println("Invalid input");
                listUI(data);
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: removes existing number from passed in dataset
    private void removeNum(DataSet data) {
        double number;
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
        double number;
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
        boolean listExists;
        listExists = isInList(listName);
        if (listExists) {
            System.out.println("A list already has that name!");
            return;
        }
        dataBase.addList(listName);
    }

    // MODIFIES: this
    // EFFECTS: removes existing dataset with the passed in name
    private void removeExistingList(String listName) {
        boolean listExists;
        ArrayList<Double> numToRemove;
        listExists = isInList(listName);
        if (listName.equalsIgnoreCase("pooled list")) {
            System.out.println("You cannot delete the pooled list!");
            return;
        }
        numToRemove = dataBase.removeList(listName);
        for (double num : numToRemove) {
            dataBase.getData(0).removeNum(num);
        }
        if (!listExists) {
            System.err.println("List does not exist");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes datasets and Scanners
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
