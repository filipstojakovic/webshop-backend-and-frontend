package org.etf.webshopbackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private Double price;
  private String location;
  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private Boolean isNew;
  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private Boolean isPurchased = false;
  private LocalDateTime date = LocalDateTime.now();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private List<ProductImage> productImages = new ArrayList<>();

  @OneToOne(mappedBy = "product")
  private Purchase purchase;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "seller_id")
  private User seller;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<ProductHasAttribute> productHasAttributes = new ArrayList<>();

}
