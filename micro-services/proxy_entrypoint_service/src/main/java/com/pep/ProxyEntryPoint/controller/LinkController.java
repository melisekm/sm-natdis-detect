package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.rest.api.LinkControllerApi;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.model.entity.Link;
import com.pep.ProxyEntryPoint.service.LinkService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class LinkController extends AbstractController<LinkInput, LinkOutput, Link, Long> implements LinkControllerApi {

    @Autowired
    public LinkController(LinkService linkService) {
        super(linkService);
    }

    @Override
    public ResponseEntity<LinkOutput> createLink(LinkInput input) {
        return super.create(input);
    }

    @Override
    public ResponseEntity<LinkOutput> getLink(Long id) {
        return super.get(id);
    }

    @Override
    public ResponseEntity<LinkOutput> updateLink(Long id, LinkInput input) {
        return super.update(id, input);
    }

    @Override
    public ResponseEntity<Void> deleteLink(Long id) {
        return super.delete(id);
    }
}
