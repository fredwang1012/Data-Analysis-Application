package model;

import java.util.ArrayList;

// represents a database with multiple datasets stored in an ArrayList and list length
public class DataBase {
    private static ArrayList<DataSet> listOfDatasets;  // dataset list
    private static int listLength;                     // length of database

    // EFFECTS: constructs a new database with length set to 0
    public DataBase() {
        listOfDatasets = new ArrayList<>();
        listLength = 0;
    }

    // MODIFIES: this
    // EFFECTS: adds a dataset with given String name to the database
    public void addList(String listName) {
        listOfDatasets.add(new DataSet(listName));
        listLength++;
    }

    // MODIFIES: this
    // EFFECTS: removes a dataset with given String name from the database
    public boolean removeList(String listName) {
        DataSet tempData;
        for (int i = 0; i < listLength; i++) {
            if (listOfDatasets.get(i).getListName().equals(listName)) {
                tempData = listOfDatasets.get(i);
                for (double num : tempData.getList()) {
                    listOfDatasets.get(0).removeNum(num);
                }
                listOfDatasets.remove(i);
                listLength--;
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: clears the database
    public void clearAll() {
        listOfDatasets.clear();
        listLength = 0;
    }

    // EFFECTS: returns this database
    public ArrayList<DataSet> getDataBase() {
        return listOfDatasets;
    }

    // EFFECTS: returns this database's length
    public int getListLength() {
        return listLength;
    }

    // EFFECTS: returns the DataSet at given index of dataset
    public DataSet getData(int index) {
        return listOfDatasets.get(index);
    }

}
