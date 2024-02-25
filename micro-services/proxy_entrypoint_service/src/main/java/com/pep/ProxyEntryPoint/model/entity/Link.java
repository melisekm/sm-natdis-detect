package com.pep.ProxyEntryPoint.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

@Entity
@Table(name = "link")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@SuperBuilder
@Accessors(chain = true)
public class Link {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "link")
    private String link;

    @Column(name = "body")
    private String body;

    @Column(name = "raw_body")
    private String rawBody;

    @Column(name = "title")
    private String title;

    @Column(name = "other_info")
    private String otherInfo;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "published_at")
    private LocalDate publishedAt;
}
