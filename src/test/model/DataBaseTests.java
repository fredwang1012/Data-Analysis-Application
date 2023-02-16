package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
class DataBaseTests {
    private DataBase test;

    @BeforeEach
    public void setUp() {
        test = new DataBase();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, test.getListLength());
    }

    @Test
    public void testAddList() {
        test.addList("test list");
        assertEquals("test list", test.getData(0).getListName());
        assertEquals(1, test.getListLength());
        test.addList("test list 2");
        assertEquals("test list 2", test.getData(1).getListName());
        assertEquals(2, test.getListLength());
    }

    @Test
    public void testRemoveList() {
        test.addList("test 1");
        test.addList("test 2");
        test.getData(0).addNum(5);
        test.getData(1).addNum(10);
        test.getData(1).addNum(20);
        assertEquals(0, test.removeList("").size());
        assertEquals(1, test.removeList("test 1").size());
        assertEquals(1, test.getListLength());
        assertEquals(2, test.removeList("test 2").size());
        assertEquals(0, test.getListLength());
    }

    @Test
    public void testClearAll() {
        test.addList("test 1");
        test.addList("test 2");
        test.clearAll();
        assertEquals(0, test.getListLength());
    }

    @Test
    public void testGetDataBase() {
        assertEquals(0, test.getDataBase().size());
        test.addList("test 1");
        assertEquals(1, test.getDataBase().size());
    }

}