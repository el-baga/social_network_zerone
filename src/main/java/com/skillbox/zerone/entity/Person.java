package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persons_id_seq")
    @SequenceGenerator(name = "persons_id_seq", sequenceName = "persons_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "e_mail")
    private String email;

    @Column(name = "email_uuid")
    private String emailUUID;

    @Column(name = "email_uuid_time")
    private LocalDateTime emailUUIDTime;

    private String phone;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    private String about;

    @Column(name = "change_password_token")
    private String changePasswordToken;

    @Column(name = "change_password_token_time")
    private LocalDateTime changePasswordTokenTime;

    private String city;

    @Column(name = "configuration_code")
    private Integer configurationCode;

    private String country;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnlineTime;

    @Column(name = "message_permissions")
    private String messagePermissions;

    @Column(name = "notifications_session_id")
    private String notificationsSessionId;

    @Column(name = "online_status")
    private Boolean onlineStatus;

    private String password;

    private String photo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "telegram_id")
    private Long telegramId;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "person_settings_id", referencedColumnName = "id")
    private PersonSettings personSettings;

    @ToString.Exclude
    @OneToMany(mappedBy = "dstPersonId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Friendships> dstFriends = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "srcPersonId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Friendships> srcFriends = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "firstPerson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dialog> firstDialog = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "secondPerson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dialog> secondDialog = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "firstPerson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dialog> authorMessages = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "secondPerson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dialog> recipientMessages = new ArrayList<>();
}
