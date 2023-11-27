package fa.hust.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordReq {
    private String passwordOld;
    private String passwordNew;
    private String passwordNew2;
}
