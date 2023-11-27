package fa.hust.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class KeyPaid implements Serializable {
    @Column(name = "story_id")
    private Long storyId;

    @Column(name = "account_id")
    private Long accountId;
}
