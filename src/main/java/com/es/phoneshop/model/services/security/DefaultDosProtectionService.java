package com.es.phoneshop.model.services.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultDosProtectionService implements DosProtectionService {

    private Map<String, AtomicInteger> userRequestMap;
    private static final int MAX_QUANTITY_OF_REQUESTS = 200;
    private static final int TIMER = 60000;
    private volatile long lastCleaningTime;

    private DefaultDosProtectionService() {
        userRequestMap = new ConcurrentHashMap<>();
        lastCleaningTime = System.currentTimeMillis();
    }

    private static class DefaultDosProtectionServiceHolder {
        static final DefaultDosProtectionService HOLDER_INSTANCE = new DefaultDosProtectionService();}

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionServiceHolder.HOLDER_INSTANCE;
    }

    @Override
    public boolean isAllowed(String ipAddr){
        if(isNeedClearing())
            userRequestMap.clear();

       int quantity = userRequestMap.computeIfAbsent(ipAddr,
               (k) -> new AtomicInteger(0)).incrementAndGet();

        return quantity < MAX_QUANTITY_OF_REQUESTS;
    }

    private boolean isNeedClearing(){
        if(System.currentTimeMillis() - lastCleaningTime > TIMER){
            lastCleaningTime = System.currentTimeMillis();
            return true;
        }
        else return false;
    }
}
