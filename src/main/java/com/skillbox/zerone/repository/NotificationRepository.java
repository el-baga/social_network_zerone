package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    List<Notification> findAllByPersonId(Long personId);

    List<Notification> findAllBySenderId(Long senderId);
}
