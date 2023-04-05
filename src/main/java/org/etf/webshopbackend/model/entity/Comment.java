package org.etf.webshopbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.etf.webshopbackend.model.entity.compositekey.UserProductId;

import java.time.LocalDateTime;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Comment {

  @EmbeddedId
  UserProductId id = new UserProductId();

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productId")
  @JoinColumn(name = "product_id")
  private Product product;

  @NotNull
  private String message;

  private LocalDateTime date;

  public Comment(final User user, final Product product, final String message, final LocalDateTime date) {
    this.user = user;
    this.product = product;
    this.date = date;
    this.message = message;
  }

  public Comment(final User user, final Product product, final String message) {
    this(user, product, message, LocalDateTime.now());
  }
}
