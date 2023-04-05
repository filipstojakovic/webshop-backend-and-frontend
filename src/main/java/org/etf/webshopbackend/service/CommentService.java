package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Comment;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.entity.compositekey.UserProductId;
import org.etf.webshopbackend.model.request.CommentRequest;
import org.etf.webshopbackend.repository.CommentRepository;
import org.etf.webshopbackend.repository.ProductRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CommentRepository commentRepository;

  public Comment insertProductComment(Long userId, Long productId, CommentRequest commentRequest) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException(Product.class, productId));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(User.class, userId));

    String commentMessage = commentRequest.getMessage();

    UserProductId userProductId = new UserProductId(userId, productId);

    Comment comment = new Comment(user, product, commentMessage);
//     comment.setId(userProductId);

    comment = commentRepository.saveAndFlush(comment);

    return comment;
  }
}
