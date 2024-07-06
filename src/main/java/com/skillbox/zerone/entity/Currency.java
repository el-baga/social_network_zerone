package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "currencies")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currencies_id_seq")
    @SequenceGenerator(name = "currencies_id_seq", sequenceName = "currencies_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String price;

    private LocalDateTime updateTime;
}
