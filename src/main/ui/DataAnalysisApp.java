package ui;

import model.DataSet;
import model.DataSets;

import java.util.*;

public class DataAnalysisApp {
    private DataSets dataSets;
    private Scanner input;
    private Scanner innerInput;
    private boolean backToMainMenu = false;

    public DataAnalysisApp() {
        runApp();
    }

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

    private void processOrder(String order) {
        if (order.toLowerCase().equals("pooled list")) {
            pooledUI();
        } else if (isInList(order)) {
            for (DataSet data : dataSets.getDataSet()) {
                if (data.getListName().equals(order)) {
                    listUI(data);
                }
            }
        } else if (order.equals("nl")) {
            System.out.println("Please give your list a name:");
            order = input.next();
            makeNewList(order);
        } else {
            System.out.println("Invalid input!");
        }
    }

    private void pooledUI() {
        String userInput;
        updateStats(dataSets.getData(0));
        System.out.println("Pooled Data:");
        System.out.println("-------------");
        for (double num : dataSets.getData(0).getList()) {
            System.out.println(num);
        }
        System.out.println("Mean: " + dataSets.getData(0).getListMean());
        System.out.println("Median: " + dataSets.getData(0).getListMedian());
        System.out.println("Variance: " + dataSets.getData(0).getListVar());
        System.out.println("Standard Deviation: " + dataSets.getData(0).getListSD());
        System.out.println();
        System.out.println("Enter a command");
        System.out.println("\tb -> Back");
        userInput = innerInput.next();
        if (userInput.toLowerCase().equals("b")) {
            return;
        } else {
            System.out.println("Invalid input!");
            pooledUI();
        }
    }

    private void listUI(DataSet data) {
        String userInput;
        System.out.println(data.getListName() + ":");
        System.out.println("-------------");
        for (double num : data.getList()) {
            System.out.println(num);
        }
        System.out.println("Mean: " + data.getListMean());
        System.out.println("Median: " + data.getListMedian());
        System.out.println("Variance: " + data.getListVar());
        System.out.println("Standard Deviation: " + data.getListSD());
        System.out.println("Enter a command");
        System.out.println("\tan -> Add Number");
        System.out.println("\trn -> Remove Number");
        System.out.println("\tb -> Back");
        userInput = innerInput.next();
        listInputProcessor(data, userInput);
    }

    private void listInputProcessor(DataSet data, String userInput) {
        if (userInput.equals("an")) {
            addNum(data);
        } else if (userInput.equals("rn")) {
            removeNum(data);
        } else if (userInput.equals("b")) {
            return;
        } else {
            System.out.println("Invalid input");
            listUI(data);
        }
    }

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
                dataSets.getData(0).addNum(number);
                updateStats(data);
                listUI(data);
            } else {
                System.out.println("Number not in list, please try again!");
                removeNum(data);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid Input!");
            System.err.println("Please try again:");
            removeNum(data);
        }
    }

    private void addNum(DataSet data) {
        double number = 0;
        String numberInput;
        System.out.println();
        System.out.println("Enter number:");
        numberInput = input.next();
        try {
            number = Double.parseDouble(numberInput);
            data.addNum(number);
            dataSets.getData(0).addNum(number);
            updateStats(data);
            listUI(data);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Input!");
            System.err.println("Please try again:");
            addNum(data);
        }
    }

    private void updateStats(DataSet data) {
        data.calcMean();
        data.calcMedian();
        data.calcVariance();
        data.calcSD();
    }

    private boolean isInList(String order) {
        for (DataSet data : dataSets.getDataSet()) {
            if (data.getListName().equals(order)) {
                return true;
            }
        }
        return false;
    }

    private void makeNewList(String listName) {
        dataSets.addList(listName);
    }

    private void initialize() {
        dataSets = new DataSets();
        dataSets.addList("Pooled List");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        innerInput = new Scanner(System.in);
        innerInput.useDelimiter("\n");
    }

    private void showMainMenu() {
        System.out.println("Please select from following: ");
        for (DataSet data : dataSets.getDataSet()) {
            System.out.println("\t" + data.getListName());
        }
        System.out.println("\n\tnl -> New List");
        System.out.println("\n\tq -> Quit");
    }


}
