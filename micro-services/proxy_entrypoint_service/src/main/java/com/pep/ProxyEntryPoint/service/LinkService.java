package com.pep.ProxyEntryPoint.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pep.ProxyEntryPoint.apiClient.ApiClient;
import com.pep.ProxyEntryPoint.converter.LinkConverter;
import com.pep.ProxyEntryPoint.model.entity.Link;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.LinkRepository;
import com.pep.ProxyEntryPoint.model.repository.PredictionRepository;
import com.pep.ProxyEntryPoint.rest.dto.ApiCallInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkCountOutput;
import com.pep.ProxyEntryPoint.rest.dto.LinkDownloadOutput;
import com.pep.ProxyEntryPoint.rest.dto.LinkDownloadOutputList;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionPredictServiceOutput;
import com.pep.ProxyEntryPoint.util.ApiClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LinkService extends AbstractService<LinkInput, LinkOutput, Link, Long>{

    private final ApiClient apiClient;
    private final JmsTemplate jmsTemplate;
    private final PredictionRepository predictionRepository;
    private final LinkConverter linkConverter;
    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository entityRepository,
                       LinkConverter entityConverter,
                       ApiClient apiClient,
                       JmsTemplate jmsTemplate,
                       PredictionRepository predictionRepository,
                       LinkRepository linkRepository) {
        super(entityRepository, entityConverter);
        this.apiClient = apiClient;
        this.jmsTemplate = jmsTemplate;
        this.predictionRepository = predictionRepository;
        this.linkConverter = entityConverter;
        this.linkRepository = linkRepository;
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
    public LinkCountOutput getLinkCount(DataInput input) throws Exception {
        ApiCallInput apiCallInput = new ApiCallInput();
        apiCallInput.setMethod(HttpMethod.POST);
        apiCallInput.setUrlPath(dlServiceUrl + "/extract_urls");

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("data", input.getData());
        apiCallInput.setBodyRequest(bodyRequest);

        List<LinkedHashMap<String, Object>> linkedHashMapList = apiClient.invokeApi(apiCallInput, List.class).getBody();
        return ApiClientUtils.convertLinkedHashMapToObject(linkedHashMapList, LinkCountOutput.class);
    }

    @Transactional
    public LinkDownloadOutputList downloadFromLinks(DataInput input) {
        ApiCallInput apiCallInput = new ApiCallInput();
        apiCallInput.setMethod(HttpMethod.POST);
        apiCallInput.setUrlPath(dlServiceUrl + "/download");

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("data", input.getData());
        apiCallInput.setBodyRequest(bodyRequest);

        List<LinkedHashMap<String, Object>> linkedHashMapList = apiClient.invokeApi(apiCallInput, List.class).getBody();
        LinkDownloadOutputList linkDownloadOutputList = new LinkDownloadOutputList();
        try {
            List<LinkDownloadOutput> outputList = convertLinkedHashMapListToObjectList(linkedHashMapList);
            linkDownloadOutputList.setOutputList(outputList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linkDownloadOutputList;
    }

    private List<LinkDownloadOutput> convertLinkedHashMapListToObjectList(List<LinkedHashMap<String, Object>> linkedHashMapList) {
        List<LinkDownloadOutput> outputList = new ArrayList<>();

        for (LinkedHashMap<String, Object> map : linkedHashMapList) {
            LinkDownloadOutput output = new LinkDownloadOutput();
            output.setOriginUrl((String) map.get("orig_url"));
            output.setFinalUrl((String) map.get("final_url"));
            output.setText((String) map.get("text"));
            output.setHtml((String) map.get("html"));
            output.setTitle((String) map.get("title"));
            output.setOtherInfo((Map<String, Object>) map.get("other_info"));
            output.setDomain((String) map.get("domain"));
            output.setPublishedAt(parseDateTime((String) map.get("published_at")));
            try {
                output.setExtractedAt(parseDateTime((String) map.get("extracted_at")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            outputList.add(output);
        }

        return outputList;
    }

    public LocalDateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    @Transactional
    public List<LinkOutput> saveLinksToPrediction(List<LinkInput> input, Long predictionId) throws Exception {
        Prediction prediction = predictionRepository.findEntityById(predictionId);
        List<Link> links = new ArrayList<>();
        for (LinkInput linkInput : input) {
            Link link = linkConverter.convertToEntity(linkInput);
            link.setPredictions(List.of(prediction));
            links.add(link);
        }
        List<Link> savedLinks = linkRepository.saveAll(links);
        return linkConverter.convertToOutputList(savedLinks);
    }
}
