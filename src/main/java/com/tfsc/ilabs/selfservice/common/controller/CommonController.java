package com.tfsc.ilabs.selfservice.common.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.models.PermissionType;
import com.tfsc.ilabs.selfservice.common.services.api.CommonService;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.security.service.SessionValidatorWithOKTA;
import com.tfsc.ilabs.selfservice.security.service.api.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class CommonController {

    @Autowired
    private CommonService commonService;
    @Autowired
    private PermissionService permissionService;

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);


    @CacheEvict(value = Constants.CACHE_NAME, allEntries = true)
    @PostMapping(Constants.EXPIRE_API_CACHE_URI)
    public ResponseEntity<String> invalidateCache() {
        return ResponseEntity.ok(Constants.RESPONSE_SUCCESS);
    }

    @PostMapping(Constants.INVALIDATE_SESSION_URI)
    public ResponseEntity<String> invalidateSession(HttpServletRequest request) {
        SessionValidatorWithOKTA.removeTokenFromMap(HttpUtils.getSessionCookies(request));
        return ResponseEntity.ok(Constants.RESPONSE_SUCCESS);
    }

    @PostMapping(Constants.EXPIRE_ENTITY_CACHE_URI)
    public ResponseEntity<String> evictEntityCache(@RequestParam(required = false) String name) {
        commonService.evictCache(name);
        return ResponseEntity.ok(Constants.RESPONSE_SUCCESS);
    }

    @GetMapping(Constants.GET_PERMISSIONS_URI)
    public Set<PermissionType> getPermissions(@PathVariable String clientId, @PathVariable String accountId,HttpServletRequest request) {
        String accessToken = HttpUtils.getSessionCookies(request);
        return permissionService.getPermissions(accessToken, clientId, accountId);
    }

    @PostMapping("/log")
    public ResponseEntity<String> publishUILog(@RequestBody JsonNode logData) {
        logger.info(String.valueOf(logData));
        return ResponseEntity.ok(Constants.RESPONSE_SUCCESS);
    }
}
