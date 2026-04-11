package com.onetyone.controller;

import com.onetyone.dto.PostRequest;
import com.onetyone.dto.PostResponse;
import com.onetyone.security.JwtUtil;
import com.onetyone.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    private String getEmailFromToken(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractEmail(token);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        return ResponseEntity.ok(postService.createPost(request, email));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        return ResponseEntity.ok(postService.updatePost(id, request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        postService.deletePost(id, email);
        return ResponseEntity.noContent().build();
    }
}