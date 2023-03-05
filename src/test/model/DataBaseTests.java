package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Test class for DataBase model class
class DataBaseTests {
    private DataBase testDataBase;

    @BeforeEach
    public void setUp() {
        testDataBase = new DataBase("Database");
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testDataBase.getListLength());
    }

    @Test
    public void testAddList() {
        testDataBase.addList("test list");
        assertEquals("test list", testDataBase.getData(0).getListName());
        assertEquals(1, testDataBase.getListLength());
        testDataBase.addList("test list 2");
        assertEquals("test list 2", testDataBase.getData(1).getListName());
        assertEquals(2, testDataBase.getListLength());
    }

    @Test
    public void testRemoveList() {
        testDataBase.addList("test 1");
        testDataBase.addList("test 2");
        testDataBase.getData(0).addNum(5);
        testDataBase.getData(1).addNum(10);
        testDataBase.getData(1).addNum(20);
        assertEquals(0, testDataBase.removeList("").size());
        assertEquals(1, testDataBase.removeList("test 1").size());
        assertEquals(1, testDataBase.getListLength());
        assertEquals(2, testDataBase.removeList("test 2").size());
        assertEquals(0, testDataBase.getListLength());
    }

    @Test
    public void testClearAll() {
        testDataBase.addList("test 1");
        testDataBase.addList("test 2");
        testDataBase.clearAll();
        assertEquals(0, testDataBase.getListLength());
    }

    @Test
    public void testGetDataBase() {
        assertEquals(0, testDataBase.getDataSets().size());
        testDataBase.addList("test 1");
        assertEquals(1, testDataBase.getDataSets().size());
    }

    @Test
    public void testCalcMean() {
        testDataBase.calcMean();
        assertEquals(0, testDataBase.getListMean());
        testDataBase.addNumToPool(50.0);
        testDataBase.calcMean();
        assertEquals(50, testDataBase.getListMean());
        testDataBase.addNumToPool(100.0);
        testDataBase.calcMean();
        assertEquals(75, testDataBase.getListMean());
    }

    @Test
    public void testCalcMedian() {
        testDataBase.calcMedian();
        assertEquals(0, testDataBase.getListMedian());
        testDataBase.addNumToPool(50.0);
        testDataBase.calcMedian();
        assertEquals(50, testDataBase.getListMedian());
        testDataBase.addNumToPool(10.0);
        testDataBase.calcMedian();
        assertEquals(30, testDataBase.getListMedian());
        testDataBase.addNumToPool(100.0);
        testDataBase.calcMedian();
        assertEquals(50, testDataBase.getListMedian());
    }

    @Test
    public void testCalcVariance() {
        testDataBase.calcVariance();
        assertEquals(0, testDataBase.getListVar());
        testDataBase.addNumToPool(50.0);
        testDataBase.calcVariance();
        assertEquals(0, testDataBase.getListVar());
        testDataBase.addNumToPool(10.0);
        testDataBase.calcVariance();
        assertEquals(400, testDataBase.getListVar());
        testDataBase.addNumToPool(10.0);
        testDataBase.addNumToPool(30.0);
        testDataBase.calcVariance();
        assertEquals(275, testDataBase.getListVar());
    }

    @Test
    public void testCalcSD() {
        testDataBase.calcSD();
        assertEquals(0, testDataBase.getListSD());
        testDataBase.addNumToPool(10.0);
        testDataBase.calcSD();
        assertEquals(0, testDataBase.getListSD());
        testDataBase.addNumToPool(90.0);
        testDataBase.calcSD();
        assertEquals(40, testDataBase.getListSD());
    }

    @Test
    public void testCalcConfInterval() {
        assertEquals("[0, 0]", testDataBase.calcConfInterval(1.96));
        testDataBase.addNumToPool(50.0);
        assertEquals("[50.0, 50.0]", testDataBase.calcConfInterval(1.96));
        testDataBase.addNumToPool(100.0);
        assertEquals("[21.96699141100894, 128.03300858899107]", testDataBase.calcConfInterval(3));
        assertEquals("[29.462323291586337, 120.53767670841367]", testDataBase.calcConfInterval(2.576));
        assertEquals("[40.351767721859176, 109.64823227814082]", testDataBase.calcConfInterval(1.96));
        assertEquals("[45.92023362370324, 104.07976637629676]", testDataBase.calcConfInterval(1.645));
        assertEquals("[52.33722766297116, 97.66277233702884]", testDataBase.calcConfInterval(1.282));
        assertEquals("[54.670680040886765, 95.32931995911323]", testDataBase.calcConfInterval(1.15));
        assertEquals("[62.979184719828694, 87.0208152801713]", testDataBase.calcConfInterval(0.68));
        assertEquals("[75.0, 75.0]", testDataBase.calcConfInterval(0));
    }

    @Test
    public void testAddNumToPool() {
        ArrayList<Double> pooledList = new ArrayList<>();
        testDataBase.addNumToPool(50.0);
        pooledList.add(50.0);
        assertEquals(pooledList, testDataBase.getPooledList());
        assertEquals(1, testDataBase.getPooledListLength());
    }

    @Test
    public void testRemoveNumFromPool() {
        ArrayList<Double> pooledList = new ArrayList<>();
        testDataBase.addNumToPool(50.0);
        testDataBase.removeNumFromPool(50.0);
        assertEquals(pooledList, testDataBase.getPooledList());
        assertEquals(0, testDataBase.getPooledListLength());
    }

    @Test
    public void testGetName() {
        assertEquals("Database", testDataBase.getName());
    }

    @Test
    public void testGetPooledListLength() {
        assertEquals(0, testDataBase.getPooledListLength());
        testDataBase.addNumToPool(10.0);
        assertEquals(1, testDataBase.getPooledListLength());
    }

    @Test
    public void testGetPooledList() {
        ArrayList<Double> pooledList = new ArrayList<>();
        assertEquals(pooledList, testDataBase.getPooledList());
        pooledList.add(10.0);
        testDataBase.addNumToPool(10.0);
        assertEquals(pooledList, testDataBase.getPooledList());
    }

    @Test
    public void testSortList() {
        ArrayList<Double> pooledList = new ArrayList<>();
        pooledList.add(10.0);
        pooledList.add(20.0);
        testDataBase.addNumToPool(20.0);
        testDataBase.addNumToPool(10.0);
        testDataBase.sortList();
        assertEquals(pooledList, testDataBase.getPooledList());
    }


}