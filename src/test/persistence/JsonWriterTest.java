package persistence;

import model.DataBase;
import model.DataSet;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{
    @Test
    void testWriterInvalidFile() {
        try {
            DataBase dataBase = new DataBase("DataBase");
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
            DataBase dataBase = new DataBase("DataBase");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDataBase.json");
            writer.open();
            writer.write(dataBase);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyDataBase.json");
            dataBase = reader.read();
            assertEquals(0, dataBase.getListLength());
            assertEquals("DataBase", dataBase.getName());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralDataBase() {
        try {
            DataBase dataBase = new DataBase("DataBase");
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
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralDataBase.json");
            writer.open();
            writer.write(dataBase);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralDataBase.json");
            dataBase = reader.read();;
            assertEquals("DataBase", dataBase.getName());
            ArrayList<DataSet> dataSets = dataBase.getDataSets();
            assertEquals(2, dataSets.size());
            checkDataSet("List1", 2,11.3,11.3,1.1000000000000005,
                    1.2100000000000013, dataSets.get(0).getList(), dataSets.get(0));
            checkDataSet("List2", 2,10,10,0,0,
                    dataSets.get(1).getList(), dataSets.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
