package ui;

import model.DataSet;
import model.DataBase;

import persistence.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

// Data analysis application
public class DataAnalysisApp {
    private static final String JSON_STORE = "./data/database.json";
    private DataBase dataBase;      // database used to store datasets for application
    private Scanner input;          // scanner used for processing main menu inputs
    private JsonWriter jsonWriter;  // writer used for writing Json file
    private JsonReader jsonReader;  // reader used for reading Json file
    private JPanel panel;
    private JFrame frame;

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
        saveReminder();
        System.out.println("Thanks for using!");
    }

    // EFFECTS: reminds user to save database and allows for saving
    private void saveReminder() {
        String order;
        while (true) {
            System.out.println("Do you want to save your database? \"y\"/\"n\"");
            order = input.next();
            order = order.toLowerCase();
            if (order.equals("y")) {
                saveDataBase();
                break;
            } else if (order.equals("n")) {
                break;
            }
            System.err.println("Invalid input!");
        }
    }

    // MODIFIES: this, dataBase
    // EFFECTS: processes user input in main menu
    private void processOrder(String order) {
        if (order.equalsIgnoreCase("pooled list")) {
            listUI(null, true);
        } else if (isInList(order)) {
            for (DataSet data : dataBase.getDataSets()) {
                if (order.equalsIgnoreCase(data.getListName())) {
                    listUI(data, false);
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

    // MODIFIES: this, dataBase
    // EFFECTS: loads database from file
    private void loadDataBase() {
        try {
            dataBase = jsonReader.read();
            System.out.println("Loaded database.");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this, data
    // EFFECTS: outputs list UI and allows for user input
    private void listUI(DataSet data, boolean isPool) {
        String userInput;
        listUIOutput(data, isPool);
        userInput = input.next();
        if (isPool) {
            listInputProcessor(userInput);
        } else {
            listInputProcessor(data, userInput);
        }
    }

    // MODIFIES: this, data
    // EFFECTS: helper for printing out list UI
    private void listUIOutput(DataSet data, boolean isPool) {
        if (isPool) {
            updateStats();
        } else {
            updateStats(data);
        }
        System.out.println("\n" + (isPool ? "Pooled List" : data.getListName()));
        System.out.println("-------------");
        for (double num : (isPool ? dataBase.getPooledList() : data.getList())) {
            System.out.println(num);
        }
        System.out.println("\nLength: " + (isPool ? dataBase.getPooledListLength() : data.getListLength()));
        System.out.println("Mean: " + (isPool ? dataBase.getListMean() : data.getListMean()));
        System.out.println("Median: " + (isPool ? dataBase.getListMedian() : data.getListMedian()));
        System.out.println("Variance: " + (isPool ? dataBase.getListVar() : data.getListVar()));
        System.out.println("Standard Deviation: " + (isPool ? dataBase.getListSD() : data.getListSD()));
        System.out.println("Enter a command");
        if (!isPool) {
            System.out.println("\t\"an\" -> Add Number");
            System.out.println("\t\"rn\" -> Remove Number");
        }
        System.out.println("\t\"sl\" -> Sort List");
        System.out.println("\t\"ci\" -> Calculate Confidence Interval (Please check for CLT assumptions!)");
        System.out.println("\t\"b\" -> Back");
    }

    // EFFECTS: calculates the confidence interval for dataset and outputs the confidence interval
    private void calcCI(DataSet data) {
        String inputText;
        double cl;
        double z;
        confIntOutput();
        inputText = input.next();
        try {
            cl = Double.parseDouble(inputText) / 100;
            z = DataSet.getZ(cl);
            if (z == 0) {
                System.err.println("Not a valid confidence level!\nPlease try again:");
                calcCI(data);
            } else {
                System.out.println("Confidence Interval:\n" + ((data == null) ? dataBase.calcConfInterval(z) :
                        data.calcConfInterval(z)));
            }
        } catch (NumberFormatException e) {
            System.err.println("Not a number!\nPlease try again:");
            calcCI(data);
        }
    }

    // EFFECTS: helper for outputting confidence interval texts
    private static void confIntOutput() {
        System.out.println("Please enter confidence level: \n\t\"99.7\" -> 99.7%");
        System.out.println("\t\"99\" -> 99%\n\t\"95\" -> 95%");
        System.out.println("\t\"90\" -> 90%");
        System.out.println("\t\"80\" -> 80%");
        System.out.println("\t\"75\" -> 75%");
        System.out.println("\t\"50\" -> 50%");
    }

    // MODIFIES: this
    // EFFECTS: handles input for list UI for pooled list
    private void listInputProcessor(String userInput) {
        switch (userInput) {
            case "b":
                return;
            case "sl":
                dataBase.sortList();
                listUI(null, true);
                break;
            case "ci":
                calcCI(null);
                listUI(null, true);
                break;
            default:
                System.err.println("Invalid input");
                listUI(null, true);
                break;
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
                listUI(data, false);
                break;
            case "ci":
                calcCI(data);
                listUI(data, false);
                break;
            default:
                System.err.println("Invalid input");
                listUI(data, false);
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
                dataBase.removeNumFromPool(number);
                updateStats(data);
                listUI(data, false);
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
            dataBase.addNumToPool(number);
            updateStats(data);
            listUI(data, false);
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
    }

    // MODIFIES: this
    // EFFECTS: update statistics of pooled list
    private void updateStats() {
        dataBase.calcMean();
        dataBase.calcMedian();
        dataBase.calcSD();
        dataBase.calcVariance();
    }

    // EFFECTS: returns true if program contains a dataset with given String name, false otherwise
    private boolean isInList(String order) {
        if (order.equalsIgnoreCase("pooled list")) {
            return true;
        }
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
        ArrayList<Double> numbsToRemove;
        listExists = isInList(listName);
        if (listName.equalsIgnoreCase("pooled list")) {
            System.err.println("You cannot delete the pooled list!");
            return;
        }
        numbsToRemove = dataBase.removeList(listName);
        for (double num : numbsToRemove) {
            dataBase.removeNumFromPool(num);
        }
        if (!listExists) {
            System.err.println("List does not exist");
        }
    }

    // MODIFIES: this, dataBase
    // EFFECTS: initializes datasets and Scanners
    private void initialize() {
        dataBase = new DataBase("Database");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        guiWindowSetup();
    }

    private void guiWindowSetup() {
        frame = new JFrame();
        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Data Analysis App");
        frame.pack();
        frame.setVisible(true);
    }

    // EFFECTS: shows main UI menu
    private void showMainMenu() {
        guiMainMenu();
        System.out.println("\nPlease select from following: \n");
        System.out.println(dataBase.getName());
        System.out.println("------------------");
        System.out.println("Lists: ");
        System.out.println("\t\"" + "Pooled List" + "\"");
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

    private void guiMainMenu() {
        Map<String, JButton> listOfDataButtons = new HashMap<>();
        panel.removeAll();
        JLabel line1 = new JLabel("Lists: ");
        JLabel line2 = new JLabel("Commands:");
        JButton button = new JButton("Pooled List");
        JButton newList = new JButton("New List");
        JButton removeList = new JButton("Remove List");
        JButton clearAll = new JButton("Clear All");
        JButton save = new JButton("Save");
        JButton load = new JButton("Load");
        JButton quit = new JButton("Quit");
        listOfDataButtons.put("Pooled List", button);
        for (DataSet dataSet : dataBase.getDataSets()) {
            JButton temp = new JButton(dataSet.getListName());
            listOfDataButtons.put(dataSet.getListName(), temp);
        }
        addButtons(listOfDataButtons, line1, line2, newList, removeList, clearAll, save, load,
                quit);
    }

    private void addButtons(Map<String, JButton> listOfDataButtons, JLabel line1, JLabel line2, JButton newList,
                            JButton removeList, JButton clearAll, JButton save, JButton load, JButton quit) {
        panel.add(line1);
        for (String s : listOfDataButtons.keySet()) {
            panel.add(listOfDataButtons.get(s));
        }
        panel.add(line2);
        panel.add(newList);
        panel.add(removeList);
        panel.add(clearAll);
        panel.add(save);
        panel.add(load);
        panel.add(quit);
    }


}
