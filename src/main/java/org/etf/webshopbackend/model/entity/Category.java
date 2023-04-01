package org.etf.webshopbackend.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private List<Attribute> attributes = new ArrayList<>();


  @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Category> subCategories = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "parent_category_id")
  private Category parentCategory;
  //@OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
  //private List<Category> subCategories = new ArrayList<>();
}
