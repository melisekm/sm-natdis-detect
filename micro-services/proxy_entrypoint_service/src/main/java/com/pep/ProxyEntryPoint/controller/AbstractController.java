package com.pep.ProxyEntryPoint.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
public abstract class AbstractController<I, O, L, SO, LR, RF, E> {

        public AbstractController() {
        }

        protected abstract String getErrorEntityCreate();
        protected abstract String getErrorEntityGet();
        protected abstract String getErrorEntityDelete();
        protected abstract String getErrorEntityUpdate();
        protected abstract String getErrorEntityAll();

        protected void onCreateException(I input) {}
        protected void onUpdateException(E id) {}
        protected void onGetException(E id) {}
        protected void onDeleteException(E id) {}
        protected void onFilterShortDetailsException(SO shortRequest) {}

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
