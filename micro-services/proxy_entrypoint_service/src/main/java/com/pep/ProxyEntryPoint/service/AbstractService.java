package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.converter.AbstractConverter;
import com.pep.ProxyEntryPoint.model.IID;
import com.pep.ProxyEntryPoint.model.repository.EntityRepository;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public O create(I input) {
        E entity = abstractConverter.convertToEntity(input);
        E savedEntity = entityRepository.save(entity);
        return abstractConverter.convertToOutput(savedEntity);
    }

    @Transactional
    public O get(ID id) {
        E entity = entityRepository.findById(id).orElse(null);
        return abstractConverter.convertToOutput(entity);
    }

    @Transactional
    public O update(ID id, I input) {
        E entity = entityRepository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        E updatedEntity = abstractConverter.convertToEntity(input);
        updatedEntity.setId(id);
        E savedEntity = entityRepository.save(updatedEntity);
        return abstractConverter.convertToOutput(savedEntity);
    }

    @Transactional
    public void delete(ID id) throws Exception {
        E entity = entityRepository.findEntityById(id);
        entityRepository.delete(entity);
    }
}