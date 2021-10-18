package com.tfsc.ilabs.selfservice.testcontainer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.Service;
import com.tfsc.ilabs.selfservice.common.models.ServiceUrls;

import java.io.IOException;

/**
 * @author prasada
 */
public class ServiceUrlsContainer {
    public static ServiceUrls getServiceUrls(Environment env) throws IOException {
        Service service = new Service();
        service.setId("1");
        service.setDescription("TIE Action calls");
        service.setName("TIE Action");

        ServiceUrls serviceUrls = new ServiceUrls();
        serviceUrls.setEnv(env);
        serviceUrls.setService(service);
        serviceUrls.setBaseURL("http://test");
        JsonNode header = new ObjectMapper().readTree("{\"Content-Type\" : \"application/json\"}");
        serviceUrls.setHeaders(header);

        return serviceUrls;
    }
}
