package com.pep.ProxyEntryPoint.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface EntityRepository<E, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E>, org.springframework.data.repository.Repository<E, ID> {

    default E findEntityById(ID id) throws Exception {
        Optional<E> entityOptional = findById(id);
        if (entityOptional.isEmpty()) {
            throw new Exception("Entity not found");
        }

        return entityOptional.get();
    }
}