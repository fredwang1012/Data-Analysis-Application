package persistence;

import model.DataBase;
import model.DataSet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;


// test class for JSON writer based on JSON writer test class from JSON serialization demo
public class JsonWriterTest extends JsonTest{
    @Test
    void testWriterInvalidFile() {
        try {
            DataBase dataBase = new DataBase("Database");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyDataBase(){
        try {
            DataBase dataBase = new DataBase("Database");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDataBase.json");
            writer.open();
            writer.write(dataBase);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyDataBase.json");
            dataBase = reader.read();
            ArrayList<Double> tempList = new ArrayList<>();
            checkDataBase("Database", 0, 0, 0, 0, 0, 0,
                    tempList, dataBase);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralDataBase() {
        try {
            DataBase dataBase = new DataBase("Database");
            dataBase.addList("List1");
            dataBase.addList("List2");
            dataBase.getData(0).addNum(10.2);
            dataBase.getData(0).addNum(12.4);
            dataBase.getData(0).calcMean();
            dataBase.getData(0).calcMedian();
            dataBase.getData(0).calcSD();
            dataBase.getData(0).calcVariance();
            dataBase.getData(1).addNum(10);
            dataBase.getData(1).addNum(10);
            dataBase.getData(1).calcMean();
            dataBase.getData(1).calcMedian();
            dataBase.getData(1).calcSD();
            dataBase.getData(1).calcVariance();
            dataBase.addNumToPool(10.2);
            dataBase.addNumToPool(12.4);
            dataBase.addNumToPool(10.0);
            dataBase.addNumToPool(10.0);
            dataBase.calcMean();
            dataBase.calcSD();
            dataBase.calcMedian();
            dataBase.calcVariance();

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralDataBase.json");
            writer.open();
            writer.write(dataBase);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralDataBase.json");
            dataBase = reader.read();
            ArrayList<DataSet> dataSets = dataBase.getDataSets();
            ArrayList<Double> tempNumList = new ArrayList<>();
            tempNumList.add(10.2);
            tempNumList.add(12.4);
            tempNumList.add(10.0);
            tempNumList.add(10.0);
            checkDataBase("Database", 2, 10.65, 10.1, 1.013656746635665,
                    4, 1.0275000000000005, tempNumList, dataBase);
            checkDataSet("List1", 2,11.3,11.3,1.1000000000000005,
                    1.2100000000000013, dataSets.get(0).getList(), dataSets.get(0));
            checkDataSet("List2", 2,10,10,0,0,
                    dataSets.get(1).getList(), dataSets.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
