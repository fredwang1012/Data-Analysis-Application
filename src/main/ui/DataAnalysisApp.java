package ui;

import model.DataSet;
import model.DataBase;
import persistence.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// Data analysis application
public class DataAnalysisApp {
    private static final String JSON_STORE = "./data/database.json";
    private DataBase dataBase;      // database used to store datasets for application
    private Scanner input;          // scanner used for processing main menu inputs
    private Scanner innerInput;     // scanner used for processing dataset UI inputs
    private JsonWriter jsonWriter;  // writer used for writing Json file
    private JsonReader jsonReader;  // reader used for reading Json file


    // EFFECTS: runs the data analysis application
    public DataAnalysisApp() throws FileNotFoundException {
        runApp();
    }


    /*
    Basic app model based on Teller application example
     */
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

    // MODIFIES: this, dataBase
    // EFFECTS: processes user input in main menu
    private void processOrder(String order) {
        if (order.equalsIgnoreCase("pooled list")) {
            pooledUI();
        } else if (isInList(order)) {
            for (DataSet data : dataBase.getDataSets()) {
                if (order.equalsIgnoreCase(data.getListName())) {
                    listUI(data);
                }
            }
        } else {
            processOrderInputHandler(order);
        }
    }

    // MODIFIES: this
    // EFFECTS: handles the user input for the main menu
    private void processOrderInputHandler(String order) {
        switch (order) {
            case "nl":
                order = getInput("Please give your list a name:");
                makeNewList(order);
                break;
            case "rl":
                order = getInput("Please enter the name of list:");
                removeExistingList(order);
                break;
            case "ca":
                dataBase.clearAll();
                initialize();
                break;
            case "s":
                saveDataBase();
                break;
            case "l":
                loadDataBase();
                break;
            default:
                System.err.println("Invalid input!");
                break;
        }
    }

    // EFFECTS: gets the user input for processOrderInputHandler and returns string
    private String getInput(String userInput) {
        String order;
        System.out.println(userInput);
        order = input.next();
        return order;
    }

    // EFFECTS: saves database to file
    private void saveDataBase() {
        try {
            jsonWriter.open();
            jsonWriter.write(dataBase);
            jsonWriter.close();
            System.out.println("Saved database to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads database from file
    private void loadDataBase() {
        try {
            dataBase = jsonReader.read();
            System.out.println("Loaded database.");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this, dataBase
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

    // MODIFIES: this, dataBase
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

    // MODIFIES: this, data
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

    // MODIFIES: this, data
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

    // MODIFIES: this, data
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
                System.err.println("Number not in list, please try again!");
                removeNum(data);
            }
        } catch (NumberFormatException e) {
            System.err.println("Not a number!");
            System.err.println("Please try again:");
            removeNum(data);
        }
    }

    // MODIFIES: this, data
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

    // MODIFIES: this, data
    // EFFECTS: update statistics of passed in dataset
    private void updateStats(DataSet data) {
        data.calcMean();
        data.calcMedian();
        data.calcVariance();
        data.calcSD();
        dataBase.getData(0).calcMean();
        dataBase.getData(0).calcMedian();
        dataBase.getData(0).calcSD();
        dataBase.getData(0).calcVariance();

    }

    // EFFECTS: returns true if program contains a dataset with given String name, false otherwise
    private boolean isInList(String order) {
        for (DataSet data : dataBase.getDataSets()) {
            if (data.getListName().equalsIgnoreCase(order)) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this, dataBase
    // EFFECTS: makes a new dataset with given name
    private void makeNewList(String listName) {
        boolean listExists;
        listExists = isInList(listName);
        if (listExists) {
            System.err.println("A list already has that name!");
            return;
        }
        dataBase.addList(listName);
    }

    // MODIFIES: this, dataBase
    // EFFECTS: removes existing dataset with the passed in name
    private void removeExistingList(String listName) {
        boolean listExists;
        ArrayList<Double> numToRemove;
        listExists = isInList(listName);
        if (listName.equalsIgnoreCase("pooled list")) {
            System.err.println("You cannot delete the pooled list!");
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

    // MODIFIES: this, dataBase
    // EFFECTS: initializes datasets and Scanners
    private void initialize() {
        dataBase = new DataBase("DataBase");
        dataBase.addList("Pooled List");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        innerInput = new Scanner(System.in);
        innerInput.useDelimiter("\n");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    // EFFECTS: shows main UI menu
    private void showMainMenu() {
        System.out.println("\nPlease select from following: ");
        System.out.println("\nLists: ");
        for (DataSet data : dataBase.getDataSets()) {
            System.out.println("\t\"" + data.getListName() + "\"");
        }
        System.out.println("\n\"nl\" -> New List");
        System.out.println("\"rl \" -> Remove List");
        System.out.println("\"ca\" -> Clear All");
        System.out.println("\"s\" -> Save");
        System.out.println("\"l\" -> Load");
        System.out.println("\"q\" -> Quit");
    }


}
