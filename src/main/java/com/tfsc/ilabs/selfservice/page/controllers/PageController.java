package com.tfsc.ilabs.selfservice.page.controllers;

import com.tfsc.ilabs.selfservice.page.dto.request.PageDataRequestDTO;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PageController {

    @Autowired
    private PageService pageService;

    @PostMapping("/workflow/instance/{workflowInstanceId}/page/{pageTemplateId}/pagedata")
    public PageDataResponseDTO savePageData(@RequestBody PageDataRequestDTO pageDataRequestDTO, @PathVariable Integer workflowInstanceId,
                                            @PathVariable String pageTemplateId) {
        return pageService.savePageData(pageDataRequestDTO, workflowInstanceId, pageTemplateId);
    }

    @GetMapping(("/workflow/instance/{workflowInstanceId}/page/{pageTemplateId}/pagedata"))
    public PageDataResponseDTO getPageData(@PathVariable Integer workflowInstanceId, @PathVariable String pageTemplateId) {
        return pageService.getPageDataByWorkflowInstanceAndPageTemplate(workflowInstanceId, pageTemplateId);
    }
}
