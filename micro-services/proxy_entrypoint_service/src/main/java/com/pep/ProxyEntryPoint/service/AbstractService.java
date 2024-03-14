package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.converter.AbstractConverter;
import com.pep.ProxyEntryPoint.model.IID;
import com.pep.ProxyEntryPoint.model.repository.EntityRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Abstract service class for CRUD operations
 *
 * @param <I> input DTO
 * @param <O> output DTO
 * @param <E> entity
 */

public abstract class AbstractService<I, O, E extends IID<ID>, ID> {

    private final EntityRepository<E, ID> entityRepository;
    private final AbstractConverter<I, O, E> abstractConverter;

    protected AbstractService(EntityRepository<E, ID> entityRepository, AbstractConverter<I, O, E> abstractConverter) {
        this.entityRepository = entityRepository;
        this.abstractConverter = abstractConverter;
    }

    protected abstract void postConvert(I input, E entity);
    protected abstract void checkCreate(I input);
    protected abstract void checkUpdate(ID id, I input);
    protected abstract void checkDelete(ID id);
    protected abstract void checkGet(ID id);


    @Transactional
    public O create(I input) {
        checkCreate(input);
        E entity = abstractConverter.convertToEntity(input);
        postConvert(input, entity);
        E savedEntity = entityRepository.save(entity);
        return abstractConverter.convertToOutput(savedEntity);
    }

    @Transactional
    public O get(ID id) {
        checkGet(id);
        E entity = entityRepository.findById(id).orElse(null);
        return abstractConverter.convertToOutput(entity);
    }

    @Transactional
    public O update(ID id, I input) {
        checkUpdate(id, input);
        E entity = entityRepository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        E updatedEntity = abstractConverter.convertToEntity(input);
        updatedEntity.setId(id);
        postConvert(input, updatedEntity);
        E savedEntity = entityRepository.save(updatedEntity);
        return abstractConverter.convertToOutput(savedEntity);
    }

    @Transactional
    public void delete(ID id) throws Exception {
        checkDelete(id);
        E entity = entityRepository.findEntityById(id);
        entityRepository.delete(entity);
    }

    @Transactional
    public List<O> findAll() {
        List<E> entities = entityRepository.findAll();
        return abstractConverter.convertToOutputList(entities);
    }

    @Transactional
    public E findEntityById(ID id) throws Exception {
        return entityRepository.findEntityById(id);
    }
}