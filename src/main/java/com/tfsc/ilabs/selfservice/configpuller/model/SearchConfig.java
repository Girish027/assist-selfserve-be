package com.tfsc.ilabs.selfservice.configpuller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sushil.Kumar
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchConfig {
    private int pageSize;
    private int pageNumber;
    private boolean hasFilter;
}
