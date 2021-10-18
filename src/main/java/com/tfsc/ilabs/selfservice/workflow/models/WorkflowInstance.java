package com.tfsc.ilabs.selfservice.workflow.models;

import com.tfsc.ilabs.selfservice.action.config.ApiResponseConverter;
import com.tfsc.ilabs.selfservice.common.dto.ApiResponse;
import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import com.tfsc.ilabs.selfservice.workflow.dto.response.EntityInstanceDTO;
import com.tfsc.ilabs.selfservice.workflow.dto.response.WorkflowInstanceDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "activity_instance")
public class WorkflowInstance extends AuditableEntity {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowInstance.class);

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "activity_template_id")
    private WorkflowTemplate workflowTemplate;

    @ManyToOne
    @JoinColumn(name = "current_page_template_id")
    private PageTemplate currentPageTemplate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workflowInstance", cascade = CascadeType.ALL)
    private Set<EntityInstance> entityInstances;

    @NotNull
    private String clientId;

    @NotNull
    private String accountId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    private WorkflowInstanceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    private DefinitionType type;

    private String pendingParentId;

    @Column(columnDefinition = "json")
    @Convert(converter = ApiResponseConverter.class)
    private ApiResponse processedResponse;

    private boolean hidden;

    public WorkflowInstance(@NotNull WorkflowTemplate workflowTemplate, @NotNull PageTemplate currentPageTemplate,
                            @NotNull String clientId, @NotNull String accountId, @NotNull WorkflowInstanceStatus status, @NotNull DefinitionType type) {
        this.workflowTemplate = workflowTemplate;
        this.currentPageTemplate = currentPageTemplate;
        this.clientId = clientId;
        this.accountId = accountId;
        this.status = status;
        this.type = type;
    }

    public boolean isTimedOut(Integer configuredTimeout) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Integer timeout = 30;
        if (configuredTimeout != null) {
            timeout = configuredTimeout;
        } else {
            logger.error("Timeout should be an integer in minutes. Found: {}. Using default {}", configuredTimeout,
                    timeout);
        }
        long diff = currentTimestamp.getTime() - this.getLastUpdatedOn().getTime();
        long timeoutInMilli = timeout * 60000;
        logger.info("Diff: {}, Timeout: {}", diff, timeout);
        return diff > timeoutInMilli;
    }

    /**
     * Lock is available for a user if - He was editing and has not timed out - If
     * the workflowInstance is in DRAFT_SAVE state
     */
    public boolean isLockAvailable(String userId, Integer configuredTimeout) {
        boolean isAvailable = false;
        if (this.status == WorkflowInstanceStatus.DRAFT_SAVE ||
                this.status == WorkflowInstanceStatus.PROMOTED_TO_TEST ||
                this.status == WorkflowInstanceStatus.TEST_PROMOTION_FAILED ||
                this.status == WorkflowInstanceStatus.LIVE_PROMOTION_FAILED ||
                (this.status == WorkflowInstanceStatus.DRAFT_EDIT && (this.isTimedOut(configuredTimeout) || userId.equalsIgnoreCase(this.getLastUpdatedBy())))
        ) {
            // If in DRAFT_SAVE then lock can be acquired
            isAvailable = true;
        }
        return isAvailable;
    }

    public WorkflowInstanceDTO toDTO() {
        WorkflowInstanceDTO workflowInstanceDTO = new WorkflowInstanceDTO();
        workflowInstanceDTO.setId(this.id);
        workflowInstanceDTO.setStatus(this.status);
        workflowInstanceDTO.setLastModifiedBy(this.getLastUpdatedBy());
        workflowInstanceDTO.setCreatedBy(this.getCreatedBy());
        workflowInstanceDTO.setCreatedOn(this.getCreatedOn());
        workflowInstanceDTO.setType(this.type);
        workflowInstanceDTO.setProcessedResponse(this.processedResponse);
        workflowInstanceDTO.setWorkflowId(this.workflowTemplate.getId());
        workflowInstanceDTO.setWorkflowTitle(this.workflowTemplate.getTitle());
        workflowInstanceDTO.setEntityType(this.workflowTemplate.getEntityTemplate().getName());

        workflowInstanceDTO.setLastUpdatedOn(this.getLastUpdatedOn());

        List<EntityInstanceDTO> entityInstanceDTOs = this.entityInstances.stream()
                .map(EntityInstance::toDTO).collect(Collectors.toList());

        workflowInstanceDTO.setEntityInstances(entityInstanceDTOs);
        return workflowInstanceDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowInstance that = (WorkflowInstance) o;
        return id == that.id &&
                Objects.equals(workflowTemplate, that.workflowTemplate) &&
                Objects.equals(currentPageTemplate, that.currentPageTemplate) &&
                Objects.equals(entityInstances, that.entityInstances) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(accountId, that.accountId) &&
                type == that.type;
    }

    //not moving to lombok due to some limitation
    @Override
    public int hashCode() {
        return Objects.hash(id, workflowTemplate, currentPageTemplate, clientId, accountId, type);
    }

    @Override
    public String toString() {
        return "WorkflowInstance{" +
                "id=" + id +
                ", workflowTemplate=" + workflowTemplate +
                ", currentPageTemplate=" + currentPageTemplate +
                ", clientId='" + clientId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
