package com.tfsc.ilabs.selfservice.testcontainer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.page.models.PageData;
import com.tfsc.ilabs.selfservice.page.models.PageDataStatus;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstance;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PageDataContainer {

    public static List<PageData> getPageDataList(WorkflowInstance workflowInstance) throws IOException {
        List<PageData> pageDataList = new ArrayList<>();
        Optional<PageTemplate> pageTemplateOptional = PageTemplateContainer.getPageTemplateById("page1");
        if (pageTemplateOptional.isPresent()) {
            PageTemplate pageTemplate = pageTemplateOptional.get();
            PageData pageData1 = new PageData();
            JsonNode jsonNode1 = new ObjectMapper().readTree("{\"a\" : 1, \"b\" : 2}");
            pageData1.setData(jsonNode1);
            pageData1.setPageTemplate(pageTemplate);
            pageData1.setStatus(PageDataStatus.COMPLETE);
            pageData1.setWorkflowInstance(workflowInstance);
            pageDataList.add(pageData1);
            PageData pageData2 = new PageData();
            JsonNode jsonNode2 = new ObjectMapper().readTree("{\"c\" : 3, \"d\" : 4}");
            pageData2.setData(jsonNode2);
            pageData2.setPageTemplate(pageTemplate);
            pageData2.setStatus(PageDataStatus.COMPLETE);
            pageData2.setWorkflowInstance(workflowInstance);
            pageDataList.add(pageData2);
        }
        return pageDataList;
    }

    public static List<PageData> getTiePageDataList(WorkflowInstance workflowInstance) throws IOException {
        List<PageData> pageDataList = new ArrayList<>();
        Optional<PageTemplate> pageTemplateOptional = PageTemplateContainer.getPageTemplateById("page1");
        if (pageTemplateOptional.isPresent()) {
            PageTemplate pageTemplate = pageTemplateOptional.get();
            PageData pageData = new PageData();
            JsonNode jsonNode1 = new ObjectMapper().readTree("{\"websiteTag\": {\"step1\": \"Download or copy code\", \"step2\": \"Paste tag in <head> element. Test tag for your test environment, and live tag for your live environment\", \"step3\": \"Repeat for all pages where chat needs to be offered\", \"stageTag\": \"/*!\\n * 24/7 Customer, Inc. Confidential, Do Not Distribute. This is an\\n * unpublished, proprietary work which is fully protected under\\n * copyright law. This code may only be used pursuant to a valid\\n * license from 24/7 Customer, Inc. http://www.247-inc.com\\n */\\n/* eslint no-use-before-define:0 */\\nwindow.SN = (function (SN) {\\n\\n    SN.clientKey = 'test-default-v1-001';\\n    SN.cdnRoot = \\\"dev-sd.s3.amazonaws.com\\\";\\n\\n    /**\\n     * Method to Create a Script Tag to Load Flow.js\\n     * @method loadFlowJs\\n     * @returns {void}\\n     */\\n    function loadFlowJs () {\\n        var pxURL = document.location.protocol + \\\"//dev-sd.s3.amazonaws.com/psp/platform/247px.debug.js?clientKey=test-default-v1-001\\\";\\n        var flowJS = document.createElement(\\\"script\\\");\\n        flowJS.async = true;\\n        flowJS.setAttribute(\\\"crossorigin\\\", \\\"\\\");\\n        flowJS.src = pxURL;\\n        var firstScript = document.getElementsByTagName(\\\"script\\\")[0];\\n        firstScript.parentNode.insertBefore(flowJS, firstScript);\\n    }\\n\\n    loadFlowJs();\\n    return SN;\\n}(window.SN || {}));\", \"productionTag\": \"/* eslint-disable */\\n/*!\\n * 24/7 Customer, Inc. Confidential, Do Not Distribute. This is an\\n * unpublished, proprietary work which is fully protected under\\n * copyright law. This code may only be used pursuant to a valid\\n * license from 24/7 Customer, Inc. http://www.247-inc.com\\n */\\nwindow.SN=function(o){function t(){var o=document.location.protocol+\\\"//\\\"+e+\\\"/\\\"+n+\\\"/247px.js?clientKey=test-default-v1-001\\\",t=document.createElement(\\\"script\\\");t.setAttribute(\\\"crossorigin\\\",\\\"\\\"),t.async=!0,t.src=o;var r=document.getElementsByTagName(\\\"script\\\")[0];r.parentNode.insertBefore(t,r)}o.clientKey='test-default-v1-001',o.cdnRoot=\\\"d1af033869koo7.cloudfront.net\\\";var e=\\\"d1af033869koo7.cloudfront.net\\\",n=\\\"psp/platform\\\";return t(),o}(window.SN||{});\"}, \"domainWhitelisting\": {\"stageWhitelist\": [\"zxczcz\", \"asdad\"], \"productionWhitelist\": []}}");
            pageData.setData(jsonNode1);
            pageData.setPageTemplate(pageTemplate);
            pageData.setStatus(PageDataStatus.COMPLETE);
            pageData.setWorkflowInstance(workflowInstance);
            pageDataList.add(pageData);
        }
        return pageDataList;
    }

    public static List<PageData> getFilePageDataList(WorkflowInstance workflowInstance) throws IOException {
        List<PageData> pageDataList = new ArrayList<>();
        Optional<PageTemplate> pageTemplateOptional = PageTemplateContainer.getPageTemplateById("page2");
        if (pageTemplateOptional.isPresent()) {
            PageTemplate pageTemplate = pageTemplateOptional.get();
            PageData pageData = new PageData();
            String path = Path.of(System.getProperty("user.dir")).toString().replace("\\","\\\\");
            JsonNode jsonNode1 = new ObjectMapper().readTree("{\"file\": {\"fileName\": \"BulkUserCreationTemplate.xls\", \"fileLocation\": \"" + path + "\\\\src\\\\test\\\\resources\\\\BulkUserCreationTemplate.xls\"}}");
            pageData.setData(jsonNode1);
            pageData.setPageTemplate(pageTemplate);
            pageData.setStatus(PageDataStatus.COMPLETE);
            pageData.setWorkflowInstance(workflowInstance);
            pageDataList.add(pageData);
        }
        return pageDataList;
    }
}
