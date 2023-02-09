package model;

import java.util.*;

import static java.lang.Math.*;


// Represents a data list having a name, list length, and associated statistics
public class DataSet {
    private String listName;               // number list's name
    private int listLength;                // length of number list
    private double listMean;               // number list mean
    private double listMedian;             // number list median
    private double listSD;                 // number list standard deviation
    private double listVar;                // number list variance
    private ArrayList<Double> numList;     // number list

    /*
     * REQUIRES: name has a non-zero length
     * EFFECTS: name on list is set to listName; all associated statistics
     *          set to 0; make new number list
     */
    public DataSet(String name) {
        listName = name;
        listLength = 0;
        listMean = 0;
        listMedian = 0;
        listSD = 0;
        listVar = 0;
        numList = new ArrayList<>();
    }

    public String getListName() {
        return listName;
    }

    public int getListLength() {
        return listLength;
    }

    public double getListMean() {
        return listMean;
    }

    public double getListMedian() {
        return listMedian;
    }

    public double getListSD() {
        return listSD;
    }

    public double getListVar() {
        return listVar;
    }

    public void addNum(double number) {
        numList.add(number);
        listLength++;
    }

    public boolean removeNum(double number) {
        for (int i = listLength; i > 0; i--) {
            if (numList.get(i) == number) {
                numList.remove(i);
                listLength--;
                return true;
            }
        }
        return false;
    }

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

    public void calcMedian() {
        ArrayList<Double> tempList = (ArrayList) numList.clone();
        Collections.sort(tempList);
        if (listLength % 2 == 0) {
            listMedian = (tempList.get(listLength / 2) + tempList.get(listLength / 2 + 1)) / 2;
        } else {
            listMedian = tempList.get(listLength / 2 + 1);
        }
    }

    public void calcVariance() {
        double sum = 0;
        calcMean();
        for (double value : numList) {
            sum += Math.pow(value - listMean, 2);
        }
        listVar = sum / listLength;
    }

    public void calcSD() {
        calcVariance();
        listSD = sqrt(listVar);
    }

    public double calcOneSampleZStat(double nullMean, double popSD) {
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

    public String calcConfInterval(double confLevel) {
        double z;
        double lowerBound;
        double upperBound;

        z = getZ(confLevel);

        lowerBound = listMean - (z * listSD / sqrt(listLength));
        upperBound = listMean + (z * listSD / sqrt(listLength));

        return "[" + lowerBound + ", " + upperBound + "]";
    }

    private static double getZ(double confLevel) {
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
        } else if (confLevel == 0.68) {
            z = 1.15;
        } else {
            z = 0;
        }
        return z;
    }

    public void sortList() {
        Collections.sort(numList);
    }

}
