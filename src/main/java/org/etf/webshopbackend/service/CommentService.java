package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Comment;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.request.CommentRequest;
import org.etf.webshopbackend.model.response.CommentResponse;
import org.etf.webshopbackend.repository.CommentRepository;
import org.etf.webshopbackend.repository.ProductRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CommentRepository commentRepository;
  private final GenericMapper<CommentRequest, Comment, CommentResponse> commentMapper;

  public CommentResponse insertProductComment(Long userId, Long productId, CommentRequest commentRequest) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException(Product.class, productId));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(User.class, userId));
    String commentMessage = commentRequest.getMessage();

    Comment comment = new Comment(user, product, commentMessage);
    comment = commentRepository.saveAndFlush(comment);

    return commentMapper.toResponse(comment, CommentResponse.class);
  }

  public List<CommentResponse> findCommentsByProductId(Long productId) {
    List<Comment> comments = commentRepository.findByProductIdOrderByDateDesc(productId);
    return commentMapper.toResponses(comments, CommentResponse.class);
  }
}
