package org.etf.webshopbackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
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
  private PaymentMethod paymentMethod;
  private String value;
  private LocalDateTime date;
  @OneToOne(fetch = FetchType.EAGER)
  @MapsId
  @JoinColumn(name = "product_id")
  private Product product;
}
