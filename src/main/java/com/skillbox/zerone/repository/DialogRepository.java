package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Dialog;
import com.skillbox.zerone.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DialogRepository extends JpaRepository<Dialog, Integer>, JpaSpecificationExecutor<Dialog> {
    List<Dialog> findDialogByFirstPersonOrSecondPerson(Person firstPerson, Person secondPerson);
    Optional <Dialog> findDialogById(Long id);
}
