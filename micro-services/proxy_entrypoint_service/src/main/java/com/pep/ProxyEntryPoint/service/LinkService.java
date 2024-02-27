package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.converter.LinkConverter;
import com.pep.ProxyEntryPoint.model.entity.Link;
import com.pep.ProxyEntryPoint.model.repository.LinkRepository;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import org.springframework.stereotype.Service;

@Service
public class LinkService extends AbstractService<LinkInput, LinkOutput, Link, Long>{

    public LinkService(LinkRepository entityRepository, LinkConverter entityConverter) {
        super(entityRepository, entityConverter);
    }

    @Override
    protected void postConvert(LinkInput input, Link entity) {

    }

    @Override
    protected void checkCreate(LinkInput input) {

    }

    @Override
    protected void checkUpdate(Long aLong, LinkInput input) {

    }

    @Override
    protected void checkDelete(Long aLong) {

    }

    @Override
    protected void checkGet(Long aLong) {

    }
}
