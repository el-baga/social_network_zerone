package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_seq_generator")
    @SequenceGenerator(name = "like_seq_generator", sequenceName = "likes_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "entity_id")
    private long entityId;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person person;

    @ManyToOne
    @JoinColumn(name = "entity_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private Post post;

    @ManyToOne
    @JoinColumn(name = "entity_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private Comment comment;
}
