package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dialogs")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_person_id")
    private Person firstPerson;

    @ManyToOne
    @JoinColumn(name = "second_person_id")
    private Person secondPerson;

    @Column(name = "last_active_time")
    private LocalDateTime lastActiveTime;

    @Column(name = "last_message_id")
    private Long lastMessageId;

    @OneToMany(mappedBy = "dialog")
    private List<Message> messageList;
}
