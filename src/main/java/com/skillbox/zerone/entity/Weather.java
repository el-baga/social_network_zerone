package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_id_seq")
    @SequenceGenerator(name = "weather_id_seq", sequenceName = "weather_id_seq", allocationSize = 1)
    @Column(columnDefinition = "bigint")
    private long id;

    @Column(name = "open_weather_ids")
    private String openWeatherId;

    @Column(columnDefinition = "character varying(255)")
    private String city;

    @Column(columnDefinition = "character varying(255)")
    private String clouds;

    @Column(columnDefinition = "timestamp(6) without time zone")
    private LocalDateTime date;

    @Column(columnDefinition = "double precision")
    private double temp;
}
