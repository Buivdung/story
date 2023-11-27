package fa.hust.service;

import fa.hust.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Comment saveComment(Comment comment);
    Page<Comment> getComment(Long storyId, Pageable pageable);
}
