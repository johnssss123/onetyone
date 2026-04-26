package com.onetyone.controller;

import com.onetyone.dto.CommentRequest;
import com.onetyone.dto.CommentResponse;
import com.onetyone.security.JwtUtil;
import com.onetyone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    private String getEmailFromToken(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractEmail(token);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        return ResponseEntity.ok(commentService.addComment(postId, request, email));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        commentService.deleteComment(commentId, email);
        return ResponseEntity.noContent().build();
    }
}