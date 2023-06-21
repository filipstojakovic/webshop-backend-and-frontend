package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.PaymentMethod;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.Purchase;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.request.PurchaseRequest;
import org.etf.webshopbackend.model.response.PurchaseResponse;
import org.etf.webshopbackend.repository.PaymentMethodRepository;
import org.etf.webshopbackend.repository.ProductRepository;
import org.etf.webshopbackend.repository.PurchaseRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PurchaseService {

  private final ProductRepository productRepository;
  private final PurchaseRepository purchaseRepository;
  private final PaymentMethodRepository paymentMethodRepository;
  private final UserRepository userRepository;
  private final GenericMapper<Purchase, Purchase, PurchaseResponse> purchaseMapper;

  public List<PurchaseResponse> findById(Long userId) {
    List<Purchase> purchases = purchaseRepository.findByUserId(userId);
    return purchaseMapper.toResponses(purchases, PurchaseResponse.class);
  }

  public PurchaseResponse purchaseProduct(Long productId, PurchaseRequest purchaseRequest, Long userId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException(Product.class, productId));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(User.class, userId));

    PaymentMethod paymentMethod = paymentMethodRepository.findById(purchaseRequest.getPaymentMethodId())
        .orElseThrow(() -> new NotFoundException(PaymentMethod.class, purchaseRequest.getPaymentMethodId()));

    product.setIsPurchased(true);

    Purchase purchase = Purchase.builder()
        .user(user)
        .product(product)
        .paymentMethod(paymentMethod)
        .value(purchaseRequest.getValue())
        .date(LocalDateTime.now())
        .build();

    purchase = purchaseRepository.saveAndFlush(purchase);
    return purchaseMapper.toResponse(purchase, PurchaseResponse.class);
  }
}
