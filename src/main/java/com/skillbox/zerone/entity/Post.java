package com.skillbox.zerone.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLOrder;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_seq_generator")
    @SequenceGenerator(name = "posts_seq_generator", sequenceName = "posts_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "post_text")
    private String postText;

    @Column(name ="time")
    private LocalDateTime time;

    @Column(name = "time_delete")
    private LocalDateTime timeDelete;

    @Column(name ="title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person author;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(
            name = "post2tag",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @SQLRestriction("parent_id is null and is_blocked is false")
    @SQLOrder("time asc")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @SQLRestriction("type = 'Post'")
    @ToString.Exclude
    private List<Like> likesList = new ArrayList<>();
}
