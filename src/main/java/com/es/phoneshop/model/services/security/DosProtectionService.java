package com.es.phoneshop.model.services.security;

import javax.servlet.http.HttpServletRequest;

public interface DosProtectionService {
    boolean isAllowed(String ipAddr);
}
