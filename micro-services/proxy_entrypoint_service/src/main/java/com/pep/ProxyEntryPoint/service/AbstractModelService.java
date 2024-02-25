package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.model.repository.EntityRepository;
import org.glassfish.jaxb.core.v2.model.core.ID;

public class AbstractModelService<I, O, L, SO, LR, RF, E> {

    protected EntityRepository<E, ID> entityRepository;
    public AbstractModelService(EntityRepository<E, ID> entityRepository) {
        this.entityRepository = entityRepository;
    }

    protected String getErrorEntityCreate() {
        return null;
    }

    protected String getErrorEntityGet() {
        return null;
    }

    protected String getErrorEntityDelete() {
        return null;
    }

    protected String getErrorEntityUpdate() {
        return null;
    }

    protected String getErrorEntityAll() {
        return null;
    }

    protected void onCreateException(I input) {
    }

    protected void onUpdateException(E id) {
    }

    protected void onGetException(E id) {
    }

    protected void onDeleteException(E id) {
    }

    protected void onFilterShortDetailsException(SO shortRequest) {
    }

    public O create(I input) {
        return null;
    }

    public O get(E id) {
        return null;
    }

    public O update(E id, I input) {
        return null;
    }

    public void delete(E id) {
    }

    public L filter(LR listRequest) {
        return null;
    }
}
