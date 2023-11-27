package fa.hust.entities;

import fa.hust.enums.EPaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "payment_code")
    private Long paymentCode;
    private Long amount;

    @JoinColumn(name = "bank_code")
    private String bankCode;

    @Enumerated(EnumType.STRING)
    private EPaymentStatus status;
    @Nationalized
    private String comment;
    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @ManyToOne
    @JoinColumn(name = "story_id")
    @ToString.Exclude
    private Stories story;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    private Account account;

}
