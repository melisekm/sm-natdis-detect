package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.apiClient.ApiClient;
import com.pep.ProxyEntryPoint.converter.LinkConverter;
import com.pep.ProxyEntryPoint.model.entity.Link;
import com.pep.ProxyEntryPoint.model.repository.LinkRepository;
import com.pep.ProxyEntryPoint.rest.dto.ApiCallInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class LinkService extends AbstractService<LinkInput, LinkOutput, Link, Long>{

    private final ApiClient apiClient;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public LinkService(LinkRepository entityRepository,
                       LinkConverter entityConverter,
                       ApiClient apiClient,
                       JmsTemplate jmsTemplate) {
        super(entityRepository, entityConverter);
        this.apiClient = apiClient;
        this.jmsTemplate = jmsTemplate;
    }

    @Value("${dl.service.url}")
    private String dlServiceUrl;

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

    @Transactional
    public Object getLinkCount(DataInput input) {
        ApiCallInput apiCallInput = new ApiCallInput();
        apiCallInput.setMethod(HttpMethod.POST);
        apiCallInput.setUrlPath(dlServiceUrl + "/extract_urls");

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("data", input.getData());
        apiCallInput.setBodyRequest(bodyRequest);

        return apiClient.invokeApi(apiCallInput, Object.class).getBody();
    }

    @Transactional
    public Object downloadFromLinks(DataInput input) {
        ApiCallInput apiCallInput = new ApiCallInput();
        apiCallInput.setMethod(HttpMethod.POST);
        apiCallInput.setUrlPath(dlServiceUrl + "/download");

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("data", input.getData());
        apiCallInput.setBodyRequest(bodyRequest);

        return apiClient.invokeApi(apiCallInput, Object.class).getBody();
    }
}
