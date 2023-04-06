package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Comment;
import org.etf.webshopbackend.model.entity.compositekey.UserProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UserProductId> {

  List<Comment> findByIdProductId(Long productId);
}
