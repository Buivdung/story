package fa.hust.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.hust.enums.EStatusStory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Stories implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    private String name;

    @Nationalized
    private String author;
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    private EStatusStory status;
    private boolean type;
    private Long money;
    private Long review;
    private String introduction;

    @Transient
    private List<Long> genreId;

    @Column(name = "chapter_total")
    private Long chapterTotal;

    @CreationTimestamp
    @Column(name = "create_date",updatable = false)
    private LocalDate createDate;

    @OneToMany(mappedBy = "story",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<StoriesGenre> storiesGenres;

    @OneToMany(mappedBy = "stories", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Payment> payments;

    @OneToMany(mappedBy = "story",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Paid> paids;
}
