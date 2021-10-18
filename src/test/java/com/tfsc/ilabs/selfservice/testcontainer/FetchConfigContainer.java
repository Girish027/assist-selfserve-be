package com.tfsc.ilabs.selfservice.testcontainer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfsc.ilabs.selfservice.common.models.Service;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.Fetch;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import com.tfsc.ilabs.selfservice.configpuller.model.ParamConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.TranslatorType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author prasada
 */
public class FetchConfigContainer {

    public static Fetch createFetchConfig() throws IOException {
        Fetch fetchConfig = new Fetch();
        JsonNode listNode = new ObjectMapper().readTree("{\"clientId\":\"test_clientId1\",\"accountId\":\"test_accountId1\"}");
        JsonNode pageNode = new ObjectMapper().createObjectNode();
        ((ObjectNode) pageNode).put("page", "page1");
        fetchConfig.setList(listNode);
        fetchConfig.setPage(pageNode);
        return fetchConfig;
    }

    public static String getFetchEntityResponse() throws IOException {
        return "{\"page\":1,\"totalPage\":1,\"size\":20,\"totalCount\":2,\"list\":{\"firstNames\":[\"Jocelyn\",\"Joseph\"],\"lastNames\":[\"Batz\",\"Russel\"]}}";
    }

    public static List<FetchConfigTemplate> fetchConfigTemplateListForExternalAPIForPagination() throws IOException {
        List<FetchConfigTemplate> fetchConfigTemplates = new ArrayList<>();
        FetchConfigTemplate template = new FetchConfigTemplate();
        template.setFetchFor("queue_entities");
        template.setHeaders(new ObjectMapper().readTree("{\"Authorization\": \"Bearer I35H04IcdUVpSm4nez1Dpycjw3dz2lOcyJsw\"}"));
        ParamConfigTemplate config = new ParamConfigTemplate();
        config.setContexts((ArrayNode) new ObjectMapper().readTree("[\"accountId\", \"pageNo\", \"pageSize\", \"searchKey\"]"));
        config.setConstants(new ObjectMapper().readTree("{}"));
        template.setParams(config);
        template.setRelativeURL("/public-api/users?page=${pageNo}");
        template.setRespApiToRespUi(new ObjectMapper().readTree("{\"objectTranslator\": \"function translate(values) {    var queues = {        firstNames: [],        lastNames: []    };var response = {currentPage:1, perPage:20, totalPage:1, totalCount:0};    var list = [];    try {        values = JSON.parse(values);        list = values.result;    } catch (e) {console.log(e.toString());        return e.toString();    }    list.forEach(function(item) {        queues.firstNames.push(item.first_name);        queues.lastNames.push(item.last_name);    }); response.currentPage = values._meta.currentPage; response.totalPage = values._meta.pageCount; response.perPage = values._meta.perPage; response.totalCount = values._meta.totalCount;  response.list = queues;  return JSON.stringify(response);}\"}"));
        template.setTranslatorType(TranslatorType.JS);
        template.setType(FetchType.ENTITY_LISTING);
        template.setService(new Service("1", "name", "test"));
        fetchConfigTemplates.add(template);
        return fetchConfigTemplates;

    }

    public static List<FetchConfigTemplate> fetchConfigTemplateListForOHSEntityList() throws IOException {
        List<FetchConfigTemplate> fetchConfigTemplates = new ArrayList<>();
        FetchConfigTemplate template = new FetchConfigTemplate();
        template.setFetchFor("queue_entities");
        template.setHeaders(new ObjectMapper().readTree("{\"apikey\": \"KtbDtdYYb7EUD4oajYCM8l9Nh7L0AgEc\"}"));
        ParamConfigTemplate config = new ParamConfigTemplate();
        config.setContexts((ArrayNode) new ObjectMapper().readTree("[\"accountId\", \"pageNo\", \"pageSize\", \"searchKey\"]"));
        config.setConstants(new ObjectMapper().readTree("{\"pageNumber\":1}"));
        config.setQueryParams(new ObjectMapper().readTree("{\"pageNumber\":2}"));
        template.setParams(config);
        template.setRelativeURL("/v1/ohs/adminEntity/listByAccount?entityType=queue&accountId=${accountId}-account-default");
        template.setRespApiToRespUi(new ObjectMapper().readTree("{\"objectTranslator\":\"function translate(values){return values;}\"}"));
        template.setTranslatorType(TranslatorType.JS);
        template.setType(FetchType.ENTITY_SEARCH);
        template.setService(new Service("ohs", "OHS", "test"));
        fetchConfigTemplates.add(template);
        return fetchConfigTemplates;

    }

}
