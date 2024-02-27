package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.exception.DefaultException;
import com.pep.ProxyEntryPoint.model.IID;
import com.pep.ProxyEntryPoint.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Abstract controller for basic CRUD operations.
 *
 * @param <I> input DTO
 * @param <O> output DTO
 * @param <E> entity
 */

@Slf4j
@Validated
public abstract class AbstractController<I, O, E extends IID<ID>, ID> {

    private final AbstractService<I, O, E, ID> abstractService;

    protected abstract String getErrorEntityCreate();
    protected abstract String getErrorEntityGet();
    protected abstract String getErrorEntityDelete();
    protected abstract String getErrorEntityUpdate();
    protected abstract String getErrorEntityAll();

    protected AbstractController(AbstractService<I, O, E, ID> abstractService) {
            this.abstractService = abstractService;
    }

    public ResponseEntity<O> create(@RequestBody I input) {
        try {
            O output = abstractService.create(input);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityCreate(), e);
        }
    }

    public ResponseEntity<O> get(@PathVariable ID id) {
        try {
            O output = abstractService.get(id);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityGet(), e);
        }
    }

    public ResponseEntity<O> update(@PathVariable ID id, @RequestBody I input) {
        try {
            O output = abstractService.update(id, input);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityUpdate(), e);
        }
    }

    public ResponseEntity<Void> delete(@PathVariable ID id) {
        try {
            abstractService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityDelete(), e);
        }
    }

    public ResponseEntity<List<O>> findAll() {
        try {
            List<O> output = abstractService.findAll();
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityAll(), e);
        }
    }
}
