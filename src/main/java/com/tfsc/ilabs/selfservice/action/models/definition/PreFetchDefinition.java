package com.tfsc.ilabs.selfservice.action.models.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author Sushil.Kumar
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreFetchDefinition {
    private Map<String, Map<String, String>> definitions;
}
