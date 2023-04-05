package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Comment;
import org.etf.webshopbackend.model.entity.compositekey.UserProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UserProductId> {

}
