package fa.hust.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchReq {
    private String param;
    private Integer pageSize;
    private Integer pageNumber;
    private String alert;
    private String massage;
    private List<Integer> pageMax;
}
