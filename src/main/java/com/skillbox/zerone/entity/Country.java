package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "external_id")
    private Long externalId;

    @ToString.Exclude
    @OneToMany(mappedBy = "country")
    private List<City> cities;
}
