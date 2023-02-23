package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Test class for DataBase model class
class DataBaseTests {
    private DataBase testDataBase;

    @BeforeEach
    public void setUp() {
        testDataBase = new DataBase("DataBase");
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
        assertEquals(0, testDataBase.getDataBase().size());
        testDataBase.addList("test 1");
        assertEquals(1, testDataBase.getDataBase().size());
    }

}