package com.tfsc.ilabs.selfservice.configpuller.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.common.exception.InputValidationException;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.NameLabel;
import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.InputValidator;
import com.tfsc.ilabs.selfservice.common.utils.StringUtils;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.AuxDataRequestDTO;
import com.tfsc.ilabs.selfservice.configpuller.dto.request.AuxEntityRequestDTO;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.AuxDataResponseDTO;
import com.tfsc.ilabs.selfservice.configpuller.dto.response.FormDataResponseDTO;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import com.tfsc.ilabs.selfservice.configpuller.services.api.FetchConfigService;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import com.tfsc.ilabs.selfservice.page.dto.response.PageDataResponseDTO;
import com.tfsc.ilabs.selfservice.page.service.api.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.tfsc.ilabs.selfservice.common.utils.Constants.*;

/**
 * Controller to fetch data from various 24/7 components Request contains
 * fetchConfig which is present in the pageDefinition:
 * <p>
 * - PAGE_DATA_VALUE / UI_ELEMENT_VALUE / UI_ELEMENT_OPTIONS
 */
@RestController
@RequestMapping("/api")
public class FetchConfigController {

    @Autowired
    private FetchConfigService fetchConfigTemplateService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private org.springframework.core.env.Environment environment;

    @Autowired
    private PageService pageService;

    @PostMapping(GET_FETCH_URI)
    public String getAuxData(@RequestBody AuxDataRequestDTO auxDataRequestDTO,
                             @PathVariable String clientId, @PathVariable String accountId, @PathVariable PublishType publishType) throws IOException {
        return StringUtils.valueAsString(getAuxDataFromService(auxDataRequestDTO, clientId, accountId, publishType));
    }

    /**
     * Returns Entity result based on FETCH_TYPE and search keyword, Response of the API is paginated.
     */
    @PostMapping(value = GET_FETCH_ENTITY_URI)
    public String fetchAuxEntityData(@RequestBody AuxEntityRequestDTO auxEntityRequestDTO, @PathVariable String clientId,
                                     @PathVariable String accountId, @PathVariable PublishType publishType) throws IOException, InputValidationException {

        InputValidator.isInvalidInput(auxEntityRequestDTO.getFetchParams(), environment.getProperty(Constants.INPUT_KEYS), null);

        JsonNode contextConfig = BaseUtil.getContext(clientId, accountId, auxEntityRequestDTO.getFetchParams());
        EntityTemplate entityTemplate = entityService.getTemplate(auxEntityRequestDTO.getEntityTemplateId());
        Environment env = BaseUtil.getEnvironmentToFetchFrom(publishType);
        return fetchConfigTemplateService.getAuxEntityData(entityTemplate.getFetchFor(), auxEntityRequestDTO.getType(), contextConfig, env);
    }

    private AuxDataResponseDTO getAuxDataFromService(AuxDataRequestDTO auxDataRequestDTO,
                                                     String clientId, String accountId, PublishType publishType) throws IOException {
        int entities = auxDataRequestDTO.getEntities().size();
        String pattern = auxDataRequestDTO.getPreFetchInputValidation();
        for (int i = 0; i < entities; i++) {
            InputValidator.isInvalidInput(auxDataRequestDTO.getEntities().get(i).toJsonNode(), environment.getProperty(Constants.INPUT_KEYS), pattern);
        }
        Environment env = BaseUtil.getEnvironmentToFetchFrom(publishType);
        Boolean isWorkflowInstancePresent = !Objects.isNull(auxDataRequestDTO.getWorkflowInstanceId());
        return fetchConfigTemplateService.getAuxData(auxDataRequestDTO, clientId, accountId, env, isWorkflowInstancePresent);
    }

    @PostMapping(value = GET_FETCH_FORMDATA_URI)
    public FormDataResponseDTO getFormData(@RequestBody AuxDataRequestDTO auxDataRequestDTO,
                                           @PathVariable String clientId, @PathVariable String accountId,
                                           @PathVariable String pageTemplateId) throws IOException {
        boolean isWorkflowInstancePresent = !Objects.isNull(auxDataRequestDTO.getWorkflowInstanceId());
        PublishType publishType = fetchConfigTemplateService.getPublishTypeFromPageTemplateId(pageTemplateId);
        AuxDataResponseDTO auxData = getAuxDataFromService(auxDataRequestDTO, clientId, accountId, publishType);
        FormDataResponseDTO formData = new FormDataResponseDTO();
        if (isWorkflowInstancePresent) {
            PageDataResponseDTO pageData = pageService.getPageDataByWorkflowInstanceAndPageTemplate(auxDataRequestDTO.getWorkflowInstanceId(), pageTemplateId);
            formData.setId(pageData.getId());
            formData.setData(pageData.getData());
            formData.setList(auxData.getList());
            formData.setWorkflowInstanceId(auxDataRequestDTO.getWorkflowInstanceId());
        } else {
            formData.setList(auxData.getList());
            JsonNode auxDataPage = auxData.getPage();
            formData.setData(auxDataPage.get(pageTemplateId));
        }
        formData.setPageTemplateId(pageTemplateId);

        return formData;
    }

    @GetMapping(Constants.GET_FETCH_DATA_URI)
    public JsonNode getFetchData(@PathVariable String clientId, @PathVariable String accountId, @PathVariable Environment env,
                                 @PathVariable String entity, @PathVariable FetchType type, @PathVariable String fetchFor) {
        NameLabel entityNameLabel = new NameLabel(entity, entity);
        JsonNode contextConfig = BaseUtil.getContext(clientId, accountId, List.of(entityNameLabel));

        return fetchConfigTemplateService.getDataFromService(fetchFor, contextConfig, type, env);
    }

    @RequestMapping(value = Constants.GET_DOWNLOAD_DATA_URI,
            method = RequestMethod.GET,
            produces = "application/vnd.ms-excel; charset=UTF-8")
    public byte[] getDataWithResponseHeader(@PathVariable String clientId, @PathVariable String accountId, @PathVariable Environment env,
                                            @PathVariable String entity, @PathVariable FetchType type, @PathVariable String fetchFor) {

        NameLabel entityNameLabel = new NameLabel(entity, entity);
        JsonNode contextConfig = BaseUtil.getContext(clientId, accountId, List.of(entityNameLabel));

        return fetchConfigTemplateService.getDataFromServiceAsString(fetchFor, contextConfig, type, env);
    }
}
