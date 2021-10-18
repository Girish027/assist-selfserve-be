package com.tfsc.ilabs.selfservice.workflow.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.models.ServiceUrls;
import com.tfsc.ilabs.selfservice.common.utils.UiSchemaConverter;
import com.tfsc.ilabs.selfservice.workflow.dto.response.BookmarkDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by ravi.b on 10-09-2019.
 */
@Data
@EqualsAndHashCode
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Bookmark extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NotNull
    private String displayLabel;

    //Can be used for tooltip or something
    @NotNull
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "service_url_id")
    private ServiceUrls serviceUrl;

    @NotNull
    private String relativeUrl;

    @Column(columnDefinition = "mediumtext")
    @Convert(converter = UiSchemaConverter.class)
    private UiSchema uiSchema;

    public BookmarkDTO toDTO() {
        String baseURl = this.serviceUrl.getBaseURL();
        if (baseURl.endsWith("/") && !baseURl.endsWith("//"))
            baseURl = baseURl.substring(0, baseURl.length() - 1);
        return new BookmarkDTO(this.id.toString(), this.displayLabel, this.description, baseURl + this.relativeUrl, this.uiSchema);
    }
}
