package com.onetyone.service;

import com.onetyone.dto.CommentRequest;
import com.onetyone.dto.CommentResponse;
import com.onetyone.entity.Comment;
import com.onetyone.entity.Post;
import com.onetyone.entity.User;
import com.onetyone.exception.ResourceNotFoundException;
import com.onetyone.repository.CommentRepository;
import com.onetyone.repository.PostRepository;
import com.onetyone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponse addComment(Long postId, CommentRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post)
                .build();

        return mapToResponse(commentRepository.save(comment));
    }

    public List<CommentResponse> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername(),
                comment.getCreatedAt()
        );
    }
}