package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "captcha_id_seq")
    @SequenceGenerator(name = "captcha_id_seq", sequenceName = "captcha_id_seq", allocationSize = 1)
    private Long id;

    private String code;

    @Column(name = "secret_code")
    private String secretCode;

    private LocalDateTime time;
}
