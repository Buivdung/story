package fa.hust.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Paid implements Serializable {

    @EmbeddedId
    private KeyPaid id;

    @CreationTimestamp
    @Column(name = "create_date",updatable = false)
    private LocalDate createDate;


    @ManyToOne
    @MapsId("storyId")
    @JoinColumn(name = "story_id")
    @ToString.Exclude
    private Stories story;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    private Account account;
}
