package ui;

import model.DataSet;
import model.DataSets;

import java.util.*;

public class DataAnalysisApp {
    private DataSets dataSets;
    private Scanner input;
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
        if (order.equals("pooled list") || order.equals("Pooled List")) {
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
        System.out.println("Pooled Data:");
        System.out.println("-------------");
        for (double num : dataSets.getData(0).getList()) {
            System.out.println(num);
        }
        System.out.println("Mean: " + dataSets.getData(0).getListMean());
        System.out.println("Median: " + dataSets.getData(0).getListMedian());
        System.out.println("Variance: " + dataSets.getData(0).getListVar());
        System.out.println("Standard Deviation: " + dataSets.getData(0).getListSD());
    }

    private void listUI(DataSet data) {
        int addedNumber = 0;
        System.out.println(data.getListName() + ":");
        System.out.println("-------------");
        for (double num : data.getList()) {
            System.out.println(num);
        }
        addNum(data, addedNumber);
        System.out.println("Mean: " + data.getListMean());
        System.out.println("Median: " + data.getListMedian());
        System.out.println("Variance: " + data.getListVar());
        System.out.println("Standard Deviation: " + data.getListSD());
    }

    private void addNum(DataSet data, int num) {
        data.addNum(num);
        updateStats(data);
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
        showMainMenu();
    }

    private void initialize() {
        dataSets = new DataSets();
        dataSets.addList("Pooled List");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
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
