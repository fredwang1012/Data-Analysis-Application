package ui;

import model.DataSet;
import model.DataBase;

import model.Event;
import model.EventLog;
import persistence.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

// Data analysis application
public class DataAnalysisApp {
    private static final String JSON_STORE = "./data/database.json";
    private DataBase dataBase;                          // database used to store datasets for application
    private Scanner input;                              // scanner used for processing main menu inputs
    private JsonWriter jsonWriter;                      // writer used for writing Json file
    private JsonReader jsonReader;                      // reader used for reading Json file
    private JPanel panel;                               // panel for main GUI
    private JFrame frame;                               // frame for main GUI
    private static JFrame subFrame;                     // frame for small sub frame
    private Map<String, JButton> listOfDataButtons;     // map for the dataset buttons and their names
    private BufferedImage image;                        // top bar image by Robert Owen-Wahl from Pixabay
    private Icon backArrow;                             // back arrow image by Clker-Free-Vector-Images from Pixabay
    private Icon exitSign;                              // exit symbol image by Jan from Pixabay

    // EFFECTS: runs the data analysis application
    public DataAnalysisApp() throws IOException {
        runApp();
    }

    /*
    Basic app model based on Teller application example
     */
    // MODIFIES: this
    // EFFECTS: keeps the app running
    private void runApp() {
        String order;
        initialize();
        System.out.println("Welcome!");
        while (true) {
            showMainMenu();
            setUpMainMenuButtons();
            order = input.next();
            order = order.toLowerCase();
            if (order.equals("q")) {
                saveReminder();
                System.out.println("Thanks for using!");
                printEventLog();
                System.exit(0);
            } else {
                processOrder(order);
            }
        }
    }

