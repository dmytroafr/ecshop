package com.echem.ecshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Product {

    private static final String SEQ_NAME = "product_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    private String title;
    private BigDecimal price;
    private BigDecimal optPrice;
    private String productDescription;
    private String photoUrl;
    private String producer;
    private String countryProducer;
    @Enumerated(EnumType.STRING)
    private OnStock onStock;
    
    @Column(name = "order_count")
    private Long orderCount = 0L;

    // @BatchSize оптимізує завантаження categories: замість N+1 запитів буде 1 + ceil(N/25) запитів
    // Використовується коли categories потрібні при pagination з @EntityGraph
    @org.hibernate.annotations.BatchSize(size = 25)
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    public boolean isAvailable(){
        return this.onStock == OnStock.ON_STOCK;
    }
}