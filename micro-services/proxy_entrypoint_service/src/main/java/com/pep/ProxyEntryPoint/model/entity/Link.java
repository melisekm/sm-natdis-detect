package com.pep.ProxyEntryPoint.model.entity;

import com.pep.ProxyEntryPoint.converter.JsonbConverter;
import com.pep.ProxyEntryPoint.model.IID;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "link")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@SuperBuilder
@Accessors(chain = true)
public class Link implements IID<Long> {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "origin_url")
    private String originUrl;

    @Column(name = "final_url")
    private String finalUrl;

    @Column(name = "text")
    private String text;

    @Column(name = "html")
    private String html;

    @Column(name = "title")
    private String title;

    @Column(name = "other_info", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private Map<String, Object> otherInfo;

    @Column(name = "extracted_at")
    private LocalDateTime extractedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "domain")
    private String domain;

    @ManyToMany
    @JoinTable(
            name = "prediction_links",
            joinColumns = @jakarta.persistence.JoinColumn(name = "link_id"),
            inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "prediction_id")
    )
    private List<Prediction> predictions;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
