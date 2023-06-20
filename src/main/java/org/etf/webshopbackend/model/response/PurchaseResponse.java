package org.etf.webshopbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.etf.webshopbackend.model.entity.PaymentMethod;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {

  private ProductResponse product;
//   private UserResponse user;
  private PaymentMethod paymentMethod;
  private String value;
  private LocalDateTime date;
}
