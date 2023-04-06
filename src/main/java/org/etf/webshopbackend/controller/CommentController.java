package org.etf.webshopbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.Comment;
import org.etf.webshopbackend.model.request.CommentRequest;
import org.etf.webshopbackend.model.response.CommentResponse;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("products")
public class CommentController {

  private final CommentService commentService;

  @GetMapping("{id}/comments")
  public ResponseEntity<List<CommentResponse>> insertProductComment(@PathVariable("id") Long productId
  ) {
    List<CommentResponse> commentResponses = commentService.findCommentsByProductId(productId);
    return ResponseEntity.ok(commentResponses);
  }

  @PostMapping("{id}/comments")
  public ResponseEntity<Comment> insertProductComment(@PathVariable("id") Long productId,
                                                      @RequestBody @Valid CommentRequest commentRequest,
                                                      @AuthenticationPrincipal JwtUserDetails user
  ) {
    Comment comment = commentService.insertProductComment(user.getId(), productId, commentRequest);
    return ResponseEntity.ok(comment);
  }
}
