package com.pep.ProxyEntryPoint.model.repository;

import com.pep.ProxyEntryPoint.model.entity.DbEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DbEntityRepository extends EntityRepository<DbEntity, Long>{
}
