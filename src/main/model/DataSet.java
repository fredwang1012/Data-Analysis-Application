package model;

import org.json.JSONObject;

import persistence.Writable;

import java.util.*;
import static java.lang.Math.*;


// Represents a dataset having a name, list length, and associated statistics
public class DataSet implements Writable {
    private final String listName;               // number list's name
    private double listLength;                   // length of number list
    private double listMean;                     // number list mean
    private double listMedian;                   // number list median
    private double listSD;                       // number list standard deviation
    private double listVar;                      // number list variance
    private ArrayList<Double> numList;           // number list


    // EFFECTS: name on list is set to listName; all associated statistics
    //          set to 0; make new number list
    public DataSet(String name) {
        listName = name;
        listLength = 0;
        listMean = 0;
        listMedian = 0;
        listSD = 0;
        listVar = 0;
        numList = new ArrayList<>();
    }

    // EFFECTS: returns list name
    public String getListName() {
        return listName;
    }

    // EFFECTS: returns list length
    public double getListLength() {
        return listLength;
    }

    // EFFECTS: returns list mean
    public double getListMean() {
        return listMean;
    }

    // EFFECTS: returns list median
    public double getListMedian() {
        return listMedian;
    }

    // EFFECTS: returns list standard deviation
    public double getListSD() {
        return listSD;
    }

    // EFFECTS: returns list variance
    public double getListVar() {
        return listVar;
    }

    // MODIFIES: this
    // EFFECTS: sets listLength to value of passed in list length
    public void setListLength(int listLength) {
        this.listLength = listLength;
    }

    // MODIFIES: this
    // EFFECTS: sets listMean to value of passed in list mean
    public void setListMean(double listMean) {
        this.listMean = listMean;
    }

    // MODIFIES: this
    // EFFECTS: sets listMedian to value of passed in list median
    public void setListMedian(double listMedian) {
        this.listMedian = listMedian;
    }

    // MODIFIES: this
    // EFFECTS: sets listSD to value of passed in list standard deviation
    public void setListSD(double listSD) {
        this.listSD = listSD;
    }

    // MODIFIES: this
    // EFFECTS: sets listVar to value of passed in list variance
    public void setListVar(double listVar) {
        this.listVar = listVar;
    }

    // MODIFIES: this
    // EFFECTS: sets numList to passed in number list
    public void setNumList(ArrayList<Double> numList) {
        this.numList = numList;
    }

    // MODIFIES: this
    // EFFECTS: adds a number to dataset
    public void addNum(double number) {
        numList.add(number);
        listLength++;
    }

    // MODIFIES: this
    // EFFECTS: removes number from dataset
    public boolean removeNum(double number) {
        for (int i = 0; i < listLength; i++) {
            if (numList.get(i) == number) {
                numList.remove(i);
                listLength--;
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: calculates and updates dataset mean
    public void calcMean() {
        double sum = 0;
        if (listLength == 0) {
            listMean = 0;
        } else {
            for (double value : numList) {
                sum += value;
            }
            listMean = sum / listLength;
        }
    }

    // MODIFIES: this
    // EFFECTS: calculates and updates dataset median
    public void calcMedian() {
        ArrayList<Double> tempList = (ArrayList) numList.clone();
        Collections.sort(tempList);
        if (listLength == 0) {
            listMedian = 0;
        } else if (listLength % 2 == 0) {
            listMedian = (tempList.get((int) (listLength / 2)) + tempList.get((int) (listLength / 2 - 1))) / 2;
        } else {
            listMedian = tempList.get((int) (listLength / 2));
        }
    }

    // MODIFIES: this
    // EFFECTS: calculates and updates dataset variance
    public void calcVariance() {
        if (listLength == 0) {
            listVar = 0;
            return;
        }
        double sum = 0;
        calcMean();
        for (double value : numList) {
            sum += Math.pow(value - listMean, 2);
        }
        listVar = sum / listLength;
    }

    // MODIFIES: this
    // EFFECTS: calculates and updates dataset standard deviation
    public void calcSD() {
        calcVariance();
        listSD = sqrt(listVar);
    }

/*    public double calcOneSampleZStat(double nullMean, double popSD) {
        return (listMean - nullMean) / (popSD / sqrt(listLength));
    }

    public double calcOneSampleZStatProp(double nullProp) {
        return (listMean - nullProp) / (sqrt(nullProp * (1 - nullProp) / listLength));
    }

    public double calcOneSampleTStat(double nullMean) {
        return (listMean - nullMean) / (listSD / sqrt(listLength));
    }

    public String calcConfIntervalProp(double confLevel) {
        double z;
        double lowerBound;
        double upperBound;

        z = getZ(confLevel);

        lowerBound = listMean - (z * sqrt(listMean * (1 - listMean) / listLength));
        upperBound = listMean + (z * sqrt(listMean * (1 - listMean) / listLength));

        return "[" + lowerBound + ", " + upperBound + "]";
    }
*/

    // MODIFIES: this
    // EFFECTS: calculates and updates dataset standard deviation and mean and calculates the confidence interval
    //          with given z score
    public String calcConfInterval(double z) {
        double lowerBound;
        double upperBound;

        if (listLength == 0) {
            return "[0, 0]";
        }

        calcSD();
        calcMean();
        lowerBound = listMean - (z * listSD / sqrt(listLength));
        upperBound = listMean + (z * listSD / sqrt(listLength));

        return "[" + lowerBound + ", " + upperBound + "]";
    }

    // EFFECTS: returns the estimated z score with given confidence level
    public static double getZ(double confLevel) {
        double z;
        if (confLevel == 0.997) {
            z = 3;
        } else if (confLevel == 0.99) {
            z = 2.576;
        } else if (confLevel == 0.95) {
            z = 1.96;
        } else if (confLevel == 0.9) {
            z = 1.645;
        } else if (confLevel == 0.8) {
            z = 1.282;
        } else if (confLevel == 0.75) {
            z = 1.15;
        } else if (confLevel == 0.5) {
            z = 0.68;
        } else {
            z = 0;
        }
        return z;
    }

    // MODIFIES: this
    // EFFECTS: sorts the dataset number list from small to large
    public void sortList() {
        Collections.sort(numList);
    }

    // EFFECTS: returns this dataset number list
    public ArrayList<Double> getList() {
        return numList;
    }

    // EFFECTS: converts this dataset into a JSONObject and returns the object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("listName", listName);
        json.put("listLength", listLength);
        json.put("listMean", listMean);
        json.put("listMedian", listMedian);
        json.put("listSD", listSD);
        json.put("listVar", listVar);
        json.put("numList", numList);
        return json;
    }
}
