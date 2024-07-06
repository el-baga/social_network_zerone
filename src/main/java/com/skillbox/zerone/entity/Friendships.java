package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friendships")
public class Friendships {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sent_time")
    private LocalDateTime sentTime;
    @Column(name = "status_name")
    private String statusName;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "dst_person_id")
    private Person dstPersonId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "src_person_id")
    private Person srcPersonId;
}
