package com.tfsc.ilabs.selfservice.security.service;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.UUID;

import static org.junit.Assert.*;


public class AllowAllSessionValidatorTest {
    AllowAllSessionValidator validator = new AllowAllSessionValidator();

    @Test
    public void test_validateSession(){
        UserSession session = validator.validateSession(new MockHttpServletRequest());
        assertEquals("default.user", session.getUserName());
    }

    @Test
    public void test_getUUID_1(){
        UUID uuid = validator.getUUID(new MockHttpServletRequest());
        assertNotNull(uuid);
    }

    @Test
    public void test_getUUID_2(){
        UUID uuid = validator.getUUID();
        assertNotNull(uuid);
    }
}
