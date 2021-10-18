package com.tfsc.ilabs.selfservice.page.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.page.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by ravi.b on 26-09-2019.
 */
@Data
@EqualsAndHashCode
public class PageDataRequestDTO extends PageDTO {
    private List<NameLabel> entities;

    public PageDataRequestDTO() {
    }

    public PageDataRequestDTO(JsonNode data, List<NameLabel> entities) {
        super(data);
        this.entities = entities;
    }
}
