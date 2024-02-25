package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.rest.api.VersionControllerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController implements VersionControllerApi {

    private static final String ERROR_GET = "Chyba pri načítaní verzie aplikácie.";

    @Value("${version}")
    private String version;

    @Override
    public String getVersion() {
        try {
            return version;
        } catch (Exception e) {
            throw new RuntimeException(ERROR_GET, e);
        }
    }
}