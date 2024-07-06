package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications_id_seq")
    @SequenceGenerator(name = "notifications_id_seq", sequenceName = "notifications_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "contact")
    private String contact;

    @Column(name = "entity_id")
    private Long entityId;

    @Builder.Default
    @Column(name = "is_read")
    private Boolean isRead = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Builder.Default
    @Column(name = "sent_time")
    private LocalDateTime sentTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person sender;
}
