package fa.hust.req;

import fa.hust.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReq {
    private String fullName;
    private String email;
    private String address;
    private LocalDate dob;
    private String phoneNumber;
    private EGender gender;
    private String password;
    private String password2;
}
