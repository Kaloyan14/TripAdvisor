package org.example;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Images {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private Location location;

    @Lob
    @Column(name = "picture", columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] picture;

    public Images(Location location, byte[] picture) {
        this.location = location;
        this.picture = picture;
    }

}
