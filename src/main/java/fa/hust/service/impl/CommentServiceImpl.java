package fa.hust.service.impl;


import fa.hust.entities.Comment;
import fa.hust.repositories.CommentRepository;
import fa.hust.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    final private CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getComment(Long storyId,Pageable pageable) {
        return commentRepository.findAll(storyId,pageable);
    }
}
