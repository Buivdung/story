package fa.hust.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeReq {
    private String numberOne;
    private String numberTwo;
    private String numberThree;
    private String numberFour;
    private String numberFive;
    private String numberSix;

}
