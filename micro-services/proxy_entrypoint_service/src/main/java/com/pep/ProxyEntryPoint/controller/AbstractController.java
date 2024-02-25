package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.model.IID;
import com.pep.ProxyEntryPoint.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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

    protected AbstractController(AbstractService<I, O, E, ID> abstractService) {
            this.abstractService = abstractService;
    }

    public ResponseEntity<O> create(@RequestBody I input) {
        try {
            O output = abstractService.create(input);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error creating entity", e);
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<O> get(@PathVariable ID id) {
        try {
            O output = abstractService.get(id);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting entity", e);
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<O> update(@PathVariable ID id, @RequestBody I input) {
        try {
            O output = abstractService.update(id, input);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating entity", e);
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Void> delete(@PathVariable ID id) {
        try {
            abstractService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deleting entity", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
