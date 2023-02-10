package model;

import java.util.ArrayList;
import model.DataSet;

public class DataSets {
    private static ArrayList<DataSet> listOfDatasets;
    private static int listLength;

    public DataSets() {
        listOfDatasets = new ArrayList<>();
        listLength = 0;
    }

    public void addList(String listName) {
        listOfDatasets.add(new DataSet(listName));
        listLength++;
    }

    public boolean removeList(String listName) {
        for (int i = listLength; i > 0; i--) {
            if (listOfDatasets.get(i).getListName() == listName) {
                listOfDatasets.remove(i);
                listLength--;
                return true;
            }
        }
        return false;
    }

    public void clearAll() {
        listOfDatasets.clear();
        listLength = 0;
    }

    public ArrayList<DataSet> getDataSet() {
        return listOfDatasets;
    }

    public int getListLength() {
        return listLength;
    }

    public DataSet getData(int index) {
        return listOfDatasets.get(index);
    }

}
