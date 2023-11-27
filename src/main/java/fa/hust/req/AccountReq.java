package fa.hust.req;

import fa.hust.enums.EGender;
import fa.hust.enums.ERoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountReq {
    private Long id;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotNull
    private ERoles role;


    private boolean enable;


    @NotEmpty
    private String fullName;

    @NotNull
    private EGender gender;

    @NotEmpty
    private String phoneNumber;

    @NotNull
    private String address;

    @Past
    private LocalDate dob;
}
