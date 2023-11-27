package fa.hust.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StoriesGenre {

    @EmbeddedId
    private KeyStoriesGenre id;

    @ManyToOne
    @ToString.Exclude
    @MapsId("storyId")
    @JoinColumn(name = "story_id")
    private Stories story;


    @ManyToOne
    @MapsId("genreId")
    @ToString.Exclude
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
