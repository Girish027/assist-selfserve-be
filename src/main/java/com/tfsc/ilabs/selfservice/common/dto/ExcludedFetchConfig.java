package com.tfsc.ilabs.selfservice.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcludedFetchConfig {
    private String fetchFor;
    private List<Integer> status;

    public ExcludedFetchConfig() {
        status = new ArrayList<>();
    }
}
