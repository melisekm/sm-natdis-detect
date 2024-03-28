package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.exception.DefaultException;
import com.pep.ProxyEntryPoint.rest.api.LinkControllerApi;
import com.pep.ProxyEntryPoint.rest.dto.LinkCountOutput;
import com.pep.ProxyEntryPoint.rest.dto.LinkDownloadOutputList;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.model.entity.Link;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import com.pep.ProxyEntryPoint.service.LinkService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
public class LinkController extends AbstractController<LinkInput, LinkOutput, Link, Long> implements LinkControllerApi {

    private final LinkService linkService;
    @Autowired
    public LinkController(LinkService linkService) {
        super(linkService);
        this.linkService = linkService;
    }

    private static final String ERROR_CREATE = "Error creating link";
    private static final String ERROR_GET = "Error getting link";
    private static final String ERROR_DELETE = "Error deleting link";
    private static final String ERROR_UPDATE = "Error updating link";
    private static final String ERROR_ALL = "Error getting all links";

    @Override
    protected String getErrorEntityCreate() {
        return ERROR_CREATE;
    }

    @Override
    protected String getErrorEntityGet() {
        return ERROR_GET;
    }

    @Override
    protected String getErrorEntityDelete() {
        return ERROR_DELETE;
    }

    @Override
    protected String getErrorEntityUpdate() {
        return ERROR_UPDATE;
    }

    @Override
    protected String getErrorEntityAll() {
        return ERROR_ALL;
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

    @Override
    public ResponseEntity<List<LinkOutput>> findAllLinks() {
        return super.findAll();
    }

    @Override
    public LinkCountOutput getLinkCount(DataInput input) {
        try {
            return linkService.getLinkCount(input);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityUpdate(), e);
        }
    }

    @Override
    public LinkDownloadOutputList downloadFromLinks(DataInput input) {
        try {
            return linkService.downloadFromLinks(input);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityUpdate(), e);
        }
    }

    @Override
    public void saveLinksToPrediction(List<LinkInput> input, Long predictionId) {
        try {
            linkService.saveLinksToPrediction(input, predictionId);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityUpdate(), e);
        }
    }
}
