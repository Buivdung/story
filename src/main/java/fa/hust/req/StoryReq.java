package fa.hust.req;

import fa.hust.enums.EStatusStory;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoryReq {
    private Long id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String author;
    private MultipartFile thumbnail;
    private EStatusStory status;
    private boolean type;
    private long money;
    private long review;
    @NotEmpty
    private MultipartFile introduction;
    private long chapterTotal;
    @NotEmpty
    private List<Long> genreId;
}
