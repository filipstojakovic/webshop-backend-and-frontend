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

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Purchase {

  @Id
  @Column(name = "product_id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;


  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "payment_method_id")
  private PaymentMethod paymentMethod; //TODO: need card number value/attribute

  private LocalDateTime date;

  @OneToOne(fetch = FetchType.EAGER)
  @MapsId
  @JoinColumn(name = "product_id")
  private Product product;
}
