package com.skillbox.zerone.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLOrder;
import org.hibernate.annotations.SQLRestriction;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "post_comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq_generator")
    @SequenceGenerator(name = "comments_seq_generator", sequenceName = "post_comments_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "time_delete")
    private LocalDateTime timeDelete;
    
    @Column(name ="time")
    private LocalDateTime time;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "post_id")
    private Long postId;

    @OneToMany(mappedBy = "parentId")
    @SQLOrder("time asc")
    @ToString.Exclude
    private List<Comment> subComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @SQLRestriction("type = 'Comment'")
    @ToString.Exclude
    private List<Like> likesList = new ArrayList<>();
}
