package model;

import java.util.ArrayList;


// Represents a data list having a name, list length, and associated statistics
public class DataList {
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
    public DataList(String name) {
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
    }
}
