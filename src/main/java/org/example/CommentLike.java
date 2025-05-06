package org.example;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comment_like")
public class CommentLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    private Comment comment;

    public CommentLike(User user, LocalDateTime timestamp, Comment comment) {
        this.user = user;
        this.timestamp = timestamp;
        this.comment = comment;
    }
}

