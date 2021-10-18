package com.tfsc.ilabs.selfservice.entity.controller;

import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.entity.controllers.EntityController;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.MockAnnotationProcessor;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class EntityControllerTest {

    @InjectMocks
    EntityController entityController = new EntityController();

    @Mock
    EntityService entityService;

    @Mock
    EntityInstance entityInstance;

    MockMvc mockMvc;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(entityController).build();

        Mockito.when(entityService.getInstance(1)).thenReturn(Optional.of(entityInstance));
    }

    @Test
    public void test_discardAnEntity() throws Exception {
        mockMvc.perform(post(Constants.BASE_URI+
                "/entity/discard/1")).andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void test_discardAnEntityWithException() throws Exception {
        mockMvc.perform(post(Constants.BASE_URI
                +"/entity/discard/2")).andReturn();
    }
}