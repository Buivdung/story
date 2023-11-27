package fa.hust.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.hust.enums.ERoles;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private ERoles role;

    private boolean enable;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Information information;

    @OneToMany(mappedBy = "account")
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(mappedBy = "account")
    @ToString.Exclude
    private List<Payment> payments;

    @OneToMany(mappedBy = "account")
    @ToString.Exclude
    private List<Paid> paids;
}
