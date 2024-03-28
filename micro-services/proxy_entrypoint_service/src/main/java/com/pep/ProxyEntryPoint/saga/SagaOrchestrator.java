package com.pep.ProxyEntryPoint.saga;

import com.pep.ProxyEntryPoint.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class SagaOrchestrator {

    private final LinkService linkService;

    @Autowired
    public SagaOrchestrator(LinkService linkService) {
        this.linkService = linkService;
    }

//    @JmsListener(destination = "sagaQueue")
//    public void handleSaga(String message) {
//        System.out.println("Received message: " + message);
//    }
}