    // MODIFIES: panel
    // EFFECTS: sets up the main menu buttons for the datasets
    private void setUpMainMenuButtons() {
        for (String s : listOfDataButtons.keySet()) {
            ActionListener temp = e -> guiProcessOrder(s);
            listOfDataButtons.get(s).addActionListener(temp);
        }
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

    // EFFECTS: processes the buttons for lists in main menu
    private void guiProcessOrder(String order) {
        if (order.equalsIgnoreCase("pooled list")) {
            guiListUI(null, true);
        } else if (isInList(order)) {
            for (DataSet data : dataBase.getDataSets()) {
                if (order.equalsIgnoreCase(data.getListName())) {
                    guiListUI(data, false);
                }
            }
        }
    }

    // EFFECTS: helper for GUI output
    private void guiListUI(DataSet dataSet, boolean isPool) {
        if (isPool) {
            guiPooledListUI();
        } else {
            guiNormalListUI(dataSet);
        }
    }

    // MODIFIES: frame, panel, dataBase
    // EFFECTS: creates the JPanel for the pooled list UI
    private void guiPooledListUI() {
        panel.removeAll();
        SwingUtilities.updateComponentTreeUI(frame);

        panel.add(new JLabel("Pooled List:"));
        for (double d : dataBase.getPooledList()) {
            panel.add(new JLabel(String.valueOf(d)));
        }
        updateStats();
        panel.add(new JLabel("Length: " + dataBase.getPooledListLength()));
        panel.add(new JLabel("Mean: " + dataBase.getListMean()));
        panel.add(new JLabel("Median: " + dataBase.getListMedian()));
        panel.add(new JLabel("Variance: " + dataBase.getListVar()));
        panel.add(new JLabel("Standard Deviation: " + dataBase.getListSD()));
        addListButtons(true, null);
    }

    // MODIFIES: panel
    // EFFECTS: helper for adding list buttons
    private void addListButtons(boolean isPool, DataSet dataSet) {
        JButton sort = new JButton("Sort");
        JButton confInt = new JButton("Confidence Interval");
        JButton back = new JButton(backArrow);
        back.setText("Back");
        buttonActionAssignment(isPool, dataSet, sort, confInt, back);
        panel.add(sort);
        panel.add(confInt);
        panel.add(back);
    }

    // MODIFIES: panel
    // EFFECTS: assigns actions to buttons
    private void buttonActionAssignment(boolean isPool, DataSet dataSet, JButton sort, JButton confInt, JButton back) {
        sort.addActionListener(e -> {
            if (isPool) {
                dataBase.sortList();
                guiPooledListUI();
            } else {
                dataSet.sortList();
                guiNormalListUI(dataSet);
            }
        });
        confInt.addActionListener(e -> confIntDialogue(isPool, dataSet, ""));
        back.addActionListener(e -> guiMainMenu());
        buttonActionAssignmentForGeneral(isPool, dataSet);
    }

    // MODIFIES: panel
    // EFFECTS: helper for assigning actions to general dataset buttons
    private void buttonActionAssignmentForGeneral(boolean isPool, DataSet dataSet) {
        if (!isPool) {
            JButton add = new JButton("Add");
            JButton remove = new JButton("Remove");
            add.addActionListener(e -> {
                addNumberDialogue(dataSet, "");
                guiNormalListUI(dataSet);
            });
            remove.addActionListener(e -> {
                removeNumberDialogue(dataSet, "");
                guiNormalListUI(dataSet);
            });
            panel.add(add);
            panel.add(remove);
        }
    }

    // EFFECTS: produces the new JFrame for adding numbers
    private void addNumberDialogue(DataSet dataSet, String message) {
        JPanel addListPanel = makeSubJPanel("Add Number");
        JLabel error = new JLabel(message);
        JLabel instruction = new JLabel("Please enter a number to add to the dataset.");
        JTextField textField = new JTextField(7);
        JButton enter = new JButton("Enter");
        addListPanel.add(error);
        addListPanel.add(instruction);
        addListPanel.add(textField);
        addListPanel.add(enter);
        enter.addActionListener(e -> handleNumberEntry(textField.getText(), addListPanel, dataSet));
    }

    // MODIFIES: dataBase, frame, panel
    // EFFECTS: assigns button actions for add number button
    private void handleNumberEntry(String input, JPanel subPanel, DataSet dataSet) {
        double number;
        try {
            number = Double.parseDouble(input);
            dataSet.addNum(number);
            dataBase.addNumToPool(number);
            updateStats(dataSet);
            subFrame.dispose();
            subPanel.setVisible(false);
            guiNormalListUI(dataSet);
        } catch (NumberFormatException e) {
            subFrame.dispose();
            subPanel.setVisible(false);
            addNumberDialogue(dataSet, "Entry was not a valid number!");
        }
    }

    // EFFECTS: produces the new JFrame for removing numbers
    private void removeNumberDialogue(DataSet dataSet, String message) {
        JPanel addListPanel = makeSubJPanel("Remove Number");
        JLabel error = new JLabel(message);
        JLabel instruction = new JLabel("Please enter a number to remove from the dataset.");
        JTextField textField = new JTextField(7);
        JButton enter = new JButton("Enter");
        addListPanel.add(error);
        addListPanel.add(instruction);
        addListPanel.add(textField);
        addListPanel.add(enter);
        enter.addActionListener(e -> handleNumRemoval(textField.getText(), addListPanel, dataSet));
    }

    // MODIFIES: dataBase, frame, panel
    // EFFECTS: assigns button actions for remove number button
    private void handleNumRemoval(String input, JPanel subPanel, DataSet dataSet) {
        double number;
        boolean numRemoved;
        try {
            number = Double.parseDouble(input);
            numRemoved = dataSet.removeNum(number);
            if (numRemoved) {
                dataBase.removeNumFromPool(number);
                subFrame.dispose();
                subPanel.setVisible(false);
                updateStats(dataSet);
                guiNormalListUI(dataSet);
            } else {
                subFrame.dispose();
                subPanel.setVisible(false);
                removeNumberDialogue(dataSet, "Entry was not an existing number!");
            }
        } catch (NumberFormatException e) {
            subFrame.dispose();
            subPanel.setVisible(false);
            removeNumberDialogue(dataSet, "Entry was not a valid number!");
        }
    }

    // EFFECTS: produces the new JFrame for confidence interval
    private void confIntDialogue(boolean isPool, DataSet dataSet, String message) {
        JPanel addListPanel = makeSubJPanel("Confidence Interval");
        JLabel error = new JLabel(message);
        JLabel instruction = new JLabel("Please enter a number one of the following confidence levels (e.g \"99\")");
        JLabel confLevels = new JLabel("99.7%, \n99%, \n95%, \n90%, \n80%, \n75%, \n50%\n");
        JTextField textField = new JTextField(7);
        JButton enter = new JButton("Enter");
        addListPanel.add(error);
        addListPanel.add(instruction);
        addListPanel.add(confLevels);
        addListPanel.add(textField);
        addListPanel.add(enter);
        enter.addActionListener(e -> handleConfInt(isPool, textField.getText(), addListPanel, dataSet));
    }

    // EFFECTS: assigns button actions for confidence interval button
    private void handleConfInt(boolean isPool, String input, JPanel panel, DataSet dataSet) {
        double cl;
        double z;
        try {
            cl = Double.parseDouble(input) / 100;
            z = DataSet.getZ(cl);
            subFrame.dispose();
            panel.setVisible(false);
            if (z == 0) {
                confIntDialogue(isPool, dataSet, "Not a valid confidence level!");
            } else {
                guiConfIntOutput("Confidence Interval: \n" + ((dataSet == null) ? dataBase.calcConfInterval(z) :
                        dataSet.calcConfInterval(z)), dataSet);
            }
        } catch (NumberFormatException e) {
            subFrame.dispose();
            panel.setVisible(false);
            confIntDialogue(isPool, dataSet, "Entry was not a valid number!");
        }
    }

    // EFFECTS: creates window for CI output
    private void guiConfIntOutput(String output, DataSet dataSet) {
        JPanel addListPanel = makeSubJPanel("Confidence Interval");
        JLabel confInt = new JLabel(output);
        addListPanel.add(confInt);
        JButton okay = new JButton("Okay");
        addListPanel.add(okay);
        okay.addActionListener(e -> {
            if (dataSet == null) {
                subFrame.dispose();
                guiPooledListUI();
            } else {
                subFrame.dispose();
                guiNormalListUI(dataSet);
            }
        });
    }

    // MODIFIES: frame, panel
    // EFFECTS: adds the buttons for the normal dataset UI
    private void guiNormalListUI(DataSet dataSet) {
        panel.removeAll();
        SwingUtilities.updateComponentTreeUI(frame);
        panel.add(new JLabel(dataSet.getListName()));
        for (double d : dataSet.getList()) {
            panel.add(new JLabel(String.valueOf(d)));
        }
        panel.add(new JLabel("Length: " + dataSet.getListLength()));
        panel.add(new JLabel("Mean: " + dataSet.getListMean()));
        panel.add(new JLabel("Median: " + dataSet.getListMedian()));
        panel.add(new JLabel("Variance: " + dataSet.getListVar()));
        panel.add(new JLabel("Standard Deviation: " + dataSet.getListSD()));
        addListButtons(false, dataSet);
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
        backArrow = new ImageIcon("./data/arrow.png");
        exitSign = new ImageIcon("./data/exit.png");
        try {
            image = ImageIO.read(new File("./data/image.jpg"));
        } catch (IOException e) {
            System.err.println("Cannot read image");
        }
        guiWindowSetup();
    }

    // MODIFIES: this, frame, panel
    // EFFECTS: sets up the GUI main menu window
    private void guiWindowSetup() {
        frame = new JFrame();
        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(15, 30));

        frame.setSize(2000, 1500);
        frame.add(panel, BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printEventLog();
                System.exit(0);
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setTitle("Data Analysis App");
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

    // MODIFIES: frame, panel
    // EFFECTS: adds buttons to the main GUI panel
    private void guiMainMenu() {
        listOfDataButtons = new LinkedHashMap<>();
        panel.removeAll();
        SwingUtilities.updateComponentTreeUI(frame);
        JLabel picture = new JLabel(new ImageIcon(image));
        panel.add(picture);
        JLabel line1 = new JLabel("Lists:");
        JLabel line2 = new JLabel("Commands:");
        JButton button = new JButton("Pooled List");
        JButton newList = new JButton("New List");
        JButton removeList = new JButton("Remove List");
        JButton clearAll = new JButton("Clear All");
        JButton save = new JButton("Save");
        JButton load = new JButton("Load");
        JButton quit = new JButton(exitSign);
        quit.setText("Exit");
        listOfDataButtons.put("Pooled List", button);
        for (DataSet dataSet : dataBase.getDataSets()) {
            JButton temp = new JButton(dataSet.getListName());
            listOfDataButtons.put(dataSet.getListName(), temp);
        }
        addButtons(listOfDataButtons, line1, line2, newList, removeList, clearAll, save, load, quit);
        assignMenuButtonActions(newList, removeList, clearAll, save, load, quit);
        setUpMainMenuButtons();
    }

    // MODIFIES: panel
    // EFFECTS: adds button actions to main menu buttons
    private void assignMenuButtonActions(JButton newList, JButton removeList, JButton clearAll, JButton save,
                                         JButton load, JButton quit) {
        newList.addActionListener(e -> newListDialogue(""));
        removeList.addActionListener(e -> removeListDialogue(""));
        clearAll.addActionListener(e -> {
            dataBase.clearAll();
            showMainMenu();
        });
        save.addActionListener(e -> {
            saveDataBase();
            saveDialogue();
        });
        load.addActionListener(e -> {
            loadDataBase();
            showMainMenu();
        });
        quit.addActionListener(e -> quitDialogue());
    }

    // EFFECTS: produces new JPanel for adding new list
    private void newListDialogue(String message) {
        JPanel addListPanel = makeSubJPanel("Add List");
        JLabel error = new JLabel(message);
        JLabel instruction = new JLabel("Please enter unique name for the dataset.");
        JTextField textField = new JTextField(7);
        JButton enter = new JButton("Enter");
        addListPanel.add(error);
        addListPanel.add(instruction);
        addListPanel.add(textField);
        addListPanel.add(enter);
        enter.addActionListener(e -> handleTextEntry(textField.getText(), addListPanel));
    }

    // EFFECTS: handles the text entry for adding list
    private void handleTextEntry(String listName, JPanel subPanel) {
        boolean listExists;
        listExists = isInList(listName);
        if (listExists) {
            subFrame.dispose();
            subPanel.setVisible(false);
            newListDialogue("A list already has that name!");
        } else if (listName.equals("")) {
            subFrame.dispose();
            subPanel.setVisible(false);
            newListDialogue("Name cannot be empty!");
        } else {
            dataBase.addList(listName);
            subFrame.dispose();
            subPanel.setVisible(false);
            guiMainMenu();
        }
    }

    // EFFECTS: helper for making the smaller sub panel
    private static JPanel makeSubJPanel(String name) {
        subFrame = new JFrame(name);
        JPanel addListPanel = new JPanel();
        addListPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        addListPanel.setLayout(new GridLayout(5, 1));
        subFrame.setSize(500, 300);
        subFrame.add(addListPanel, BorderLayout.CENTER);
        subFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        subFrame.setLocationRelativeTo(null);
        subFrame.setVisible(true);
        return addListPanel;
    }

    // EFFECTS: produces new JPanel for removing existing list
    private void removeListDialogue(String message) {
        JPanel addListPanel = makeSubJPanel("Remove List");
        JLabel error = new JLabel(message);
        JLabel instruction = new JLabel("Please enter the name of the dataset you would like to remove.");
        JTextField textField = new JTextField(7);
        JButton enter = new JButton("Enter");
        addListPanel.add(error);
        addListPanel.add(instruction);
        addListPanel.add(textField);
        addListPanel.add(enter);
        enter.addActionListener(e -> handleRemoveTextEntry(textField.getText(), addListPanel));
    }

    // EFFECTS: handles the text entry for removing list
    private void handleRemoveTextEntry(String name, JPanel panel) {
        boolean listExists;
        listExists = isInList(name);
        if (name.equalsIgnoreCase("pooled list")) {
            subFrame.dispose();
            panel.setVisible(false);
            removeListDialogue("You cannot remove the Pooled List!");
        } else if (listExists) {
            ArrayList<Double> numbersToRemove = dataBase.removeList(name);
            for (double num : numbersToRemove) {
                dataBase.removeNumFromPool(num);
            }
            dataBase.removeList(name);
            subFrame.dispose();
            panel.setVisible(false);
            guiMainMenu();
        } else {
            subFrame.dispose();
            panel.setVisible(false);
            removeListDialogue("Given list does not exist!");
        }
    }


    // EFFECTS: produces new JPanel for saving database
    private void saveDialogue() {
        JPanel savePanel = makeSubJPanel("Saved!");
        JLabel message = new JLabel("Your database has been saved!");
        JButton close = new JButton("Close");
        close.addActionListener(e -> {
            subFrame.dispose();
            SwingUtilities.updateComponentTreeUI(subFrame);
            guiMainMenu();
        });
        savePanel.add(message);
        savePanel.add(close);
    }

    // MODIFIES: this
    // EFFECTS: produces new JPanel prompt for saving database when quitting
    private void quitDialogue() {
        JPanel savePanel = makeSubJPanel("Save Prompt");
        JLabel message = new JLabel("Would you like to save before quitting?");
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        yes.addActionListener(e -> {
            saveDataBase();
            printEventLog();
            System.exit(0);
        });
        no.addActionListener(e -> {
            printEventLog();
            System.exit(0);
        });
        savePanel.add(message);
        savePanel.add(yes);
        savePanel.add(no);
    }

    // MODIFIES: panel
    // EFFECTS: adds all buttons to main GUI menu
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

    // EFFECTS: prints out the event log into console
    private void printEventLog() {
        System.out.println("Event log: ");
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.toString() + ", ");
        }
    }


}
