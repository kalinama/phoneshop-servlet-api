package com.es.phoneshop.model.services.security;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.TestCase.*;

public class DefaultDosProtectionServiceTest {

    private Map<String, AtomicInteger> userRequestTestMap;
    private final int MAX_QUANTITY_OF_REQUESTS;
    private final int TIMER;

    private DosProtectionService dosProtectionService;

    public DefaultDosProtectionServiceTest() throws IllegalAccessException, NoSuchFieldException {
        Field field = DefaultDosProtectionService.class.getDeclaredField("MAX_QUANTITY_OF_REQUESTS");
        field.setAccessible(true);
        MAX_QUANTITY_OF_REQUESTS = (int) field.get(null);

        field = DefaultDosProtectionService.class.getDeclaredField("TIMER");
        field.setAccessible(true);
        TIMER = (int) field.get(null);
    }

    @Before
    public void setup() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {

        Constructor<DefaultDosProtectionService> constructor = DefaultDosProtectionService.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        dosProtectionService = constructor.newInstance();

        userRequestTestMap = new HashMap<>();

        for (int i = 0; i < 5; i++)
            userRequestTestMap.put(UUID.randomUUID().toString(), new AtomicInteger(1));

        Field field = DefaultDosProtectionService.class.getDeclaredField("userRequestMap");
        field.setAccessible(true);
        field.set(dosProtectionService, userRequestTestMap);
    }

    @Test
    public void isAllowedTestTrue(){
        String ipAddr = userRequestTestMap.keySet().iterator().next();
        AtomicInteger oldInt = userRequestTestMap.get(ipAddr);

        assertTrue(dosProtectionService.isAllowed(ipAddr));
        assertEquals(oldInt.incrementAndGet(), userRequestTestMap.get(ipAddr).intValue());
    }

    @Test
    public void isAllowedTestFalse(){
        String ipAddr = userRequestTestMap.keySet().iterator().next();
        userRequestTestMap.get(ipAddr).set(MAX_QUANTITY_OF_REQUESTS);

        assertFalse(dosProtectionService.isAllowed(ipAddr));
        assertEquals(MAX_QUANTITY_OF_REQUESTS + 1, userRequestTestMap.get(ipAddr).intValue());
    }

    @Test
    public void cleanMapTest() throws NoSuchFieldException, IllegalAccessException {
        Field field = DefaultDosProtectionService.class.getDeclaredField("lastCleaningTime");
        field.setAccessible(true);
        field.set(dosProtectionService, System.currentTimeMillis() - TIMER*2);

        String ipAddr = userRequestTestMap.keySet().iterator().next();

        assertTrue(dosProtectionService.isAllowed(ipAddr));
        assertEquals(1, userRequestTestMap.size());
        assertEquals(userRequestTestMap.get(ipAddr).intValue(), new AtomicInteger(1).intValue());
    }

}
