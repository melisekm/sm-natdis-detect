package com.pep.ProxyEntryPoint.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class LinkCountOutput {

    private List<String> urls;
}
