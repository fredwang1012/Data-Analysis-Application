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
        assertFalse(test.removeList(""));
        assertTrue(test.removeList("test 1"));
        assertEquals(1, test.getListLength());
        assertTrue(test.removeList("test 2"));
        assertEquals(0, test.getListLength());
    }

    @Test
    public void testClearAll() {
        test.addList("test 1");
        test.addList("test 2");
        test.clearAll();
        assertEquals(0, test.getListLength());
    }


}