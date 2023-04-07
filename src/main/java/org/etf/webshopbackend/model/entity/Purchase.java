package org.etf.webshopbackend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Purchase {

  @Id
  @Column(name = "product_id")
  private Long productId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_method_id")
  private PaymentMethod paymentMethod;

  @OneToOne(fetch = FetchType.EAGER)
  @MapsId
  @JoinColumn(name = "product_id")
  private Product products;
}
