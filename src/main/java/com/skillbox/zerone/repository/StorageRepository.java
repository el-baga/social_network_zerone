package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long>, JpaSpecificationExecutor<Storage>
{
    void deleteByOwnerId(Long id);

    boolean existsByOwnerId(Long id);

    Storage findByOwnerId(Long id);
}
