package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.PersonSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonSettingsRepository extends JpaRepository<PersonSettings, Long> {
}
