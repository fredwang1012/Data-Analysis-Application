package persistence;

import model.DataSet;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

// superclass for JSON tests based on JSON serialization demo
public class JsonTest {
    public void checkDataSet(String listName, int listLength, double listMean, double listMedian, double listSD,
                             double listVar, ArrayList<Double> numList, DataSet dataSet) {
        assertEquals(listName, dataSet.getListName());
        assertEquals(listLength, dataSet.getListLength());
        assertEquals(listMean, dataSet.getListMean());
        assertEquals(listMedian, dataSet.getListMedian());
        assertEquals(listSD, dataSet.getListSD());
        assertEquals(listVar, dataSet.getListVar());
        assertEquals(numList, dataSet.getList());
    }
}
