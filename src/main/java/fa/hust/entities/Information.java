package fa.hust.entities;



import fa.hust.enums.EGender;
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
public class Information implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Nationalized
    private String address;
    private String avatar;

    private LocalDate dob;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "account", unique = true)
    private Account account;

}
