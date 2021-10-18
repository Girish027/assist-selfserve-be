package com.tfsc.ilabs.selfservice.configpuller.dto.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class AuxDataRequestDTO {
    private List<NameLabel> entities;
    private Fetch fetch;
    private Integer workflowInstanceId;
    private String preFetchInputValidation;
    public AuxDataRequestDTO() {
    }

    public AuxDataRequestDTO(Fetch fetch, List<NameLabel> entities, Integer workflowInstanceId, String preFetchInputValidation) {
        this.fetch = fetch;
        this.entities = entities;
        this.workflowInstanceId = workflowInstanceId;
        this.preFetchInputValidation = preFetchInputValidation;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}