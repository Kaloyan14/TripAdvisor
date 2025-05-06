package org.example;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    private Location location;

    @Lob
    private String text;

    public Comment(User user, LocalDateTime timestamp, Location location, String text) {
        this.user = user;
        this.timestamp = timestamp;
        this.location = location;
        this.text = text;
    }
}

