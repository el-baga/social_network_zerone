package com.skillbox.zerone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_seq_generator")
    @SequenceGenerator(name = "tags_seq_generator", sequenceName = "tags_id_seq", allocationSize = 1)
    @Column(columnDefinition = "BIGINT")
    private long id;

    @Column
    private String tag;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();
}
