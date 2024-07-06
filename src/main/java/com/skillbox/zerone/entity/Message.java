package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "read_status")
    private String readStatus = MessageStatus.UNREAD.toString();

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "dialog_id", referencedColumnName = "id")
    private Dialog dialog;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Person recipient;
}
