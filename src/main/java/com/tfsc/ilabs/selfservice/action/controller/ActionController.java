package com.tfsc.ilabs.selfservice.action.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.action.services.api.ActionExecutorService;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ActionController {
    @Autowired
    ActionExecutorService actionExecutorService;

    @PostMapping(Constants.EXECUTE_ACTION_URI)
    public ApiResponse executeAction(@RequestBody JsonNode pageData, @PathVariable String clientId, @PathVariable String accountId,
                                     @PathVariable Environment env, @PathVariable String entity, @PathVariable Integer actionId) {
        NameLabel entityNameLabel = new NameLabel(entity, entity);
        return actionExecutorService.execute(actionId, pageData, entityNameLabel, clientId, accountId, env);
    }
}
