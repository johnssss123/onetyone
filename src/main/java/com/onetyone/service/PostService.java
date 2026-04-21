package com.onetyone.service;

import com.onetyone.dto.PostRequest;
import com.onetyone.dto.PostResponse;
import com.onetyone.entity.Post;
import com.onetyone.entity.User;
import com.onetyone.repository.PostRepository;
import com.onetyone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.onetyone.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createPost(PostRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        Post saved = postRepository.save(post);
        return mapToResponse(saved);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return mapToResponse(post);
    }

    public PostResponse updatePost(Long id, PostRequest request, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You can only edit your own posts");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return mapToResponse(postRepository.save(post));
    }

    public void deletePost(Long id, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own posts");
        }

        postRepository.delete(post);
    }

    private PostResponse mapToResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getCreatedAt()
        );
    }
}
