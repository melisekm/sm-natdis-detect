package com.pep.ProxyEntryPoint.model.entity;

import com.pep.ProxyEntryPoint.model.IID;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prediction")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@SuperBuilder
@Accessors(chain = true)
public class Prediction implements IID<Long> {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "informative")
    private Boolean informative;

    @NotNull
    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "prediction_text")
    private String predictionText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "rating")
    private Boolean rating;

    @ManyToMany
    @JoinTable(
            name = "prediction_links",
            joinColumns = @jakarta.persistence.JoinColumn(name = "prediction_id"),
            inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "link_id")
    )
    private List<Link> links;

    @ToString.Exclude
    @OneToMany(mappedBy = "prediction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DbEntity> entities;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
