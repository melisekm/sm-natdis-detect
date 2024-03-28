package com.pep.ProxyEntryPoint.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.model.entity.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkConverter extends AbstractConverter<LinkInput, LinkOutput, Link> {
    @Override
    public Link convertToEntity(LinkInput input) {
        Link link = new Link();
        link.setOriginUrl(input.getOriginUrl());
        link.setFinalUrl(input.getFinalUrl());
        link.setText(input.getText());
        link.setHtml(input.getHtml());
        link.setTitle(input.getTitle());
        link.setOtherInfo(input.getOtherInfo());
        link.setDomain(input.getDomain());
        link.setPublishedAt(input.getPublishedAt());
        link.setExtractedAt(input.getExtractedAt());
        return link;
    }

    @Override
    public LinkOutput convertToOutput(Link entity) {
        LinkOutput linkOutput = new LinkOutput();
        linkOutput.setId(entity.getId());
        linkOutput.setOriginUrl(entity.getOriginUrl());
        linkOutput.setFinalUrl(entity.getFinalUrl());
        linkOutput.setText(entity.getText());
        linkOutput.setHtml(entity.getHtml());
        linkOutput.setTitle(entity.getTitle());
        linkOutput.setOtherInfo(entity.getOtherInfo());
        linkOutput.setDomain(entity.getDomain());
        linkOutput.setPublishedAt(entity.getPublishedAt());
        linkOutput.setExtractedAt(entity.getExtractedAt());
        return linkOutput;
    }
}
