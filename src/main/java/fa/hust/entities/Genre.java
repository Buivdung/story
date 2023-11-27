package fa.hust.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Genre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    private String name;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<StoriesGenre> storiesGenres;
}
