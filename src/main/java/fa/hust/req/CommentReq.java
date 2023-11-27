package fa.hust.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentReq {
    private  Long id;
    private Long storyId;
    private Long accountId;
    private String comment;
}
