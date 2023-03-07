package persistence;

import model.DataBase;
import model.DataSet;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

// test class for JSON reader based on JSON reader test class from JSON serialization demo:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
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
            ArrayList<Double> pooledList = new ArrayList<>();
            checkDataBase("Database", 0, 0, 0, 0, 0, 0,
                    pooledList, dataBase);
        } catch (IOException e) {
            fail("File read failed");
        }
    }

    @Test
    void testReaderGeneralDataBase() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralDataBase.json");
        try {
            DataBase dataBase = reader.read();
            ArrayList<DataSet> dataSets = dataBase.getDataSets();
            ArrayList<Double> pooledList = new ArrayList<>();
            pooledList.add(100.0);
            pooledList.add(100.0);
            pooledList.add(50.0);
            pooledList.add(100.0);

            checkDataBase("Database", 2, 87.5, 100, 21.650635094610966,
                    4, 468.75, pooledList, dataBase);
            checkDataSet("list 1", 2,100,100,0,0, dataSets.get(0).getList(),
                    dataSets.get(0));
            checkDataSet("list 2", 2,75,75,25,625,
                    dataSets.get(1).getList(), dataSets.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
