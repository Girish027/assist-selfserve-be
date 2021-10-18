package com.tfsc.ilabs.selfservice.testcontainer;


import com.tfsc.ilabs.selfservice.workflow.models.ClientsConfig;
import com.tfsc.ilabs.selfservice.workflow.models.ConfigNames;
import com.tfsc.ilabs.selfservice.workflow.models.ScopeType;

import java.util.List;

public class ClientsConfigContainer {

    public static ClientsConfig createClientsConfig(Integer id, String scopeId, ScopeType scopeType, ConfigNames name, List<String> values) {
        ClientsConfig clientsConfig = new ClientsConfig();
        clientsConfig.setId(id);
        clientsConfig.setScopeId(scopeId);
        clientsConfig.setScopeType(scopeType);
        clientsConfig.setConfigName(name);
        clientsConfig.setValues(values);
        return clientsConfig;
    }
}
