package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Dialog;
import com.skillbox.zerone.entity.Message;
import com.skillbox.zerone.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer>, JpaSpecificationExecutor<Message> {
    List<Message> findMessageByDialogAndIsDeleted(Dialog dialog, Boolean isDeleted);
    Optional<Message> findById(Long id);

    List<Message> findMessageByDialogAndRecipientAndReadStatusAndIsDeleted(Dialog dialog, Person user, String status,
                                                                              Boolean isDeleted);

    List<Message> findAllByIsDeleted(boolean b);

    List<Message> findMessageByRecipientAndReadStatusAndIsDeleted(Person user, String toString, boolean isDeleted);
    List<Message> findByAuthorAndRecipient(Person author, Person recipient);
}
