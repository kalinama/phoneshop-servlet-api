package com.es.phoneshop.model.item.dao;

import com.es.phoneshop.model.item.IdentifiedItem;
import com.es.phoneshop.model.item.exception.ItemNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ArrayListDaoTest {

    private Dao dao;
    private List<TestItem> testItemsList;
    private long maxId;

    private class TestItem extends IdentifiedItem{
        TestItem(Long id){
            super(id);
        }
        TestItem(){}
    }
    
    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        testItemsList = new ArrayList<>();
        testItemsList.add(new TestItem(1L));
        testItemsList.add(new TestItem(2L)); 
        testItemsList.add(new TestItem(3L));
        testItemsList.add(new TestItem(4L));
        testItemsList.add(new TestItem(5L));
        
        maxId = testItemsList.stream()
                .max(Comparator.comparing(TestItem::getId))
                .get()
                .getId();

        dao = mock(
                ArrayListDao.class,
                Mockito.CALLS_REAL_METHODS);

        Field fieldItems = ArrayListDao.class.getDeclaredField("items");
        fieldItems.setAccessible(true);
        fieldItems.set(dao, testItemsList);

        Field fieldType = ArrayListDao.class.getDeclaredField("type");
        fieldType.setAccessible(true);
        fieldType.set(dao, TestItem.class);

        Field fieldIdMaxValue = ArrayListDao.class.getDeclaredField("idMaxValue");
        fieldIdMaxValue.setAccessible(true);
        fieldIdMaxValue.set(dao, maxId);
    }
    
    @Test
    public void testGetItemByExistedId() throws ItemNotFoundException {
        TestItem testItem = testItemsList.get(0);
        TestItem result = (TestItem) dao.getById(testItem.getId());

        assertEquals(testItem, result);
    }

    @Test(expected = ItemNotFoundException.class)
    public void testGetItemByNotExistedId() throws ItemNotFoundException {
        long notExistedId = maxId + 1;
        dao.getById(notExistedId);
    }

    @Test
    public void testSaveItemWithoutId() throws ItemNotFoundException {
        TestItem item = new TestItem();
        int oldSize = testItemsList.size();
        dao.save(item);

        assertNotEquals(testItemsList.size(), oldSize);
        assertNotNull(item.getId());
        assertEquals(item, dao.getById(item.getId()));
    }

    @Test
    public void testSaveItemWithExistedId() throws ItemNotFoundException {
        long existedId = testItemsList.get(0).getId();
        TestItem item = new TestItem(existedId);
        int oldSize = testItemsList.size();
        dao.save(item);

        assertEquals(testItemsList.size(), oldSize);
        assertEquals(item, dao.getById(existedId));
    }

    @Test
    public void testSaveItemWithNotExistedId() throws ItemNotFoundException {
        long notExistedId = maxId + 1;
        TestItem item = new TestItem(notExistedId);
        int oldSize = testItemsList.size();
        dao.save(item);

        assertNotEquals(testItemsList.size(), oldSize);
        assertEquals(item, dao.getById(notExistedId));
    }

    @Test(expected = ItemNotFoundException.class)
    public void testDeleteItemById() throws ItemNotFoundException {
        long idOfRemovableItem = testItemsList.get(0).getId();
        dao.delete(idOfRemovableItem);
        dao.getById(idOfRemovableItem);
    }

}
