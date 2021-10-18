package com.tfsc.ilabs.selfservice.action.models.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by ravi.b on 24-07-2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ResponseDefinition {
    private Integer responseCode;

    private String responseBody;

}
