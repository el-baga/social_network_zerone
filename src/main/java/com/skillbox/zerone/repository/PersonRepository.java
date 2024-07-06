package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    Optional<Person> findByEmail(String email);
    Person getPersonById(Long aLong);

    Optional<Person> findPersonByEmailUUID(String emailUUID);
    Optional<Person> findPersonByChangePasswordToken(String changePasswordToken);

    List<Person> findAllByIsDeleted(boolean isDeleted);

    boolean existsByIdAndIsDeleted(Long id, boolean isDeleted);

    boolean existsByIdAndIsBlocked(Long id, boolean isBlocked);

    boolean existsByEmail(String email);
}
