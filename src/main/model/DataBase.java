package model;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.sqrt;

// Represents a database with multiple datasets stored in an ArrayList and list length
public class DataBase implements Writable {
    private static String name;                        // name of database
    private static int listLength;                     // length of database
    private static int pooledListLength;               // length of pooled list
    private double listMean;                           // number list mean
    private double listMedian;                         // number list median
    private double listSD;                             // number list standard deviation
    private double listVar;                            // number list variance
    private static ArrayList<Double> pooledList;       // pooled list of database values
    private static ArrayList<DataSet> listOfDatasets;  // dataset list

    // EFFECTS: constructs a new database with given name, initializes list of datasets and pooled list, and sets all
    //          associated statistics to 0
    public DataBase(String name) {
        DataBase.name = name;
        listLength = 0;
        pooledListLength = 0;
        listMean = 0;
        listMedian = 0;
        listSD = 0;
        listVar = 0;
        pooledList = new ArrayList<>();
        listOfDatasets = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a dataset with given String name to the database
    public void addList(String listName) {
        listOfDatasets.add(new DataSet(listName));
        listLength++;
    }

    // MODIFIES: this
    // EFFECTS: adds the given number to the pooled list
    public void addNumToPool(Double number) {
        pooledList.add(number);
        pooledListLength++;
    }

    // MODIFIES: this
    // EFFECTS: removes the given number from the pooled list
    public void removeNumFromPool(Double number) {
        pooledList.remove(number);
        pooledListLength--;
    }

    // MODIFIES: this
    // EFFECTS: removes a dataset with given String name from the database and returns numbers from removed list
    public ArrayList<Double> removeList(String listName) {
        ArrayList<Double> tempData = new ArrayList<>();
        for (int i = 0; i < listLength; i++) {
            if (listOfDatasets.get(i).getListName().equalsIgnoreCase(listName)) {
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
        pooledList.clear();
        listLength = 0;
        pooledListLength = 0;
        listMean = 0;
        listMedian = 0;
        listSD = 0;
        listVar = 0;
    }

    // EFFECTS: returns the pooled list of numbers
    public ArrayList<Double> getPooledList() {
        return pooledList;
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

    // EFFECTS: returns this database's pooled list length
    public int getPooledListLength() {
        return pooledListLength;
    }

    // EFFECTS: returns the DataSet at given index of dataset
    public DataSet getData(int index) {
        return listOfDatasets.get(index);
    }

    // EFFECTS: returns pooled list mean
    public double getListMean() {
        return listMean;
    }

    // EFFECTS: returns pooled list median
    public double getListMedian() {
        return listMedian;
    }

    // EFFECTS: returns pooled list standard deviation
    public double getListSD() {
        return listSD;
    }

    // EFFECTS: returns pooled list variance
    public double getListVar() {
        return listVar;
    }

    // MODIFIES: this
    // EFFECTS: sets listLength to value of passed in list length
    public void setListLength(int listLength) {
        DataBase.listLength = listLength;
    }

    // MODIFIES: this
    // EFFECTS: sets pooled list length to value of passed in list length
    public void setPooledListLength(int listLength) {
        pooledListLength = listLength;
    }

    // MODIFIES: this
    // EFFECTS: sets pooled list mean to value of passed in list mean
    public void setListMean(double listMean) {
        this.listMean = listMean;
    }

    // MODIFIES: this
    // EFFECTS: sets pooled list median to value of passed in list median
    public void setListMedian(double listMedian) {
        this.listMedian = listMedian;
    }

    // MODIFIES: this
    // EFFECTS: sets pooled list standard deviation to value of passed in list standard deviation
    public void setListSD(double listSD) {
        this.listSD = listSD;
    }

    // MODIFIES: this
    // EFFECTS: sets pooled list variance to value of passed in list variance
    public void setListVar(double listVar) {
        this.listVar = listVar;
    }

    // MODIFIES: this
    // EFFECTS: sets pooled list to passed in number list
    public void setPooledList(ArrayList<Double> numList) {
        pooledList = numList;
    }

    // MODIFIES: this
    // EFFECTS: calculates and updates pooled list mean
    public void calcMean() {
        double sum = 0;
        if (pooledListLength == 0) {
            listMean = 0;
        } else {
            for (double value : pooledList) {
                sum += value;
            }
            listMean = sum / pooledListLength;
        }
    }

    // MODIFIES: this
    // EFFECTS: calculates and updates pooled list median
    public void calcMedian() {
        ArrayList<Double> tempList = (ArrayList) pooledList.clone();
        Collections.sort(tempList);
        if (pooledListLength == 0) {
            listMedian = 0;
        } else if (pooledListLength % 2 == 0) {
            listMedian = (tempList.get(pooledListLength / 2) + tempList.get(pooledListLength / 2 - 1))
                    / 2;
        } else {
            listMedian = tempList.get(pooledListLength / 2);
        }
    }

    // MODIFIES: this
    // EFFECTS: calculates and updates pooled list variance
    public void calcVariance() {
        if (pooledListLength == 0) {
            listVar = 0;
            return;
        }
        double sum = 0;
        calcMean();
        for (double value : pooledList) {
            sum += Math.pow(value - listMean, 2);
        }
        listVar = sum / pooledListLength;
    }

    // MODIFIES: this
    // EFFECTS: calculates and updates pooled list standard deviation
    public void calcSD() {
        calcVariance();
        listSD = sqrt(listVar);
    }

/*    public double calcOneSampleZStat(double nullMean, double popSD) {
        return (listMean - nullMean) / (popSD / sqrt(pooledListLength));
    }

    public double calcOneSampleZStatProp(double nullProp) {
        return (listMean - nullProp) / (sqrt(nullProp * (1 - nullProp) / pooledListLength));
    }

    public double calcOneSampleTStat(double nullMean) {
        return (listMean - nullMean) / (listSD / sqrt(pooledListLength));
    }

    public String calcConfIntervalProp(double confLevel) {
        double z;
        double lowerBound;
        double upperBound;

        z = getZ(confLevel);

        lowerBound = listMean - (z * sqrt(listMean * (1 - listMean) / pooledListLength));
        upperBound = listMean + (z * sqrt(listMean * (1 - listMean) / pooledListLength));

        return "[" + lowerBound + ", " + upperBound + "]";
    }
*/

    // MODIFIES: this
    // EFFECTS: calculates and updates pooled list standard deviation and mean and calculates the confidence interval
    //          with given z score
    public String calcConfInterval(double z) {
        double lowerBound;
        double upperBound;

        if (pooledListLength == 0) {
            return "[0, 0]";
        }

        calcSD();
        calcMean();
        lowerBound = listMean - (z * listSD / sqrt(pooledListLength));
        upperBound = listMean + (z * listSD / sqrt(pooledListLength));

        return "[" + lowerBound + ", " + upperBound + "]";
    }

    // MODIFIES: this
    // EFFECTS: sorts the pooled number list from small to large
    public void sortList() {
        Collections.sort(pooledList);
    }

    // EFFECTS: returns JSONObject version of database
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("listLength", listLength);
        json.put("listOfDatasets", dataSetsToJson());
        json.put("listMean", listMean);
        json.put("listMedian", listMedian);
        json.put("listSD", listSD);
        json.put("listVar", listVar);
        json.put("pooledListLength", pooledListLength);
        json.put("pooledList", pooledListToJson());
        return json;
    }

    // EFFECTS: returns JSONArray version of the pooled number list in database
    private JSONArray pooledListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (double num : pooledList) {
            jsonArray.put(num);
        }
        return jsonArray;
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
