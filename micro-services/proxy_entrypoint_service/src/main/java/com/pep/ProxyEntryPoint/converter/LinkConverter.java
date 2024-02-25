package com.pep.ProxyEntryPoint.converter;

import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.model.entity.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkConverter extends AbstractConverter<LinkInput, LinkOutput, Link> {
    @Override
    public Link convertToEntity(LinkInput input) {
        Link link = new Link();
        link.setLink(input.getLink());
        link.setBody(input.getBody());
        link.setRawBody(input.getRawBody());
        link.setTitle(input.getTitle());
        link.setOtherInfo(input.getOtherInfo());
        link.setPublishedAt(input.getPublishedAt());
        link.setCreatedAt(input.getCreatedAt());
        return link;
    }

    @Override
    public LinkOutput convertToOutput(Link entity) {
        LinkOutput linkOutput = new LinkOutput();
        linkOutput.setId(entity.getId());
        linkOutput.setLink(entity.getLink());
        linkOutput.setBody(entity.getBody());
        linkOutput.setRawBody(entity.getRawBody());
        linkOutput.setTitle(entity.getTitle());
        linkOutput.setOtherInfo(entity.getOtherInfo());
        linkOutput.setPublishedAt(entity.getPublishedAt());
        linkOutput.setCreatedAt(entity.getCreatedAt());
        return linkOutput;
    }
}
