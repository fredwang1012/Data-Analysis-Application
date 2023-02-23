package persistence;

import model.DataBase;
import model.DataSet;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/fakeDatabase.json");
        try {
            DataBase dataBase = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // test passed
        }
    }

    @Test
    void testReaderEmptyDataBase() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyDataBase.json");
        try {
            DataBase dataBase = reader.read();
            ArrayList<DataSet> dataSets = dataBase.getDataSets();
            ArrayList<Double> numList = dataSets.get(0).getList();
            assertEquals(1, dataSets.size());
            assertEquals("DataBase", dataBase.getName());
            checkDataSet("Pooled List", 0,0,0,0,0, numList, dataSets.get(0));
        } catch (IOException e) {
            fail("File read failed");
        }
    }

    @Test
    void testReaderGeneralDataBase() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralDataBase.json");
        try {
            DataBase dataBase = reader.read();
            assertEquals("DataBase", dataBase.getName());
            ArrayList<DataSet> dataSets = dataBase.getDataSets();
            assertEquals(3, dataSets.size());
            checkDataSet("Pooled List", 4,87.5,100,21.650635094610966,
                    468.75, dataSets.get(0).getList(), dataSets.get(0));
            checkDataSet("List1", 2,100,100,0,0, dataSets.get(1).getList(),
                    dataSets.get(1));
            checkDataSet("List2", 2,75,75,25,625,
                    dataSets.get(2).getList(), dataSets.get(2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
