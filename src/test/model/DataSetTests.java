package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// test class for DataSet model class
class DataSetTests {
    private DataSet testDataSet;

    @BeforeEach
    public void setUp() {
        testDataSet = new DataSet("test");
    }

    @Test
    public void testConstructor() {
        assertEquals("test", testDataSet.getListName());
        assertEquals(0, testDataSet.getListLength());
    }

    @Test
    public void testAddNum() {
        testDataSet.addNum(10);
        assertEquals(1, testDataSet.getListLength());
        testDataSet.addNum(20);
        assertEquals(2, testDataSet.getListLength());
        assertEquals(10, testDataSet.getList().get(0));
        assertEquals(20, testDataSet.getList().get(1));
    }

    @Test
    public void testRemoveNumber() {
        assertFalse(testDataSet.removeNum(90));
        testDataSet.addNum(10);
        testDataSet.addNum(20);
        assertTrue(testDataSet.removeNum(10));
        assertFalse(testDataSet.removeNum(100));
        assertEquals(1, testDataSet.getListLength());
        assertTrue(testDataSet.removeNum(20));
        assertEquals(0, testDataSet.getListLength());
    }

    @Test
    public void testCalcMean() {
        testDataSet.calcMean();
        assertEquals(0, testDataSet.getListMean());
        testDataSet.addNum(50);
        testDataSet.calcMean();
        assertEquals(50, testDataSet.getListMean());
        testDataSet.addNum(100);
        testDataSet.calcMean();
        assertEquals(75, testDataSet.getListMean());
    }

    @Test
    public void testCalcMedian() {
        testDataSet.calcMedian();
        assertEquals(0, testDataSet.getListMedian());
        testDataSet.addNum(50);
        testDataSet.calcMedian();
        assertEquals(50, testDataSet.getListMedian());
        testDataSet.addNum(10);
        testDataSet.calcMedian();
        assertEquals(30, testDataSet.getListMedian());
        testDataSet.addNum(100);
        testDataSet.calcMedian();
        assertEquals(50, testDataSet.getListMedian());
    }

    @Test
    public void testCalcVariance() {
        testDataSet.calcVariance();
        assertEquals(0, testDataSet.getListVar());
        testDataSet.addNum(50);
        testDataSet.calcVariance();
        assertEquals(0, testDataSet.getListVar());
        testDataSet.addNum(10);
        testDataSet.calcVariance();
        assertEquals(400, testDataSet.getListVar());
        testDataSet.addNum(10);
        testDataSet.addNum(30);
        testDataSet.calcVariance();
        assertEquals(275, testDataSet.getListVar());
    }

    @Test
    public void testCalcSD() {
        testDataSet.calcSD();
        assertEquals(0, testDataSet.getListSD());
        testDataSet.addNum(10);
        testDataSet.calcSD();
        assertEquals(0, testDataSet.getListSD());
        testDataSet.addNum(90);
        testDataSet.calcSD();
        assertEquals(40, testDataSet.getListSD());
    }

    @Test
    public void testCalcConfInterval() {
        assertEquals("[0, 0]", testDataSet.calcConfInterval(1.96));
        testDataSet.addNum(50);
        assertEquals("[50.0, 50.0]", testDataSet.calcConfInterval(1.96));
        testDataSet.addNum(100);
        assertEquals("[21.96699141100894, 128.03300858899107]", testDataSet.calcConfInterval(3));
        assertEquals("[29.462323291586337, 120.53767670841367]", testDataSet.calcConfInterval(2.576));
        assertEquals("[40.351767721859176, 109.64823227814082]", testDataSet.calcConfInterval(1.96));
        assertEquals("[45.92023362370324, 104.07976637629676]", testDataSet.calcConfInterval(1.645));
        assertEquals("[52.33722766297116, 97.66277233702884]", testDataSet.calcConfInterval(1.282));
        assertEquals("[54.670680040886765, 95.32931995911323]", testDataSet.calcConfInterval(1.15));
        assertEquals("[62.979184719828694, 87.0208152801713]", testDataSet.calcConfInterval(0.68));
        assertEquals("[75.0, 75.0]", testDataSet.calcConfInterval(0));
    }
    @Test
    public void testGetZ() {
        assertEquals(3, DataSet.getZ(0.997));
        assertEquals(2.576, DataSet.getZ(0.99));
        assertEquals(1.96, DataSet.getZ(0.95));
        assertEquals(1.645, DataSet.getZ(0.9));
        assertEquals(1.282, DataSet.getZ(0.8));
        assertEquals(1.15, DataSet.getZ(0.75));
        assertEquals(0.68, DataSet.getZ(0.5));
        assertEquals(0, DataSet.getZ(0.1));
    }

    @Test
    public void testSortList() {
        ArrayList<Double> testList = new ArrayList<>();
        testDataSet.addNum(10);
        testDataSet.sortList();
        testList.add(10.0);
        assertEquals(testList, testDataSet.getList());
        testList.clear();
        testDataSet.addNum(5);
        testDataSet.sortList();
        testList.add(5.0);
        testList.add(10.0);
        assertEquals(testList, testDataSet.getList());

    }
}
