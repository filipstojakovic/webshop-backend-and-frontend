package org.etf.webshopbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.Comment;
import org.etf.webshopbackend.model.request.CommentRequest;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("products")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("{id}/comments")
  public ResponseEntity<Comment> insertProductComment(@PathVariable("id") Long productId,
                                                      @RequestBody @Valid CommentRequest commentRequest,
                                                      @AuthenticationPrincipal JwtUserDetails user
  ) {
    Comment comment = commentService.insertProductComment(user.getId(), productId, commentRequest);
    return ResponseEntity.ok(comment);
  }
}
