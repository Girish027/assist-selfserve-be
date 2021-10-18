package com.tfsc.ilabs.selfservice.configpuller.services.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.AuxDataRequestDTO;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.AuxDataResponseDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import com.tfsc.ilabs.selfservice.common.models.PublishType;

public interface FetchConfigService {

    AuxDataResponseDTO getAuxData(AuxDataRequestDTO auxDataRequestDTO, String clientId, String accountId, Environment env, Boolean isWorkflowInstancePresent);

    String getAuxEntityData(String fetchFor, String type, JsonNode contextConfig, Environment env);

    JsonNode getDataFromService(String fetchFor, JsonNode contextConfig, FetchType type, Environment env);

    PublishType getPublishTypeFromPageTemplateId(String pageTemplateId);

    byte[] getDataFromServiceAsString(String fetchFor, JsonNode contextConfig, FetchType type, Environment env);
    
}