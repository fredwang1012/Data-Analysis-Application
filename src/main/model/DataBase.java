package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import java.util.ArrayList;

// Represents a database with multiple datasets stored in an ArrayList and list length
public class DataBase implements Writable {
    private static ArrayList<DataSet> listOfDatasets;  // dataset list
    private static int listLength;                     // length of database
    private static String name;

    // EFFECTS: constructs a new database with length set to 0
    public DataBase(String name) {
        listOfDatasets = new ArrayList<>();
        listLength = 0;
        DataBase.name = name;
    }

    // MODIFIES: this
    // EFFECTS: adds a dataset with given String name to the database
    public void addList(String listName) {
        listOfDatasets.add(new DataSet(listName));
        listLength++;
    }

    // MODIFIES: this
    // EFFECTS: removes a dataset with given String name from the database and returns numbers from removed list
    public ArrayList<Double> removeList(String listName) {
        ArrayList<Double> tempData = new ArrayList<>();
        for (int i = 0; i < listLength; i++) {
            if (listOfDatasets.get(i).getListName().equals(listName)) {
                tempData = listOfDatasets.get(i).getList();
                listOfDatasets.remove(i);
                listLength--;
                return tempData;
            }
        }
        return tempData;
    }

    // MODIFIES: this
    // EFFECTS: clears the database
    public void clearAll() {
        listOfDatasets.clear();
        listLength = 0;
    }

    // EFFECTS: returns database name
    public String getName() {
        return name;
    }

    // EFFECTS: returns this database
    public ArrayList<DataSet> getDataSets() {
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

    // EFFECTS: returns JSONObject version of database
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("listLength", listLength);
        json.put("listOfDatasets", dataSetsToJson());
        return json;
    }

    // EFFECTS: returns JSONArray version of the dataset list in database
    private JSONArray dataSetsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (DataSet dataSet : listOfDatasets) {
            jsonArray.put(dataSet.toJson());
        }
        return jsonArray;
    }
}
