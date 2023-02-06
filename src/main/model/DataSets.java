package model;

import java.util.ArrayList;

public class DataSets {
    private static ArrayList<DataSet> listOfDatasets;
    private static int listLength;

    public DataSets() {
        listOfDatasets = new ArrayList<>();
        listLength = 0;
    }

    public void addList(String listName) {
        DataSet tempList = new DataSet(listName);
        listOfDatasets.add(tempList);
    }

    public boolean removeList(String listName) {
        for (int i = listLength; i > 0; i--) {
            if (listOfDatasets.get(i).getListName() == listName) {
                listOfDatasets.remove(i);
                return true;
            }
        }
        return false;
    }

    public void clearAll() {
        listOfDatasets.clear();
    }

}
