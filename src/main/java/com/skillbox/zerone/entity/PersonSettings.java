package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "person_settings")
public class PersonSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_settings_id_seq")
    @SequenceGenerator(name = "person_settings_id_seq", sequenceName = "person_settings_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_comment")
    private Boolean commentComment = true;

    @Column(name = "friend_birthday")
    private Boolean friendBirthday = true;

    @Column(name = "friend_request")
    private Boolean friendRequest = true;

    @Column(name = "message")
    private Boolean message = true;

    @Column(name = "post")
    private Boolean post = true;


    @Column(name = "post_comment")
    private Boolean postComment = true;

    @Column(name = "post_like")
    private Boolean postLike = true;

    @OneToOne(mappedBy = "personSettings")
    @ToString.Exclude
    private Person person;
}
