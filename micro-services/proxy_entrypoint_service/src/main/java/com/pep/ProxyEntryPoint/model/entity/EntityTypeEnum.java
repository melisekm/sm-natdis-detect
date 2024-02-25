package com.pep.ProxyEntryPoint.model.entity;

import com.pep.ProxyEntryPoint.model.IID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "entity_type_enum")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@SuperBuilder
@Accessors(chain = true)
public class EntityTypeEnum {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "enum_key")
    private String key;

    @Column(name = "enum_value")
    private String value;
}
